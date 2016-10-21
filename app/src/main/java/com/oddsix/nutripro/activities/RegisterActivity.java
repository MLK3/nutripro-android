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
import com.oddsix.nutripro.utils.validations.HasMinLength;
import com.oddsix.nutripro.utils.validations.IsEmail;
import com.oddsix.nutripro.utils.validations.IsNotEmpty;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.annotations.PrimaryKey;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setToolbar(true);
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

    private void setViews() {
        setButtonClickListener();
        setGenderSpinner();
        setTextInputLayouts();
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
        mTilList.add(mMailTil);
        mTilList.add(mPassTil);
        mTilList.add(mPassConfirmationTil);
        mTilList.add(mNameTil);
        mTilList.add(mAgeTil);
        mTilList.add(mWeightTil);
        mTilList.add(mHeightTil);
    }

    private void setButtonClickListener() {
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
            showProgressdialog();
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(new RegisterModel(mMailTil.getEditText().getText().toString(), mPassTil.getEditText().getText().toString(), mNameTil.getEditText().getText().toString(),
                    Integer.valueOf(mAgeTil.getEditText().getText().toString()), mGenderArray[mGenderSp.getSelectedItemPosition()], Integer.valueOf(mHeightTil.getEditText().getText().toString()),
                    Float.valueOf(mWeightTil.getEditText().getText().toString())));
            mRealm.commitTransaction();
            dismissProgressDialog();
            finish();
        }
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
            showToast("É necessário escolher um sexo.");
        }
    }

    private boolean areAllFieldsValid() {
        boolean hasInvalidField = false;
        for (TextInputLayout til : mTilList) {
            if (til.isErrorEnabled()) {
                hasInvalidField = true;
            }
        }

        if (mGenderSp.getSelectedItemPosition() == 0) {
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
