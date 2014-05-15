package com.thm.unicap.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import com.activeandroid.query.Select;
import com.thm.unicap.app.R;
import com.thm.unicap.app.UnicapApplication;
import com.thm.unicap.app.connection.UnicapConnector;
import com.thm.unicap.app.model.Student;
import com.thm.unicap.app.util.UnicapUtils;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mRegistrationView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mRegistrationView = (EditText) findViewById(R.id.registration);

        //Allow only digits or hyphen
        InputFilter filter = new InputFilter() {
            //TODO: Fix some bugs
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if(dest.length() >= 11) return "";

//                Log.d(UnicapApplication.TAG, "----------");
//                Log.d(UnicapApplication.TAG, "source: "+source.toString());
//                Log.d(UnicapApplication.TAG, "start: "+String.valueOf(start));
//                Log.d(UnicapApplication.TAG, "end: "+String.valueOf(end));
//                Log.d(UnicapApplication.TAG, "dest: "+dest.toString());
//                Log.d(UnicapApplication.TAG, "dstart: "+String.valueOf(dstart));
//                Log.d(UnicapApplication.TAG, "dend: "+String.valueOf(dend));

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

        Button mRegistrationSignInButton = (Button) findViewById(R.id.registration_sign_in_button);
        mRegistrationSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

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
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(registration, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mRegistration;
        private final String mPassword;

        UserLoginTask(String registration, String password) {
            mRegistration = registration;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                UnicapConnector.prepareLoginRequest();
                UnicapConnector.loginRequest(mRegistration, mPassword);

                UnicapConnector.cleanDatabase();

                UnicapConnector.receivePersonalData();
                UnicapConnector.receiveSubjectsPastData();
                UnicapConnector.receiveSubjectsActualData();
                UnicapConnector.receiveSubjectsPendingData();
                UnicapConnector.receiveSubjectsCalendarData();
                UnicapConnector.receiveSubjectsTestsData();

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showProgress(false);
                //TODO: Show specific error instead of "incorrect password"
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



