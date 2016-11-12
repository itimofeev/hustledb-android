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

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.Events.OnLoadCompleteEvent;

public class CompetitionsListFragment extends Fragment implements RecyclerView.OnItemTouchListener {

    @Inject
    Bus bus;
    @Inject
    CompetitionsCache competitionsCache;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CompetitionsListListener listener;
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
        if (!(context instanceof CompetitionsListListener)) {
            throw new IllegalArgumentException("Parent activity must implement" + CompetitionsListListener.class);
        }
        listener = (CompetitionsListListener) context;
        //setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        competitionsAdapter = new CompetitionsRecyclerAdapter(null);
        mRecyclerView.setAdapter(competitionsAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(getActivity(), new RecyclerViewOnGestureListener());
        competitionsCache.registerObserver(competitionsAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        bus.register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competitions_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        competitionsCache.unregisterObserver(competitionsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
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
            View view = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
            listener.onCompetitionClicked(competitionsCache.getCompetitionById((int) view.getTag()));
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
        }
    }
    @Subscribe
    public void onLoadComplete(OnLoadCompleteEvent event){
        mSwipeRefreshLayout.setRefreshing(false);
    }
    interface CompetitionsListListener extends SwipeRefreshLayout.OnRefreshListener{
        void onCompetitionClicked(Competition competition);
    }
}
