package ru.hustledb.hustledb;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import ru.hustledb.hustledb.ValueClasses.Competition;
import rx.subscriptions.CompositeSubscription;

import static ru.hustledb.hustledb.R.id.toolbar;

public class CompetitionsListFragment extends Fragment implements RecyclerView.OnItemTouchListener {

    @Inject
    RxBus bus;
    @Inject
    CompetitionsCache competitionsCache;
    @BindView(R.id.fclRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fclSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CompositeSubscription subscriptions;

    private CompetitionsListener listener;
    private CompetitionsRecyclerAdapter competitionsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GestureDetectorCompat gestureDetector;


    public static CompetitionsListFragment newInstance() {
        return new CompetitionsListFragment();
    }

    public CompetitionsListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof CompetitionsListener)) {
            throw new IllegalArgumentException("Parent activity must implement" + CompetitionsListener.class);
        }
        listener = (CompetitionsListener) context;
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
        App.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competitions_list, container, false);
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
        competitionsAdapter = new CompetitionsRecyclerAdapter(null);
        recyclerView.setAdapter(competitionsAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new RecyclerViewOnGestureListener());
        competitionsCache.registerObserver(competitionsAdapter);
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
        competitionsCache.unregisterObserver(competitionsAdapter);
        subscriptions.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            View view = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (view != null) {
                listener.onCompetitionClicked(competitionsCache.getCompetitionById((int) view.getTag()));
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

    interface CompetitionsListener extends SwipeRefreshLayout.OnRefreshListener{

        void onBackToMain();

        void onCompetitionClicked(Competition competition);

        void onPreregistrationInfoClicked(int f_competition_id);

        void onNominationClicked();
    }
}
