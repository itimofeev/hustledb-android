package ru.hustledb.hustledb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.Events.OnCompetitionsLoadCompleteEvent;
import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sergey on 25.02.17.
 */

public class ContestsSublistFragment extends Fragment implements RecyclerView.OnItemTouchListener {
    private static final String POSITION_ARG = "positionArg";
    @Inject
    RxBus bus;
    @Inject
    ContestsCache contestsCache;
    @BindView(R.id.fclRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fclSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CompositeSubscription subscriptions;

    /**
     * Position in the ViewPager, position > 1 corresponds to the year of the contest
     * in descending order, down from the current year. Position = 0 is for the contests to be.
     */
    private int position;

    private ContestsSublistFragment.CompetitionsListener listener;
    private ContestsRecyclerAdapter competitionsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GestureDetectorCompat gestureDetector;

    public static ContestsSublistFragment newInstance(int position) {
        ContestsSublistFragment fragment = new ContestsSublistFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_ARG, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof ContestsSublistFragment.CompetitionsListener)) {
            throw new IllegalArgumentException("Parent activity must implement" + ContestsSublistFragment.CompetitionsListener.class);
        }
        listener = (ContestsSublistFragment.CompetitionsListener) context;
        //setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION_ARG);
        } else {
            position = 0;
        }
        App.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contests_sublist, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(listener);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        competitionsAdapter = new ContestsRecyclerAdapter(null);
        competitionsAdapter.setPagerPosition(position);
        recyclerView.setAdapter(competitionsAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new RecyclerViewOnGestureListener());
        contestsCache.registerObserverByYear(competitionsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        subscriptions = new CompositeSubscription();
        subscriptions.add(bus.asObservable()
                .subscribe(o -> {
                    if (o instanceof OnCompetitionsLoadCompleteEvent) {
                        onLoadComplete((OnCompetitionsLoadCompleteEvent) o);
                    }
                })
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onBackToMain();
    }

    @Override
    public void onStop() {
        super.onStop();
        contestsCache.unregisterObserver(competitionsAdapter);
        subscriptions.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            View view = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (view != null) {
                listener.onCompetitionClicked(contestsCache.getCompetitionById((int) view.getTag()));
            }
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
        }
    }

    public void onLoadComplete(OnCompetitionsLoadCompleteEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        if (event.isError()) {
            Toast.makeText(getContext(), "Произошла ошибка загрузки", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    interface CompetitionsListener extends SwipeRefreshLayout.OnRefreshListener {

        void onBackToMain();

        void onCompetitionClicked(Contest contest);

        void onPreregistrationInfoClicked(int f_competition_id);

        void onNominationClicked();
    }
}
