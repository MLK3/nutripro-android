package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.RegisterResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;
import com.oddsix.nutripro.utils.validations.HasMinLength;
import com.oddsix.nutripro.utils.validations.IsEmail;
import com.oddsix.nutripro.utils.validations.IsNotEmpty;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


/**
 * Created by Filippe on 16/10/16.
 */

public class RegisterActivity extends BaseActivity {
    public final int SPINNER_HINT_POSITION = 0;

    private TextInputLayout mMailTil, mPassTil, mPassConfirmationTil, mNameTil, mAgeTil, mWeightTil, mHeightTil;
    private List<TextInputLayout> mTilList;
    private Spinner mGenderSp;
    private RadioGroup mRadioGroup;
    private Button mButton;
    private String[] mGenderArray;
    private Realm mRealm;

    private NutriproProvider mProvider;

    private boolean mIsEditingRegister;
    private RegisterResponse mRegisterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRealm = Realm.getDefaultInstance();

        mProvider = new NutriproProvider(this);

        mIsEditingRegister = getIntent().getBooleanExtra(Constants.EXTRA_BOOL_EDIT_REGISTER, false);

        setRegisterToolbar();
        findViews();
        setViews();
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
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_activity_level);
    }

    private void setRegisterToolbar() {
        if (mIsEditingRegister) {
            setToolbar(true, getString(R.string.edit_register_title));
        } else {
            setToolbar(true);
        }
    }

    private void setViews() {
        setButton();
        setGenderSpinner();
        setTextInputLayouts();
        if (mIsEditingRegister) {
            DBRegisterModel registerDbModel = mRealm.where(DBRegisterModel.class)
                    .equalTo("email", SharedPreferencesHelper.getInstance().getUserEmail()).findFirst();
            if(registerDbModel != null) {
                mRegisterModel = new RegisterResponse(registerDbModel);
                populateViews();
            }

        }
    }

    private void populateViews() {
        mMailTil.setVisibility(View.GONE);
        mPassTil.setVisibility(View.GONE);
        mPassConfirmationTil.setVisibility(View.GONE);
        mNameTil.getEditText().setText(mRegisterModel.getName());
        mAgeTil.getEditText().setText(String.valueOf(mRegisterModel.getAge()));
        mWeightTil.getEditText().setText(String.valueOf(mRegisterModel.getPeso()));
        mHeightTil.getEditText().setText(String.valueOf(mRegisterModel.getAltura()));
        for (int i = 0; i < mGenderArray.length; i++) {
            if (mGenderArray[i].equals(mRegisterModel.getGender())) {
                mGenderSp.setSelection(i);
            }
        }

        String activity = mRegisterModel.getActivity();

        int id = -1;
        if (activity.equalsIgnoreCase(getString(R.string.activity_level_sedentary))) {
            id = R.id.register_rb_sedentary;
        } else if (activity.equalsIgnoreCase(getString(R.string.activity_level_low))) {
            id = R.id.register_rb_low;
        } else if (activity.equalsIgnoreCase(getString(R.string.activity_level_medium))) {
            id = R.id.register_rb_medium;
        } else if (activity.equalsIgnoreCase(getString(R.string.activitY_level_high))) {
            id = R.id.register_rb_high;
        }
        mRadioGroup.check(id);

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
        if (!mIsEditingRegister) {
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
        if (mIsEditingRegister) {
            mButton.setText(getString(R.string.edit_register_btn_save));
        } else {
            mButton.setText(getString(R.string.register_btn_continue));
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
            final String activityLevel;
            switch (mRadioGroup.getCheckedRadioButtonId()) {
                case R.id.register_rb_sedentary:
                    activityLevel = getString(R.string.activity_level_sedentary);
                    break;
                case R.id.register_rb_low:
                    activityLevel = getString(R.string.activity_level_low);
                    break;
                case R.id.register_rb_medium:
                    activityLevel = getString(R.string.activity_level_medium);
                    break;
                case R.id.register_rb_high:
                    activityLevel = getString(R.string.activitY_level_high);
                    break;
                default:
                    activityLevel = "";
            }
            if (mIsEditingRegister) {
                showProgressdialog();
                mProvider.updateRegister(mNameTil.getEditText().getText().toString(),
                        mGenderArray[mGenderSp.getSelectedItemPosition()],
                        Integer.valueOf(mAgeTil.getEditText().getText().toString()),
                        mMailTil.getEditText().getText().toString(),
                        activityLevel,
                        Integer.valueOf(mWeightTil.getEditText().getText().toString()),
                        Integer.valueOf(mHeightTil.getEditText().getText().toString()),
                        new NutriproProvider.OnResponseListener<GeneralResponse>() {
                            @Override
                            public void onResponseSuccess(GeneralResponse response) {
                                updateRegisterModel(activityLevel);
                                mRealm.beginTransaction();
                                mRealm.copyToRealmOrUpdate(new DBRegisterModel(mMailTil.getEditText().getText().toString(),
                                        mNameTil.getEditText().getText().toString(),
                                        mGenderArray[mGenderSp.getSelectedItemPosition()],
                                        Integer.valueOf(mAgeTil.getEditText().getText().toString()),
                                        activityLevel,
                                        Integer.valueOf(mWeightTil.getEditText().getText().toString()),
                                        Integer.valueOf(mHeightTil.getEditText().getText().toString())

                                ));
                                mRealm.commitTransaction();
                                SharedPreferencesHelper.getInstance().putUserEmail(mMailTil.getEditText().getText().toString());
                                finishWithResult();
                                dismissProgressDialog();
                            }

                            @Override
                            public void onResponseFailure(String msg, int code) {
                                dismissProgressDialog();
                                showToast(msg);
                            }
                        }
                );
            } else {
                showProgressdialog();
                mProvider.createRegister(mNameTil.getEditText().getText().toString(),
                        mGenderArray[mGenderSp.getSelectedItemPosition()],
                        Integer.valueOf(mAgeTil.getEditText().getText().toString()),
                        mMailTil.getEditText().getText().toString(),
                        activityLevel,
                        Integer.valueOf(mHeightTil.getEditText().getText().toString()),
                        Integer.valueOf(mWeightTil.getEditText().getText().toString()),
                        new NutriproProvider.OnResponseListener<GeneralResponse>() {
                            @Override
                            public void onResponseSuccess(GeneralResponse response) {
                                try {
                                    mRealm.beginTransaction();
                                    mRealm.copyToRealm(new DBRegisterModel(mMailTil.getEditText().getText().toString(),
                                            mNameTil.getEditText().getText().toString(),
                                            mGenderArray[mGenderSp.getSelectedItemPosition()],
                                            Integer.valueOf(mAgeTil.getEditText().getText().toString()),
                                            activityLevel,
                                            Integer.valueOf(mWeightTil.getEditText().getText().toString()),
                                            Integer.valueOf(mHeightTil.getEditText().getText().toString())

                                    ));
                                    mRealm.commitTransaction();
                                    dismissProgressDialog();
                                    startSuggestedDietActivity();
                                } catch (RealmPrimaryKeyConstraintException e) {
                                    dismissProgressDialog();
                                    showToast(getString(R.string.register_error_already_registred));
                                }
                            }

                            @Override
                            public void onResponseFailure(String msg, int code) {
                                dismissProgressDialog();
                                showToast(msg);
                            }
                        }
                );
            }
        }
    }

    private void updateRegisterModel(String activityLevel) {
        mRegisterModel = new RegisterResponse(mNameTil.getEditText().getText().toString(),
                mGenderArray[mGenderSp.getSelectedItemPosition()],
                Integer.valueOf(mAgeTil.getEditText().getText().toString()),
                mMailTil.getEditText().getText().toString(),
                activityLevel,
                Integer.valueOf(mHeightTil.getEditText().getText().toString()),
                Integer.valueOf(mWeightTil.getEditText().getText().toString()));
    }

    private void startSuggestedDietActivity() {
        Intent intent = new Intent(this, SuggestedDietActivity.class);
        startActivity(intent);
        finish();
    }

    private void finishWithResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_REGISTER_MODEL, mRegisterModel);
        setResult(RESULT_OK, intent);
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

        if (!HasMinLength.isValid(mPassTil.getEditText().getText().toString(), getResources().getInteger(R.integer.password_min_length))) {
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
            Integer.valueOf(mWeightTil.getEditText().getText().toString());
        } catch (NumberFormatException e) {
            mWeightTil.setError(getString(R.string.register_error_invalid_weight));
        }

        try {
            Integer.valueOf(mHeightTil.getEditText().getText().toString());
        } catch (NumberFormatException e) {
            mHeightTil.setError(getString(R.string.register_error_invalid_height));
        }

        if (mRadioGroup.getCheckedRadioButtonId() == -1) {
            showToast(getString(R.string.register_level_error_not_selected));
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

        if (mRadioGroup.getCheckedRadioButtonId() == -1) {
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
