package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.validations.IsEmail;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Filippe on 16/10/16.
 */

public class LoginActivity extends BaseActivity {
    private TextInputLayout mLoginTil, mPassTil;
    private NutriproProvider mNutriproProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        mNutriproProvider = new NutriproProvider(this);
        setBtn();
    }

    private void findViews() {
        mLoginTil = (TextInputLayout) findViewById(R.id.login_mail_til);
        mPassTil = (TextInputLayout) findViewById(R.id.login_password_til);
    }

    public void setBtn() {
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEnterClicked(view);
            }
        });
    }

    private void validateAllFields() {
        if (!IsEmail.isValid(mLoginTil.getEditText().getText().toString())) {
            mLoginTil.setError(getString(R.string.login_error_invalid_mail));
        } else {
            mLoginTil.setErrorEnabled(false);
        }

        int passMinLength = getResources().getInteger(R.integer.password_min_length);
        if (!(mPassTil.getEditText().getText().toString().length() >= passMinLength)) {
            mPassTil.setError(getString(R.string.login_error_password_length_error, passMinLength));
        } else {
            mPassTil.setErrorEnabled(false);
        }

    }

    private boolean isFieldsAllFieldsValid() {
        validateAllFields();
        return !mLoginTil.isErrorEnabled() && !mPassTil.isErrorEnabled();
    }

    public void onEnterClicked(final View view) {
        if (isFieldsAllFieldsValid()) {
            showProgressdialog();
            mNutriproProvider.signIn(mLoginTil.getEditText().getText().toString(),
                    mPassTil.getEditText().getText().toString(),
                    new NutriproProvider.OnResponseListener<GeneralResponse>() {
                        @Override
                        public void onResponseSuccess(GeneralResponse response) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
                            sharedPreferences.edit().putBoolean(Constants.PREF_IS_LOGGED, true).apply();
                            getSuggestedDiet(view);
                        }

                        @Override
                        public void onResponseFailure(String msg, int code) {
                            showToast(msg);
                            dismissProgressDialog();
                        }
                    });
        }
    }

    public void getSuggestedDiet(final View view) {
        mNutriproProvider.getDiet(new NutriproProvider.OnResponseListener<SuggestedDietResponse>() {
            @Override
            public void onResponseSuccess(SuggestedDietResponse response) {
                dismissProgressDialog();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(Constants.EXTRA_DIET, response);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
    }

    public void onRegisterClicked(View view) {
        Intent registerIntent = new Intent(view.getContext(), RegisterActivity.class);
        startActivityForResult(registerIntent, Constants.REQ_REGISTER);
    }
}
