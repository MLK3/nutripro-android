package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBDietNutrientModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.requests.DietNutrientRequest;
import com.oddsix.nutripro.rest.models.requests.NutrientRequest;
import com.oddsix.nutripro.rest.models.responses.DietNutrientResponse;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by filippecl on 21/12/16.
 */

public class EditDietActivity extends BaseActivity {

    private LinearLayout mLayout;

    private List<DBDietNutrientModel> mNutrients = new ArrayList<>();

    private List<EditText> mMaxValue = new ArrayList<>();
    private List<EditText> mMinValue = new ArrayList<>();

    private NutriproProvider mProvider;
    private Realm mRealm;

    private DBDietModel mSuggestedDiet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diet);
        setToolbar(true, getString(R.string.edit_diet_activity_title));
        mRealm = Realm.getDefaultInstance();

        mLayout = (LinearLayout) findViewById(R.id.container);

        mSuggestedDiet = mRealm.where(DBDietModel.class)
                .equalTo("email", SharedPreferencesHelper.getInstance().getUserEmail()).findFirst();
        mNutrients = mSuggestedDiet.getDiet();

        mProvider = new NutriproProvider(this);

        setNutrients();

        setButton();
    }

    private void setNutrients() {
        for (DBDietNutrientModel nutrient : mNutrients) {
            View view = getLayoutInflater().inflate(R.layout.item_edit_diet, null, false);

            TextView maxUnit = (TextView) view.findViewById(R.id.item_diet_max_unit);
            maxUnit.setText(String.valueOf(nutrient.getUnit()));
            TextView minUnit = (TextView) view.findViewById(R.id.item_diet_min_unit);
            minUnit.setText(String.valueOf(nutrient.getUnit()));
            TextView name = (TextView) view.findViewById(R.id.item_diet_name);
            name.setText(nutrient.getName());

            EditText max = (EditText) view.findViewById(R.id.item_diet_min_value);
            max.setText(String.valueOf(nutrient.getMax()));
            mMaxValue.add(max);

            EditText min = (EditText) view.findViewById(R.id.item_diet_max_value);
            min.setText(String.valueOf(nutrient.getMin()));
            mMinValue.add(min);

            mLayout.addView(view);
        }
    }

    private void setButton() {
        findViewById(R.id.edit_diet_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<DietNutrientRequest> nutrientsToUpdate = new ArrayList<DietNutrientRequest>();
                for (int i = 0; i < mNutrients.size(); i++) {
                    nutrientsToUpdate.add(new DietNutrientRequest(mNutrients.get(i).getName(),
                            Integer.valueOf(mMaxValue.get(i).getText().toString()),
                            Integer.valueOf(mMinValue.get(i).getText().toString())));
                }
                showProgressdialog();
                mProvider.updateDiet(nutrientsToUpdate, new NutriproProvider.OnResponseListener<GeneralResponse>() {
                    @Override
                    public void onResponseSuccess(GeneralResponse response) {
                        dismissProgressDialog();
                        showToast(getString(R.string.edit_diet_activity_toast_success));
                        mRealm.beginTransaction();
                        for (int i = 0; i < mNutrients.size(); i++) {
                            mSuggestedDiet.getDiet().get(i).setName(nutrientsToUpdate.get(i).getName());
                            mSuggestedDiet.getDiet().get(i).setMax(nutrientsToUpdate.get(i).getMax());
                            mSuggestedDiet.getDiet().get(i).setMin(nutrientsToUpdate.get(i).getMin());
                            mSuggestedDiet.getDiet().get(i).setUnit(mNutrients.get(i).getUnit());
                        }
                        mRealm.commitTransaction();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onResponseFailure(String msg, int code) {
                        dismissProgressDialog();
                        showToast(msg);
                    }
                });
            }
        });
    }
}
