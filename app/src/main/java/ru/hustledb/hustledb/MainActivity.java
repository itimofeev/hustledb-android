package ru.hustledb.hustledb;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalCompetitionsProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetCompetitionsProvider;
import ru.hustledb.hustledb.DataProviders.Retrofit.InternetPreregistrationProvider;
import ru.hustledb.hustledb.Events.OnCompetitionsLoadCompleteEvent;
import ru.hustledb.hustledb.Events.OnPreregistrationLoadCompleteEvent;
import ru.hustledb.hustledb.ValueClasses.Competition;
import rx.subscriptions.CompositeSubscription;

import static ru.hustledb.hustledb.R.id.toolbar;

public class MainActivity extends AppCompatActivity
        implements CompetitionsListFragment.CompetitionsListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    RxBus bus;
    @Inject
    CompetitionsCache competitionsCache;
    @Inject
    PreregistrationCache preregistrationCache;
    @Inject
    InternetCompetitionsProvider internetCompetitionsProvider;
    @Inject
    LocalCompetitionsProvider localCompetitionsProvider;
    @Inject
    InternetPreregistrationProvider internetPreregistrationProvider;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription subscriptions;

    private static final String COMPETITIONS_LIST_FRAGMENT_TAG = "competitionsListFragment";
    private static final String COMPETITIONS_DETAILS_FRAGMENT_TAG = "competitionDetailFragment";
//    private static final String PREREGISTRATION_FRAGMENT_TAG = "preregistrationFragment";
    private static final String NOMINATION_FRAGMENT_TAG = "nominationFragment";
    private CompetitionsListFragment competitionsListFragment;
    private CompetitionDetailsFragment competitionDetailsFragment;
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
        localCompetitionsProvider.getCompetitionsObservable().subscribe(competitionsCache);
        updateCompetitions();
        setSupportActionBar(toolbar);

        if (competitionsListFragment == null) {
            competitionsListFragment = CompetitionsListFragment.newInstance();
        }
        showFragment(competitionsListFragment, COMPETITIONS_LIST_FRAGMENT_TAG, false);
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
            internetCompetitionsProvider.getCompetitionsObservable().subscribe(localCompetitionsProvider);
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
    public void onCompetitionClicked(Competition competition) {
        updatePreregistration(competition.getId());
        if (competitionDetailsFragment == null) {
            competitionDetailsFragment = CompetitionDetailsFragment.newInstance(competition);
        } else {
            competitionDetailsFragment.setCompetition(competition);
        }
        toolbar.setLogo(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        toolbar.setTitle(competition.getTitle());
        showFragment(competitionDetailsFragment, COMPETITIONS_DETAILS_FRAGMENT_TAG, true);
    }

    @Override
    public void onPreregistrationInfoClicked(int f_competition_id) {
        updatePreregistration(f_competition_id);
    }

//    public void onPreregistrationDownloaded(OnPreregistrationLoadCompleteEvent event) {
//        if (competitionDetailsFragment.isVisible()) {
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
        if (competitionsListFragment.isVisible()) {
            updateCompetitions();
        } else {
            updatePreregistration(null);
        }
    }
}
