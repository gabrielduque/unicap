package com.thm.unicap.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.activeandroid.query.Select;
import com.crashlytics.android.Crashlytics;
import com.halfbit.tinybus.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.thm.unicap.app.about.AboutFragment;
import com.thm.unicap.app.auth.AccountGeneral;
import com.thm.unicap.app.calendar.CalendarFragment;
import com.thm.unicap.app.connection.UnicapDataManager;
import com.thm.unicap.app.dashboard.DashboardFragment;
import com.thm.unicap.app.feedback.FeedbackFragment;
import com.thm.unicap.app.grade.GradesFragment;
import com.thm.unicap.app.lessons.LessonsFragment;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.subject.SubjectsFragment;
import com.thm.unicap.app.sync.UnicapSyncEvent;

import java.io.IOException;
import java.util.List;

import hotchemi.android.rate.AppRate;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.me.lewisdeane.ldialogs.CustomDialog;


public class MainActivity extends MaterialNavigationDrawer implements MaterialAccountListener {

    private AccountManager accountManager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initTaskDescriptionConfig() {
        int color = getResources().getColor(R.color.unicap_base_dark);
        setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
    }

    @Override
    public void onResume() {
        super.onResume();

        UnicapApplication.bus.register(this);

        if (!UnicapApplication.hasStudentData()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!UnicapApplication.hasCurrentAccount()) {
                        initAccountCreateIfNeeded();
                    }
                }
            }, 500);
        } else {
            updateNavigationDrawerInfo();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        UnicapApplication.bus.unregister(this);
    }

    public void initAccountCreateIfNeeded() {

        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        if(accounts.length == 0) {
            accountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {

                    try {
                        future.getResult(); // Used to catch exceptions
                        restartActivity();
                    } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                        finish();
                    }

                }
            }, null);
        } else {

            Account selectedAccount = accounts[0];

            String lastAccount = getLastAccount();

            if(lastAccount != null) {
                UnicapApplication.log(lastAccount);
                for (Account account : accounts) {
                    if(account.name.equals(lastAccount)) {
                        selectedAccount = account;
                    }
                }
            }

            selectAccount(selectedAccount);

        }

    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void selectAccount(Account account) {
        try {
            UnicapApplication.setCurrentAccount(account);

            Student student = new Select().from(Student.class).where("Student.Registration = ?", UnicapApplication.getCurrentAccount().name).executeSingle();

            if (student != null) {
                UnicapApplication.setCurrentStudent(student);
                UnicapApplication.bus.post(new UnicapSyncEvent(UnicapSyncEvent.EventType.SYNC_COMPLETED));

            } else if (!UnicapApplication.isSyncing()) {
                UnicapApplication.forceSync();
            }

            saveLastAccount(account.name);

        } catch (Exception e) {
            if (!UnicapApplication.hasStudentData()) finish();
        }
    }

    private void saveLastAccount(String registration) {
        getPreferences(Context.MODE_PRIVATE).edit().putString("last_account", registration).commit();
    }

    private String getLastAccount() {
        return getPreferences(Context.MODE_PRIVATE).getString("last_account", null);
    }

    @Subscribe
    public void receiveSyncEvent(UnicapSyncEvent event) {
        switch (event.getEventType()) {
            case SYNC_COMPLETED:
                databaseUpdated();
                break;
        }
    }

    public void databaseUpdated() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateNavigationDrawerInfo();
            }
        });
    }

    private void updateNavigationDrawerInfo() {

        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        for (Account account : accounts) {

            Student student = new Select().from(Student.class).where("Registration = ?", account.name).executeSingle();
            final MaterialAccount materialAccount = getUIAccountByRegistration(account.name);

            if (student != null) {
                materialAccount.setTitle(student.name);
                materialAccount.setSubTitle(student.registration);

                Picasso.with(this)
                        .load(student.getGravatarURL(100))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                materialAccount.setPhoto(bitmap.copy(bitmap.getConfig(), true));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }

        }

    }

    private MaterialAccount getUIAccountByRegistration(String registration) {
        List<MaterialAccount> uiAccountList = getAccountList();

        for (MaterialAccount materialAccount : uiAccountList) {
            if(materialAccount.getSubTitle().equals(registration)) {
                return materialAccount;
            }
        }
        return null;
    }

    private void logout() {

        final Student currentStudent = UnicapApplication.getCurrentStudent();

        if (currentStudent == null) {
            restartActivity();
        } else {
            Account account = new Account(currentStudent.registration, AccountGeneral.ACCOUNT_TYPE);

            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    UnicapDataManager.cleanUserData(currentStudent.registration);
                    UnicapApplication.setCurrentAccount(null);
                    UnicapApplication.setCurrentStudent(null);
                    restartActivity();
                }
            }, null);
        }
    }

    @Override
    public void init(Bundle bundle) {

        accountManager = AccountManager.get(this);

        Crashlytics.start(this);

        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
        this.disableLearningPattern();
        this.getToolbar().setTitleTextColor(getResources().getColor(android.R.color.white));

        this.setAccountListener(this);

        this.addMultiPaneSupport();

        this.addSection(this.newSection(getString(R.string.dashboard), R.drawable.ic_dashboard_gray600_24dp, new DashboardFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));
        this.addSection(this.newSection(getString(R.string.subjects), R.drawable.ic_library_books_grey600_24dp, new SubjectsFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));
        this.addSection(this.newSection(getString(R.string.calendar), R.drawable.ic_event_grey600_24dp, new CalendarFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));
        this.addSection(this.newSection(getString(R.string.lessons), R.drawable.ic_black_board_grey600_24dp, new LessonsFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));
        this.addSection(this.newSection(getString(R.string.grades), R.drawable.ic_award_grey600_24dp, new GradesFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));

        this.addBottomSection(this.newSection(getString(R.string.feedback), R.drawable.ic_email_grey600_24dp, new FeedbackFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));
        this.addBottomSection(this.newSection(getString(R.string.about), R.drawable.ic_info_outline_grey600_24dp, new AboutFragment())
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));
        this.addBottomSection(this.newSection(getString(R.string.logout), R.drawable.ic_exit_to_app_grey600_24dp, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                logout();
            }
        }).setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));


        this.addAccountSection(this.newSection(getString(R.string.add_account), R.drawable.ic_person_add_grey600_24dp, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {

                AccountManager accountManager = AccountManager.get(MainActivity.this);

                accountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, MainActivity.this, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {

                        try {
                            Bundle bundle = future.getResult();// Used to catch exceptions
                            String registration = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                            setAccountForNextLaunch(registration);
                            restartActivity();
                        } catch (OperationCanceledException | IOException | AuthenticatorException ignored) {
                        }

                    }
                }, null);
            }
        }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            initTaskDescriptionConfig();

        initUIAccounts();

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

    private void setAccountForNextLaunch(String registration) {
        UnicapApplication.setCurrentAccount(null);
        UnicapApplication.setCurrentStudent(null);
        saveLastAccount(registration);
    }

    private void initUIAccounts() {

        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        String lastAccount = getLastAccount();
        boolean hasLastAccount = lastAccount != null && accountExists(lastAccount);

        // Adding current account
        if(hasLastAccount) {
            MaterialAccount materialAccount = new MaterialAccount(getResources(), lastAccount, lastAccount, R.drawable.ic_graduate_lightgrey_24dp, R.drawable.material_base);
            this.addAccount(materialAccount);
        }

        for (Account account : accounts) {

            if(hasLastAccount && account.name.equals(lastAccount)) {
                continue;
            }

            MaterialAccount materialAccount = new MaterialAccount(getResources(), account.name, account.name, R.drawable.ic_graduate_lightgrey_24dp, R.drawable.material_base);
            this.addAccount(materialAccount);
        }

        updateNavigationDrawerInfo();

    }

    private boolean accountExists(String registration) {
        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        for (Account account : accounts) {
            if(account.name.equals(registration)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public void onAccountOpening(MaterialAccount materialAccount) {
        CustomDialog customDialog = new CustomDialog.Builder(this, getString(R.string.profile_picture), getString(R.string.learn_more))
                .content(getString(R.string.gravatar_text))
                .negativeText(getString(R.string.not_now))
                .positiveColor(getResources().getColor(android.R.color.holo_blue_light))
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

    @Override
    public void onChangeAccount(MaterialAccount materialAccount) {

        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        String registration = materialAccount.getSubTitle();

        for (Account account : accounts) {
            if(account.name.equals(registration)) {
                selectAccount(account);
            }
        }


    }
}

