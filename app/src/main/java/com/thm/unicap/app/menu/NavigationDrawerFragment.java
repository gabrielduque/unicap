package com.thm.unicap.app.menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.auth.AccountGeneral;
import com.thm.unicap.app.connection.UnicapDataManager;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.util.DatabaseDependentFragment;
import com.thm.unicap.app.util.DatabaseListener;

import java.util.ArrayList;
import java.util.List;

import uk.me.lewisdeane.ldialogs.CustomDialog;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements DatabaseListener, DatabaseDependentFragment {

    public static final int SESSION_DASHBOARD = 0;
    public static final int SESSION_SUBJECTS = 1;
    public static final int SESSION_CALENDAR = 2;
    public static final int SESSION_LESSONS = 3;
    public static final int SESSION_GRADES = 4;

    public static final int SESSION_FEEDBACK = 5;
    public static final int SESSION_ABOUT = 6;

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = SESSION_DASHBOARD;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        if(UnicapApplication.hasStudentData()) { // Show offline data
            databaseUpdated();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        initDatabaseIndependentViews();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        UnicapApplication.addDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UnicapApplication.removeDatabaseListener(this);
    }

    @Override
    public void initDatabaseDependentViews() {
        ViewGroup navHeaderContainer = (ViewGroup) mRootView.findViewById(R.id.nav_header_container);
        ImageView navHeaderPicture = (ImageView) mRootView.findViewById(R.id.nav_header_picture);
        TextView navHeaderCourse = (TextView) mRootView.findViewById(R.id.nav_header_course);
        TextView navHeaderName = (TextView) mRootView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = (TextView) mRootView.findViewById(R.id.nav_header_email);

        Picasso.with(getActivity())
                .load(UnicapApplication.getCurrentStudent().getGravatarURL(100))
                .placeholder(R.drawable.ic_graduate)
                .into(navHeaderPicture);
        navHeaderCourse.setText(UnicapApplication.getCurrentStudent().course);
        navHeaderName.setText(UnicapApplication.getCurrentStudent().name);
        navHeaderEmail.setText(UnicapApplication.getCurrentStudent().email);

        ObjectAnimator.ofFloat(navHeaderContainer, "translationY", 0).setDuration(300).start();
        ObjectAnimator.ofFloat(mDrawerListView, "translationY", 0).setDuration(300).start();

        navHeaderContainer.setClickable(true);
        navHeaderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog customDialog = new CustomDialog.Builder(getActivity(), getString(R.string.profile_picture), getString(R.string.learn_more))
                .content(getString(R.string.gravatar_text))
                .negativeText(getString(R.string.not_now))
                .positiveColor(getResources().getColor(R.color.blue))
                .build();

                customDialog.setClickListener(new CustomDialog.ClickListener() {
                    @Override
                    public void onConfirmClick() {
                        String url = getString(R.string.gravatar_website);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

                customDialog.show();
            }
        });
    }

    private void initDatabaseIndependentViews() {
        mDrawerListView = (ListView) mRootView.findViewById(R.id.navigation_listview);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        List<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

        //Main items
        navigationItems.add(new NavigationItem(getString(R.string.dashboard), R.drawable.ic_dashboard));
        navigationItems.add(new NavigationItem(getString(R.string.subjects), R.drawable.ic_subjects));
        navigationItems.add(new NavigationItem(getString(R.string.calendar), R.drawable.ic_calendar));
        navigationItems.add(new NavigationItem(getString(R.string.lessons), R.drawable.ic_lessons));
        navigationItems.add(new NavigationItem(getString(R.string.grades), R.drawable.ic_grades));

        //Extra items
        navigationItems.add(new NavigationItem(getString(R.string.feedback), R.drawable.ic_action_feedback, NavigationItem.Type.EXTRA));
        navigationItems.add(new NavigationItem(getString(R.string.about), R.drawable.ic_action_about, NavigationItem.Type.EXTRA));

        mDrawerListView.setAdapter(new NavigationAdapter(getActionBar().getThemedContext(), R.layout.navigation_item_normal, navigationItems));

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_logout:

                AccountManager accountManager = AccountManager.get(getActivity());

                final Student currentStudent = UnicapApplication.getCurrentStudent();

                if(currentStudent == null) {
                    getActivity().finish();
                    return true;
                }

                Account account = new Account(currentStudent.registration, AccountGeneral.ACCOUNT_TYPE);

                accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                    @Override
                    public void run(AccountManagerFuture<Boolean> future) {
                        UnicapDataManager.cleanUserData(currentStudent.registration);
                        UnicapApplication.setCurrentAccount(null);
                        UnicapApplication.setCurrentStudent(null);
                        getActivity().finish();
                    }
                }, null);

                return true;
            case R.id.action_sync:
                UnicapApplication.forceSync();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void databaseSyncing() {

    }

    @Override
    public void databaseUpdated() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initDatabaseDependentViews();
            }
        });
    }

    @Override
    public void databaseUnreachable(String message) {

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public int getCurrentSelectedPosition() {
        return mCurrentSelectedPosition;
    }
}
