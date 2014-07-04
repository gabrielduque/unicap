package com.thm.unicap.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;

import com.thm.unicap.app.about.AboutFragment;
import com.thm.unicap.app.calendar.CalendarFragment;
import com.thm.unicap.app.dashboard.DashboardFragment;
import com.thm.unicap.app.auth.LoginActivity;
import com.thm.unicap.app.grade.GradesActivity;
import com.thm.unicap.app.grade.GradesFragment;
import com.thm.unicap.app.grade.SubjectGradesListItemCard;
import com.thm.unicap.app.lessons.LessonsFragment;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.feedback.FeedbackFragment;
import com.thm.unicap.app.model.SubjectTest;
import com.thm.unicap.app.subject.SubjectsFragment;

import java.util.List;


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
        } else {
//            notifyNewGrades();
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
            case NavigationDrawerFragment.SESSION_CALENDAR:
                fragment = new CalendarFragment();
                break;
            case NavigationDrawerFragment.SESSION_LESSONS:
                fragment = new LessonsFragment();
                break;
            case NavigationDrawerFragment.SESSION_GRADES:
                fragment = new GradesFragment();
                break;
            case NavigationDrawerFragment.SESSION_FEEDBACK:
                fragment = new FeedbackFragment();
                break;
            case NavigationDrawerFragment.SESSION_ABOUT:
                fragment = new AboutFragment();
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
            case NavigationDrawerFragment.SESSION_SETTINGS:
                mTitle = getString(R.string.settings);
                break;
            case NavigationDrawerFragment.SESSION_FEEDBACK:
                mTitle = getString(R.string.feedback);
                break;
            case NavigationDrawerFragment.SESSION_ABOUT:
                mTitle = getString(R.string.about);
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
            getMenuInflater().inflate(R.menu.activity_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

//    private void notifyNewGrades() {
//        List<SubjectTest> newSubjectTests = UnicapApplication.getStudent().getNewSubjectTests();
//
//        for (SubjectTest subjectTest : newSubjectTests) {
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.ic_stat_unicap)
//                    .setContentTitle(subjectTest.subject.name)
//                    .setAutoCancel(true);
//
//            switch (subjectTest.degree) {
//                case FIRST_DEGREE:
//                    builder.setContentText("Primeiro GQ disponível");
//                    break;
//                case SECOND_DEGREE:
//                    builder.setContentText("Segundo GQ disponível");
//                    break;
//                case FINAL_DEGREE:
//                    builder.setContentText("Avaliação final disponível");
//                    break;
//            }
//
//// Creates an explicit intent for an Activity in your app
//            Intent resultIntent = new Intent(this, GradesActivity.class);
//            resultIntent.putExtra("subject_id", subjectTest.subject.getId());
//
//// The stack builder object will contain an artificial back stack for the
//// started Activity.
//// This ensures that navigating backward from the Activity leads out of
//// your application to the Home screen.
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//// Adds the back stack for the Intent (but not the Intent itself)
//            stackBuilder.addParentStack(MainActivity.class);
//// Adds the Intent that starts the Activity to the top of the stack
//            stackBuilder.addNextIntent(resultIntent);
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(
//                            0,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                    );
//            builder.setContentIntent(resultPendingIntent);
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// mId allows you to update the notification later on.
//
//            int mId = 78135;
//            mNotificationManager.notify(mId, builder.build());
////                subjectTest.notify = false;
////                subjectTest.save();
//        }
//    }
}
