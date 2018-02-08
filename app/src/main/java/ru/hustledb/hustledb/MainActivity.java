package ru.hustledb.hustledb;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalContestsProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetContestsProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetPreregistrationProvider;
import ru.hustledb.hustledb.Events.OnCompetitionsLoadCompleteEvent;
import ru.hustledb.hustledb.Events.OnPreregistrationLoadCompleteEvent;
import ru.hustledb.hustledb.ValueClasses.Contest;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity
        implements ContestsSublistFragment.CompetitionsListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    RxBus bus;
    @Inject
    ContestsCache contestsCache;
    @Inject
    PreregistrationCache preregistrationCache;
    @Inject
    InternetContestsProvider internetContestsProvider;
    @Inject
    LocalContestsProvider localContestsProvider;
    @Inject
    InternetPreregistrationProvider internetPreregistrationProvider;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription subscriptions;

    private static final String COMPETITIONS_LIST_FRAGMENT_TAG = "contestsListFragment";
    private static final String COMPETITIONS_DETAILS_FRAGMENT_TAG = "competitionDetailFragment";
//    private static final String PREREGISTRATION_FRAGMENT_TAG = "preregistrationFragment";
    private static final String NOMINATION_FRAGMENT_TAG = "nominationFragment";
    private ContestsListFragment contestsListFragment;
    private ContestDetailsFragment contestDetailsFragment;
//    private PreregistrationFragment preregistrationFragment;
    private NominationFragment nominationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        subscriptions = new CompositeSubscription();
        subscriptions.add(bus.asObservable()
                .subscribe(o -> {
//                    if (o instanceof OnPreregistrationLoadCompleteEvent) {
//                        onPreregistrationDownloaded((OnPreregistrationLoadCompleteEvent) o);
//                    }
                })
        );
        localContestsProvider.getCompetitionsObservable().subscribe(contestsCache);
        updateCompetitions();
        setSupportActionBar(toolbar);

        if (contestsListFragment == null) {
            contestsListFragment = ContestsListFragment.newInstance();
        }
        showFragment(contestsListFragment, COMPETITIONS_LIST_FRAGMENT_TAG, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }
    private void showFragment(Fragment fragment, String tag, boolean addToBackstack) {
        if (!fragment.isVisible()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.a_mCoordinatorLayout, fragment, tag);
            if (addToBackstack) {
                transaction.addToBackStack(null);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void updateCompetitions() {
        if (hasConnection()) {
            internetContestsProvider.getContestsObservable().subscribe(localContestsProvider);
        } else {
            Toast.makeText(this, "Нет подключения к Интернет!", Toast.LENGTH_SHORT).show();
            if (bus.hasObservers()) {
                bus.send(new OnCompetitionsLoadCompleteEvent(false));
            }
        }
    }

    public void updatePreregistration(Integer f_competition_id) {
        if (hasConnection()) {
            if (f_competition_id == null) {
                f_competition_id = preregistrationCache.getCurrentPreregistrationId();
            }
            internetPreregistrationProvider.getPreregistrationObservable(f_competition_id).subscribe(preregistrationCache);
        } else {
            Toast.makeText(this, "Нет подключения к Интернет!", Toast.LENGTH_SHORT).show();
            if (bus.hasObservers()) {
                bus.send(new OnPreregistrationLoadCompleteEvent(false));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompetitionClicked(Contest contest) {
        updatePreregistration(contest.getId());
        if (contestDetailsFragment == null) {
            contestDetailsFragment = ContestDetailsFragment.newInstance();
        } else {
            contestDetailsFragment.setContest(contest);
        }
        toolbar.setLogo(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        toolbar.setTitle(contest.getTitle());
        showFragment(contestDetailsFragment, COMPETITIONS_DETAILS_FRAGMENT_TAG, true);
    }

    @Override
    public void onPreregistrationInfoClicked(int f_competition_id) {
        updatePreregistration(f_competition_id);
    }

//    public void onPreregistrationDownloaded(OnPreregistrationLoadCompleteEvent event) {
//        if (contestDetailsFragment.isVisible()) {
//            if (!event.isError()) {
//                if (preregistrationFragment == null) {
//                    preregistrationFragment = PreregistrationFragment.newInstance();
//                }
//                showFragment(preregistrationFragment, PREREGISTRATION_FRAGMENT_TAG, true);
//            } else {
//                Toast.makeText(this, "Произошла ошибка загрузки", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    @Override
    public void onBackToMain(){
        //toolbar.setLogo(R.mipmap.logo);
        toolbar.setTitle("HustleDb");
    }
    @Override
    public void onNominationClicked() {
        if (nominationFragment == null) {
            nominationFragment = NominationFragment.newInstance();
        }
        showFragment(nominationFragment, NOMINATION_FRAGMENT_TAG, true);
    }

    @Override
    public void onRefresh() {
        if (contestsListFragment.isVisible()) {
            updateCompetitions();
        } else {
            updatePreregistration(null);
        }
    }
}
