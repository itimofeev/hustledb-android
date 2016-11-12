package ru.hustledb.hustledb;

import android.content.Context;
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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import ru.hustledb.hustledb.DataProviders.Retrofit.InternetCompetitionsProvider;
import ru.hustledb.hustledb.DataProviders.LocalDb.LocalCompetitionsProvider;
import ru.hustledb.hustledb.Events.OnLoadCompleteEvent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompetitionsListFragment.CompetitionsListListener {

    @Inject
    Bus bus;
    @Inject
    CompetitionsCache competitionsCache;
    @Inject
    InternetCompetitionsProvider internetCompetitionsProvider;
    @Inject
    LocalCompetitionsProvider localCompetitionsProvider;

    private static final String COMPETITIONS_LIST_FRAGMENT_TAG = "competitionsListFragment";
    private static final String COMPETITIONS_DETAILS_FRAGMENT_TAG = "competitionDetailFragment";
    private CompetitionsListFragment competitionsListFragment;
    private CompetitionDetailsFragment competitionDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        localCompetitionsProvider.getCompetitionsObservable().subscribe(competitionsCache);
        updateCompetitions();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(competitionsListFragment == null){
            competitionsListFragment = CompetitionsListFragment.newInstance();
        }
        showFragment(competitionsListFragment, COMPETITIONS_LIST_FRAGMENT_TAG, false);
    }
    private void showFragment(Fragment fragment, String tag, boolean addToBackstack) {
        if (!fragment.isVisible()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.a_mCoordinatorLayout, fragment, tag);
            if(addToBackstack){
                transaction.addToBackStack(null);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCompetitionClicked(Competition competition) {
        if(competitionDetailsFragment == null){
            competitionDetailsFragment = CompetitionDetailsFragment.newInstance(competition);
        } else {
            competitionDetailsFragment.setCompetition(competition);
        }
        showFragment(competitionDetailsFragment, COMPETITIONS_DETAILS_FRAGMENT_TAG, true);
    }
    private boolean hasConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public void updateCompetitions(){
        if(hasConnection()) {
            internetCompetitionsProvider.getCompetitionsObservable().subscribe(localCompetitionsProvider);
        } else {
            Toast.makeText(this, "Нет подключения к Интернет!", Toast.LENGTH_SHORT).show();
            bus.post(new OnLoadCompleteEvent());
        }
    }

    @Override
    public void onRefresh() {
        updateCompetitions();
    }
}
