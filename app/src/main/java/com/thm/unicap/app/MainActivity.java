package com.thm.unicap.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

import com.activeandroid.query.Select;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.thm.unicap.app.about.AboutFragment;
import com.thm.unicap.app.auth.AccountGeneral;
import com.thm.unicap.app.calendar.CalendarFragment;
import com.thm.unicap.app.dashboard.DashboardFragment;
import com.thm.unicap.app.feedback.FeedbackFragment;
import com.thm.unicap.app.grade.GradesFragment;
import com.thm.unicap.app.lessons.LessonsFragment;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.subject.SubjectsFragment;
import com.thm.unicap.app.sync.UnicapContentProvider;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private AccountManager mAccountManager;

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
    private SuperActivityToast mSuperActivityToast;
    private Object mStatusChangedHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountManager = AccountManager.get(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if(!UnicapApplication.hasStudentData())
            getStudentFromAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
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

    private void getStudentFromAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();

                            Account currentAccount = new Account(bundle.getString(AccountManager.KEY_ACCOUNT_NAME), AccountGeneral.ACCOUNT_TYPE);
                            UnicapApplication.setCurrentAccount(currentAccount);

                            Student student = new Select().from(Student.class).where("Student.Registration = ?", UnicapApplication.getCurrentAccount().name).executeSingle();

                            if(student != null) {
                                UnicapApplication.setCurrentStudent(student);
                                UnicapApplication.notifyDatabaseUpdated();
                            } else {
                                forceSync(UnicapApplication.getCurrentAccount().name);
                            }

                        } catch (Exception e) {
                            //TODO: Friendly message
                            Log.d("UNICAP", String.valueOf(e));
                            finish();
                        }
                    }
                }
                , null);
    }

    public void forceSync(String registration) {

//        mSuperActivityToast = new SuperActivityToast(this, SuperToast.Type.PROGRESS);
//        mSuperActivityToast.setText(getString(R.string.synchronizing));
//        mSuperActivityToast.setIndeterminate(true);
//        mSuperActivityToast.show();

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

//        mStatusChangedHandle = ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE, this);
        ContentResolver.requestSync(UnicapApplication.getCurrentAccount(), UnicapContentProvider.AUTHORITY, settingsBundle);

    }

//    @Override
//    public void onStatusChanged(int which) {
//        //TODO: Implement this using broadcast receivers and move to application
//        //TODO: This is being miss called
//
//        if(!ContentResolver.isSyncActive(UnicapApplication.getCurrentAccount(), UnicapContentProvider.AUTHORITY)) {
//            ContentResolver.removeStatusChangeListener(mStatusChangedHandle);
//
//            Student student = new Select().from(Student.class).where("Student.Registration = ?", UnicapApplication.getCurrentAccount().name).executeSingle();
//
//            if(student != null) {
//                UnicapApplication.setCurrentStudent(student);
//                UnicapApplication.notifyDatabaseUpdated();
//            } else {
//                //TODO: Friendly message
////                finish();
//            }
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mSuperActivityToast != null)
//                        mSuperActivityToast.dismiss();
//                }
//            });
//        }
//    }
}
