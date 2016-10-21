package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.RegisterModel;
import com.oddsix.nutripro.utils.Constants;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Filippe on 16/10/16.
 */

public class LoginActivity extends BaseActivity {
    private TextInputLayout mLoginTil, mPassTil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setBtn();
    }

    public void findViews() {
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

    public void onEnterClicked(View view) {
        RealmResults<RegisterModel> registers = Realm.getDefaultInstance().where(RegisterModel.class)
                .equalTo("mail", mLoginTil.getEditText().getText().toString())
                .findAll();

        if (registers.isEmpty()) {
            showToast(getString(R.string.login_error_user_not_found));
        } else {
            if (mPassTil.getEditText().getText().toString().equals(registers.get(0).getPassword())) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showToast(getString(R.string.login_error_user_unauthorized));
            }
        }
    }

    public void onRegisterClicked(View view) {
        Intent registerIntent = new Intent(view.getContext(), RegisterActivity.class);
        startActivityForResult(registerIntent, Constants.REQ_REGISTER);
    }
}
