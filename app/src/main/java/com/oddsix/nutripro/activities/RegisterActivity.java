package com.oddsix.nutripro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.RegisterModel;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.validations.HasMinLength;
import com.oddsix.nutripro.utils.validations.IsEmail;
import com.oddsix.nutripro.utils.validations.IsNotEmpty;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Filippe on 16/10/16.
 */

public class RegisterActivity extends BaseActivity {
    public final int SPINNER_HINT_POSITION = 0;

    private TextInputLayout mMailTil, mPassTil, mPassConfirmationTil, mNameTil, mAgeTil, mWeightTil, mHeightTil;
    private List<TextInputLayout> mTilList;
    private Spinner mGenderSp;
    private Button mButton;
    private String[] mGenderArray;
    private Realm mRealm;

    private boolean mIsEditingRegister;
    private RegisterModel mRegisterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mIsEditingRegister = getIntent().getBooleanExtra(Constants.EXTRA_BOOL_EDIT_REGISTER, false);

        setRegisterToolbar();
        findViews();
        setViews();
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

    private void setRegisterToolbar(){
        if(mIsEditingRegister) {
            setToolbar(true, getString(R.string.edit_register_title));
        } else {
            setToolbar(true);
        }
    }

    private void setViews() {
        setButton();
        setGenderSpinner();
        setTextInputLayouts();
        if(mIsEditingRegister) {
            mRegisterModel = Realm.getDefaultInstance().where(RegisterModel.class)
                    .equalTo("mail", getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_MAIL, ""))
                    .findFirst();
            populateViews();
        }
    }

    private void populateViews() {
        mMailTil.setVisibility(View.GONE);
        mPassTil.setVisibility(View.GONE);
        mPassConfirmationTil.setVisibility(View.GONE);
        mNameTil.getEditText().setText(mRegisterModel.getName());
        mAgeTil.getEditText().setText(String.valueOf(mRegisterModel.getAge()));
        mWeightTil.getEditText().setText(String.valueOf(mRegisterModel.getWeight()));
        mHeightTil.getEditText().setText(String.valueOf(mRegisterModel.getHeight()));
        for (int i = 0; i < mGenderArray.length; i++) {
            if(mGenderArray[i].equals(mRegisterModel.getGender())) {
                mGenderSp.setSelection(i);
            }
        }
    }

    private void setTextInputLayouts() {
        createTextInputLayoutArray();
        for (final TextInputLayout til : mTilList) {
            setOnFocusChangeListener(til);
        }
    }

    private void setOnFocusChangeListener(final TextInputLayout til) {
        til.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    til.setErrorEnabled(false);
                }
            }
        });
    }

    private void createTextInputLayoutArray() {
        mTilList = new ArrayList<>();
        if(!mIsEditingRegister) {
            mTilList.add(mMailTil);
            mTilList.add(mPassTil);
            mTilList.add(mPassConfirmationTil);
        }
        mTilList.add(mNameTil);
        mTilList.add(mAgeTil);
        mTilList.add(mWeightTil);
        mTilList.add(mHeightTil);
    }

    private void setButton() {
        if(mIsEditingRegister) {
            mButton.setText(getString(R.string.edit_register_btn_save));
        } else {
            mButton.setText(getString(R.string.register_btn_send));
        }

        mButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           onSendClicked(view);
                                       }
                                   }
        );
    }

    public void onSendClicked(View view) {
        validateFields();
        if (areAllFieldsValid()) {
            if(mIsEditingRegister) {
                mRealm.beginTransaction();
                mRegisterModel.setName(mNameTil.getEditText().getText().toString());
                mRegisterModel.setAge(Integer.valueOf(mAgeTil.getEditText().getText().toString()));
                mRegisterModel.setGender(mGenderArray[mGenderSp.getSelectedItemPosition()]);
                mRegisterModel.setHeight(Float.valueOf(mHeightTil.getEditText().getText().toString().replaceAll(",", ".")));
                mRegisterModel.setWeight(Float.valueOf(mWeightTil.getEditText().getText().toString().replaceAll(",", ".")));
                mRealm.commitTransaction();
                finishWithResult();
            } else {
                showProgressdialog();
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(new RegisterModel(mMailTil.getEditText().getText().toString(), mPassTil.getEditText().getText().toString(), mNameTil.getEditText().getText().toString(),
                        Integer.valueOf(mAgeTil.getEditText().getText().toString()), mGenderArray[mGenderSp.getSelectedItemPosition()], Float.valueOf(mHeightTil.getEditText().getText().toString().replaceAll(",", ".")),
                        Float.valueOf(mWeightTil.getEditText().getText().toString().replaceAll(",", "."))));
                mRealm.commitTransaction();
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
                sharedPreferences.edit().putString(Constants.PREF_MAIL, mMailTil.getEditText().getText().toString()).apply();
                dismissProgressDialog();
                sharedPreferences.edit().putBoolean(Constants.PREF_IS_LOGGED, true).apply();
                startSuggestedDietActivity();
            }
        }
    }

    private void startSuggestedDietActivity(){
        Intent intent = new Intent(this, SuggestedDietActivity.class);
        startActivity(intent);
        finish();
    }

    private void finishWithResult() {
        setResult(RESULT_OK);
        finish();
    }

    private void validateFields() {
        for (TextInputLayout til : mTilList) {
            if (!IsNotEmpty.isValid(til.getEditText().getText().toString())) {
                til.setError(getString(R.string.register_error_empty_field));
            } else {
                til.setErrorEnabled(false);
            }
        }

        //validate fields that needs specific validation
        if (!IsEmail.isValid(mMailTil.getEditText().getText().toString())) {
            mMailTil.setError(getString(R.string.register_error_invalid_mail));
        } else {
            mMailTil.setErrorEnabled(false);
        }

        if (!HasMinLength.isValid(mPassTil.getEditText().getText().toString(), getResources().getInteger(R.integer.password_max_lengt))) {
            mPassTil.setError(getString(R.string.register_error_password_length));
        } else {
            mPassTil.setErrorEnabled(false);
        }

        if (!mPassConfirmationTil.getEditText().getText().toString().equals(mPassTil.getEditText().getText().toString())) {
            mPassConfirmationTil.setError(getString(R.string.register_error_not_matching));
        } else {
            mPassConfirmationTil.setErrorEnabled(false);
        }

        if (mGenderSp.getSelectedItemPosition() == 0) {
            showToast(getString(R.string.register_error_gender_not_selected));
        }

        try {
            Float.valueOf(mWeightTil.getEditText().getText().toString().replaceAll(",", "."));
        } catch (NumberFormatException e) {
            mWeightTil.setError(getString(R.string.register_error_invalid_weight));
        }

        try {
            Float.valueOf(mHeightTil.getEditText().getText().toString().replaceAll(",", "."));
        } catch (NumberFormatException e) {
            mHeightTil.setError(getString(R.string.register_error_invalid_height));
        }
    }

    private boolean areAllFieldsValid() {
        boolean hasInvalidField = false;
        for (TextInputLayout til : mTilList) {
            if (til.isErrorEnabled()) {
                hasInvalidField = true;
            }
        }

        if (mGenderSp.getSelectedItemPosition() == SPINNER_HINT_POSITION) {
            hasInvalidField = true;
        }

        return !hasInvalidField;
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
        mGenderSp.setAdapter(spinnerAdapter);
    }
}
