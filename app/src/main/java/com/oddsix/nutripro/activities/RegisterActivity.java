package com.oddsix.nutripro.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.RegisterModel;

import io.realm.Realm;

/**
 * Created by Filippe on 16/10/16.
 */

public class RegisterActivity extends BaseActivity {
    public final int SPINNER_HINT_POSITION = 0;

    private boolean mIsValidSpinnerOption = false;

    private TextInputLayout mMailTil, mPassTil, mPassConfirmationTil, mNameTil, mAgeTil, mWeightTil, mHeightTil;
    private Spinner mGenderSp;
    private String[] mGenderArray;
    private Realm mRealm;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        setGenderSpinner();
        setButton();
        mRealm = Realm.getDefaultInstance();
    }

    private void findViews() {
        mMailTil = (TextInputLayout) findViewById(R.id.register_mail_til);
        mPassTil = (TextInputLayout) findViewById(R.id.register_password_til);
        mPassConfirmationTil = (TextInputLayout) findViewById(R.id.register_password_confirmation_til);
        mNameTil = (TextInputLayout) findViewById(R.id.register_name_til);
        mAgeTil = (TextInputLayout) findViewById(R.id.register_age_til);
        mWeightTil = (TextInputLayout) findViewById(R.id.register_weight_til);
        mHeightTil = (TextInputLayout) findViewById(R.id.register_height_til);
        mGenderSp = (Spinner) findViewById(R.id.register_gender_sp);
        mButton = (Button) findViewById(R.id.register_send_btn);
    }

    public void setButton() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
                showProgressdialog();
                RegisterModel registerModel = new RegisterModel(mMailTil.getEditText().getText().toString(),
                        mPassTil.getEditText().getText().toString(), mNameTil.getEditText().getText().toString(),
                        Integer.valueOf(mAgeTil.getEditText().getText().toString()), mGenderArray[mGenderSp.getSelectedItemPosition()], Integer.valueOf(mHeightTil.getEditText().getText().toString()),
                        Float.valueOf(mWeightTil.getEditText().getText().toString()));
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(registerModel);
                mRealm.commitTransaction();
                dismissProgressDialog();
                //TODO OPEN DIETA ACTIVITY
            }
        });

    }

    private void validateFields() {
        validateMail();
    }

    private void validateMail() {
        if (mMailTil.getEditText().getText().toString().isEmpty()) {
            mMailTil.setErrorEnabled(true);
        }
    }

    private void setGenderSpinner() {
        mGenderArray = getResources().getStringArray(R.array.info_gender_options);

        //Set adapter to first item be used as hint
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(this, R.layout.partial_spinner, mGenderArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == SPINNER_HINT_POSITION) {
                    // Disable the first item from Spinner
                    // First item will be used as hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == SPINNER_HINT_POSITION) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > SPINNER_HINT_POSITION) {
                    mIsValidSpinnerOption = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mGenderSp.setAdapter(spinnerAdapter);
    }
}
