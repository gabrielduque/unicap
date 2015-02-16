package com.thm.unicap.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
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

import hotchemi.android.rate.AppRate;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.me.lewisdeane.ldialogs.CustomDialog;


public class MainActivity extends MaterialNavigationDrawer implements MaterialAccountListener {

    private AccountManager mAccountManager;

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
                        selectAccountCreateIfNeeded();
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

                            if (student != null) {
                                UnicapApplication.setCurrentStudent(student);
                                UnicapApplication.bus.post(new UnicapSyncEvent(UnicapSyncEvent.EventType.SYNC_COMPLETED));

                            } else if (!UnicapApplication.isSyncing()) {
                                UnicapApplication.forceSync();
                            }

                        } catch (Exception e) {
                            if (!UnicapApplication.hasStudentData()) finish();
                        }
                    }
                }
                , null);
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
        Student student = UnicapApplication.getCurrentStudent();

        getCurrentAccount().setTitle(student.name);
        getCurrentAccount().setSubTitle(student.course);
        notifyAccountDataChanged();

        Picasso.with(this)
                .load(student.getGravatarURL(100))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        getCurrentAccount().setPhoto(bitmap.copy(bitmap.getConfig(), true));
                        notifyAccountDataChanged();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private void logout() {
        AccountManager accountManager = AccountManager.get(this);

        final Student currentStudent = UnicapApplication.getCurrentStudent();

        if (currentStudent == null) {
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

    @Override
    public void init(Bundle bundle) {

        Crashlytics.start(this);

        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
        this.disableLearningPattern();
        this.getToolbar().setTitleTextColor(getResources().getColor(android.R.color.white));

        this.setAccountListener(this);

//        this.addMultiPaneSupport();

        MaterialAccount account = new MaterialAccount(getResources(), "", "", R.drawable.ic_graduate_white_80dp, R.drawable.material_base);
        this.addAccount(account);

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
        this.addBottomSection(this.newSection(getString(R.string.logout), R.drawable.ic_exit_to_app_grey600_24dp, this)
                .setSectionColor(getResources().getColor(R.color.unicap_base), getResources().getColor(R.color.unicap_base_dark)));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            initTaskDescriptionConfig();

        mAccountManager = AccountManager.get(this);

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
    public void onClick(MaterialSection section) {
        switch (section.getPosition()) {
            case 102: //Logout
                logout();
            default:
                super.onClick(section);
        }
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
    }
}

