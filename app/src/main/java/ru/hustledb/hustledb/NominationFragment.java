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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.Events.OnNominationLoadCompleteEvent;
import ru.hustledb.hustledb.Events.OnPreregistrationLoadCompleteEvent;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sergey on 20.11.16.
 */

public class NominationFragment extends Fragment implements RecyclerView.OnItemTouchListener{

    @Inject
    PreregistrationCache preregistrationCache;
    @Inject
    RxBus bus;
    @BindView(R.id.nfSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.nfRecyclerView)
    RecyclerView recyclerView;


    private CompositeSubscription subscriptions;
    private CompetitionsListFragment.CompetitionsListener listener;
    private RecyclerView.LayoutManager layoutManager;
    private NominationRecyclerAdapter recyclerAdapter;
    private GestureDetectorCompat gestureDetector;

    public static NominationFragment newInstance(){
        return new NominationFragment();
    }
    public NominationFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CompetitionsListFragment.CompetitionsListener) {
            listener = (CompetitionsListFragment.CompetitionsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nomination, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(listener);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new NominationRecyclerAdapter(null);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new NominationFragment.RecyclerViewOnGestureListener());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        preregistrationCache.registerNominationObserver(recyclerAdapter);
        subscriptions = new CompositeSubscription();
        subscriptions.add(bus.asObservable()
                .subscribe(o -> {
                    if(o instanceof OnPreregistrationLoadCompleteEvent){
                        onLoadComplete((OnPreregistrationLoadCompleteEvent) o);
                    } else if(o instanceof OnNominationLoadCompleteEvent){
                        onNominationLoadComplete((OnNominationLoadCompleteEvent) o);
                    }
                }));
    }

    private void onNominationLoadComplete(OnNominationLoadCompleteEvent event) {
        if(event.isError()){
//            nfEmptyView.setVisibility(View.VISIBLE);
        } else {
//            nfEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        preregistrationCache.unregisterNominationObserver(recyclerAdapter);
        subscriptions.unsubscribe();
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
    public void onLoadComplete(OnPreregistrationLoadCompleteEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        if (event.isError()) {
            Toast.makeText(getContext(), "Произошла ошибка загрузки", Toast.LENGTH_SHORT).show();
        }
    }
    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            View view = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if(view != null) {
                recyclerAdapter.toggleView(recyclerView.getChildAdapterPosition(view),
                        (NominationRecyclerAdapter.ViewHolder)recyclerView.getChildViewHolder(view));
            }
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
        }
    }
}
