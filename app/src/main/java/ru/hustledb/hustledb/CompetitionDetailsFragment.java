package ru.hustledb.hustledb;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.Events.OnPreregistrationLoadCompleteEvent;
import ru.hustledb.hustledb.ValueClasses.Competition;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.GONE;
import static android.view.View.SCROLL_AXIS_HORIZONTAL;

public class CompetitionDetailsFragment extends Fragment implements RecyclerView.OnItemTouchListener {
    private static final String C_ID = "competitionId";
    private static final String C_URL = "competitionUrl";
    private static final String C_TITLE = "competitionTitle";
    private static final String C_DATE = "competitionDate";
    private static final String C_CITY = "competitionCity";
    private static final String C_DESC = "competitionDesc";

    private Competition competition;
    private CompositeSubscription subscriptions;
    private CompetitionsListFragment.CompetitionsListener listener;
    private RecyclerView.LayoutManager layoutManager;
    private PreregistrationRecyclerAdapter recyclerAdapter;
    private GestureDetectorCompat gestureDetector;

//    @BindView(R.id.dtSwipeRefreshLayout)
//    SwipeRefreshLayout dtSwipeRefreshLayout;
    @BindView(R.id.dtTitle)
    TextView dtTitle;
    @BindView(R.id.dtCity)
    TextView dtCity;
    @BindView(R.id.dtDate)
    TextView dtDate;
    @BindView(R.id.dtDesc)
    TextView dtDesc;
    @BindView(R.id.dtListTitle)
    TextView dtListTitle;
    @BindView(R.id.dtRecyclerView)
    RecyclerView dtRecyclerView;
    @Inject
    PreregistrationCache preregistrationCache;
    @Inject
    RxBus bus;

    public CompetitionDetailsFragment() {
        // Required empty public constructor
    }

    public static CompetitionDetailsFragment newInstance(Competition competition) {
        CompetitionDetailsFragment fragment = new CompetitionDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(C_ID, competition.getId());
        args.putString(C_URL, competition.getUrl());
        args.putString(C_TITLE, competition.getTitle());
        args.putString(C_DATE, competition.getDate());
        args.putString(C_CITY, competition.getCity());
        args.putString(C_DESC, competition.getDesc());
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof CompetitionsListFragment.CompetitionsListener)) {
            throw new IllegalArgumentException("Parent activity must implement" + CompetitionsListFragment.CompetitionsListener.class);
        }
        listener = (CompetitionsListFragment.CompetitionsListener) context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        if (getArguments() != null && competition == null) {
            Bundle bundle = getArguments();
            competition = new Competition(bundle.getInt(C_ID),
                    bundle.getString(C_URL),
                    bundle.getString(C_TITLE),
                    bundle.getString(C_DATE),
                    bundle.getString(C_DESC),
                    bundle.getString(C_CITY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_details, container, false);
        ButterKnife.bind(this, view);
//        displayPreregistrationInfo.setOnClickListener(v -> listener.onPreregistrationInfoClicked(competition.getId()));
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        dtRecyclerView.setLayoutManager(layoutManager);
        dtRecyclerView.setHasFixedSize(true);
        recyclerAdapter = new PreregistrationRecyclerAdapter(null);
        dtRecyclerView.setAdapter(recyclerAdapter);
        dtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dtRecyclerView.addOnItemTouchListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dtRecyclerView.getContext(),
                LinearLayout.VERTICAL);
        dtRecyclerView.addItemDecoration(dividerItemDecoration);
        gestureDetector = new GestureDetectorCompat(getActivity(), new CompetitionDetailsFragment.RecyclerViewOnGestureListener());
    }
    @Override
    public void onResume() {
        super.onResume();
        preregistrationCache.registerPreregistrationObserver(recyclerAdapter);
        subscriptions = new CompositeSubscription();
        subscriptions.add(bus.asObservable()
                .subscribe(o -> {
                    if (o instanceof OnPreregistrationLoadCompleteEvent) {
                        onLoadComplete((OnPreregistrationLoadCompleteEvent) o);
                    }
                })
        );
        if(!competition.getTitle().equals("")) {
            ((View) dtTitle.getParent()).setVisibility(View.VISIBLE);
            if(!competition.getUrl().equals("")){
                dtTitle.setMovementMethod(LinkMovementMethod.getInstance());
                String link = "<a href=" + competition.getUrl() + ">" +
                        competition.getTitle() + "</a>";
                dtTitle.setText(Html.fromHtml(link));
            } else {
                dtTitle.setText(competition.getTitle());
            }
        } else {
            ((View) dtTitle.getParent()).setVisibility(GONE);
        }
        if(!competition.getCity().equals("")){
            dtCity.setText(competition.getCity());
            ((View) dtCity.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) dtCity.getParent()).setVisibility(GONE);
        }
        if(!competition.getDate().equals("")) {
            dtDate.setText(competition.getPrettyDate());
            ((View) dtDate.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) dtDate.getParent()).setVisibility(GONE);
        }
        if(!competition.getDesc().equals("")) {
            dtDesc.setText(competition.getDesc());
            ((View) dtDesc.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) dtDesc.getParent()).setVisibility(GONE);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        recyclerAdapter.onNext(null);
        dtListTitle.setText("");
        preregistrationCache.unregisterPreregistrationObserver(recyclerAdapter);
        subscriptions.unsubscribe();
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
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
//        swipeRefreshLayout.setRefreshing(false);
        if (event.isError()) {
            dtListTitle.setText("Информация о пререгистрации недоступна");
            //Toast.makeText(getContext(), "Произошла ошибка загрузки", Toast.LENGTH_SHORT).show();
        } else{
            dtListTitle.setText("Номинации и регистрации");
        }
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            View view = dtRecyclerView.findChildViewUnder(event.getX(), event.getY());
            if(view != null) {
                preregistrationCache.selectNomination(((TextView)view.findViewById(R.id.piClas)).getText().toString());
                listener.onNominationClicked();
            }
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            super.onLongPress(event);
        }
    }
}
