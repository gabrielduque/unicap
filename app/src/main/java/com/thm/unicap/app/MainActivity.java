package com.thm.unicap.app;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.thm.unicap.app.dashboard.DashboardFragment;
import com.thm.unicap.app.auth.LoginActivity;
import com.thm.unicap.app.grade.GradesFragment;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.subject.SubjectsFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Handler mHandler = new Handler();
    private Runnable mSwitchFragmentRunnable;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!UnicapApplication.isLogged()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment;

        switch (position) {
            case NavigationDrawerFragment.SESSION_DASHBOARD:
                fragment = new DashboardFragment();
                break;
            case NavigationDrawerFragment.SESSION_SUBJECTS:
                fragment = new SubjectsFragment();
                break;
            case NavigationDrawerFragment.SESSION_GRADES:
                fragment = new GradesFragment();
                break;
            default:
                fragment = new DashboardFragment();
                break;

        }

        mHandler.removeCallbacks(mSwitchFragmentRunnable);

        mSwitchFragmentRunnable = new Runnable() {
            public void run() {
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        };

        mHandler.postDelayed(mSwitchFragmentRunnable, 350);

    }

    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mSwitchFragmentRunnable);
        super.onStop();
    }

    public void onSectionAttached(int session) {
        switch (session) {
            case NavigationDrawerFragment.SESSION_DASHBOARD:
                mTitle = getString(R.string.dashboard);
                break;
            case NavigationDrawerFragment.SESSION_SUBJECTS:
                mTitle = getString(R.string.subjects);
                break;
            case NavigationDrawerFragment.SESSION_CALENDAR:
                mTitle = getString(R.string.calendar);
                break;
            case NavigationDrawerFragment.SESSION_LESSONS:
                mTitle = getString(R.string.lessons);
                break;
            case NavigationDrawerFragment.SESSION_GRADES:
                mTitle = getString(R.string.grades);
                break;
        }

        //TODO: supportInvalidateOptionsMenu being called twice (here and on onDrawerClosed)
        //Call needed to change actionbar at startup
        supportInvalidateOptionsMenu();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
