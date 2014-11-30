package com.thm.unicap.app.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.devspark.robototextview.widget.RobotoTextView;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.connection.OnTaskCancelled;
import com.thm.unicap.app.connection.OnTaskCompleted;
import com.thm.unicap.app.connection.UnicapDataManager;
import com.thm.unicap.app.connection.UnicapRequestResult;
import com.thm.unicap.app.sync.UnicapContentProvider;
import com.thm.unicap.app.util.UnicapUtils;

/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends AccountAuthenticatorActivity implements OnTaskCompleted, OnTaskCancelled {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";

    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    private UnicapAuthTask mAuthTask = null;

    // UI references.
    private EditText mRegistrationView;
    private EditText mPasswordView;
    private CircularProgressButton mRegistrationSignInButton;
    private boolean isLoginInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.unicap_base_dark);
            setTaskDescription(new ActivityManager.TaskDescription(null, null, color));
        }

        mAccountManager = AccountManager.get(getBaseContext());

        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        // Set up the login form.
        mRegistrationView = (EditText) findViewById(R.id.registration);

        //Allow only digits or hyphen
        InputFilter filter = new InputFilter() {
            //TODO: Fix some bugs
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if(dest.length() >= 11) return "";

                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) return "";

                    if (dstart == 9) return "-"+source.charAt(i);
                }

                return null;
            }
        };

        mRegistrationView.setFilters(new InputFilter[]{filter});

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mRegistrationSignInButton = (CircularProgressButton) findViewById(R.id.registration_sign_in_button);
        mRegistrationSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (isLoginInProgress) return;

        isLoginInProgress = true;

        // Reset errors.
        mRegistrationView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String registration = mRegistrationView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!UnicapUtils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid registration.
        if (TextUtils.isEmpty(registration)) {
            mRegistrationView.setError(getString(R.string.error_field_required));
            focusView = mRegistrationView;
            cancel = true;
        } else if (!UnicapUtils.isRegistrationValid(registration)) {
            mRegistrationView.setError(getString(R.string.error_invalid_registration));
            focusView = mRegistrationView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            isLoginInProgress = false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mRegistrationSignInButton.setIndeterminateProgressMode(true);
            mRegistrationSignInButton.setProgress(1);

            mAuthTask = new UnicapAuthTask(new StudentCredentials(registration, password));
            mAuthTask.setOnTaskCompletedListener(this);
            mAuthTask.setOnTaskCancelledListener(this);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void onTaskCompleted(UnicapRequestResult result) {

        final StudentCredentials credentials = mAuthTask.getCredentials();

        if (result.isSuccess()) {

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mRegistrationSignInButton.animate().setDuration(shortAnimTime).alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    mRegistrationSignInButton.setVisibility(View.GONE);

                    Bundle data = new Bundle();
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, credentials.getRegistration());
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);
                    data.putString(AccountManager.KEY_AUTHTOKEN, credentials.getAuthToken());
                    data.putString(PARAM_USER_PASS, credentials.getPassword());

                    Intent intent = new Intent();
                    intent.putExtras(data);
                    finishLogin(intent);
                }
            });

        } else {

            onTaskCancelled();

            // Clean up to prevent broken data
            UnicapDataManager.cleanUserData(credentials.getRegistration());

            SuperToast superToast = new SuperToast(LoginActivity.this, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN));
            superToast.setText(result.getExceptionMessage(LoginActivity.this));
            superToast.setDuration(SuperToast.Duration.EXTRA_LONG);
            superToast.setIcon(R.drawable.ic_action_warning, SuperToast.IconPosition.LEFT);
            superToast.show();
        }

        isLoginInProgress = false;
    }

    @Override
    public void onTaskCancelled() {

        // Circular Progress Button has a bug that cannot handle [progress -> idle] directly
        // Workaround: [progress -> error -> idle]
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRegistrationSignInButton.setProgress(-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRegistrationSignInButton.setProgress(0);
                        isLoginInProgress = false;
                    }
                }, 1000);
            }
        }, 1000);
    }

    private void finishLogin(Intent intent) {

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            if(mAccountManager.addAccountExplicitly(account, accountPassword, null)) {
                mAccountManager.setAuthToken(account, authtokenType, authtoken);

                ContentResolver.setSyncAutomatically(account, UnicapContentProvider.AUTHORITY, true);
            }
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}



