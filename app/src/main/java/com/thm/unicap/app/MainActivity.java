package com.thm.unicap.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.activeandroid.query.Select;
import com.crashlytics.android.Crashlytics;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.thm.unicap.app.about.AboutFragment;
import com.thm.unicap.app.auth.AccountGeneral;
import com.thm.unicap.app.calendar.CalendarFragment;
import com.thm.unicap.app.connection.UnicapDataManager;
import com.thm.unicap.app.dashboard.DashboardFragment;
import com.thm.unicap.app.feedback.FeedbackFragment;
import com.thm.unicap.app.grade.GradesFragment;
import com.thm.unicap.app.lessons.LessonsFragment;
import com.thm.unicap.app.menu.NavigationDrawerFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.subject.SubjectsFragment;
import com.thm.unicap.app.util.DatabaseListener;

import hotchemi.android.rate.AppRate;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DatabaseListener {

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
    private SuperActivityToast mSyncingToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.unicap_base_dark);
            setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
        }

        mAccountManager = AccountManager.get(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppRate.with(MainActivity.this)
                        .setInstallDays(10) // default 10, 0 means install day.
                        .setLaunchTimes(10) // default 10
                        .setRemindInterval(2) // default 1
                        .setShowNeutralButton(true) // default true
                        .monitor();

                // Show a dialog if meets conditions
                AppRate.showRateDialogIfMeetsConditions(MainActivity.this);
            }
        }, 1000);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(!UnicapApplication.hasStudentData()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!UnicapApplication.hasCurrentAccount()) {
                        selectAccountCreateIfNeeded();
                    }
                }
            }, 500);
        }

        UnicapApplication.addDatabaseListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        UnicapApplication.removeDatabaseListener(this);
        mHandler.removeCallbacks(mSwitchFragmentRunnable);
        if(mSyncingToast != null) mSyncingToast.dismiss();
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
            case NavigationDrawerFragment.SESSION_LOGOUT:
                logout();
                return;
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
                        .commitAllowingStateLoss();
            }
        };

        mHandler.postDelayed(mSwitchFragmentRunnable, 350);

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

    public void selectAccountCreateIfNeeded() {
        mAccountManager.getAuthTokenByFeatures(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, this, null, null,
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
                                if(!UnicapApplication.isSyncing())
                                    UnicapApplication.forceSync();
                                else
                                    databaseSyncing();
                            }

                        } catch (Exception e) {
                            if(!UnicapApplication.hasStudentData()) finish();
                        }
                    }
                }
                , null);
    }

    @Override
    public void databaseSyncing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mSyncingToast == null) {
                    mSyncingToast = new SuperActivityToast(MainActivity.this, SuperToast.Type.PROGRESS);
                    mSyncingToast.setText(getString(R.string.synchronizing));
                    mSyncingToast.setIndeterminate(true);
                }

                if(!mSyncingToast.isShowing())
                    mSyncingToast.show();
            }
        });
    }

    @Override
    public void databaseUpdated() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSyncingToast != null) {
                    mSyncingToast.dismiss();
                }
            }
        });
    }

    @Override
    public void databaseUnreachable(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSyncingToast != null) {
                    mSyncingToast.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            int currentSelectedPosition = mNavigationDrawerFragment.getCurrentSelectedPosition();

            if(currentSelectedPosition == NavigationDrawerFragment.SESSION_DASHBOARD)
                super.onBackPressed();
            else
                mNavigationDrawerFragment.selectItem(NavigationDrawerFragment.SESSION_DASHBOARD);
        }
    }

    private void logout() {
        AccountManager accountManager = AccountManager.get(this);

        final Student currentStudent = UnicapApplication.getCurrentStudent();

        if(currentStudent == null) {
            finish();
        } else {
            Account account = new Account(currentStudent.registration, AccountGeneral.ACCOUNT_TYPE);

            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    UnicapDataManager.cleanUserData(currentStudent.registration);
                    UnicapApplication.setCurrentAccount(null);
                    UnicapApplication.setCurrentStudent(null);
                    finish();
                }
            }, null);
        }
    }
}
