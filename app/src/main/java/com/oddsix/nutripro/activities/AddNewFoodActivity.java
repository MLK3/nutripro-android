package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.requests.NutrientRequest;
import com.oddsix.nutripro.rest.models.requests.RegisterFoodRequest;
import com.oddsix.nutripro.rest.models.responses.CreateFoodResponse;
import com.oddsix.nutripro.rest.models.responses.NutrientResponse;
import com.oddsix.nutripro.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class AddNewFoodActivity extends BaseActivity {

    private TextInputLayout mNameTil, mCarbTil, mEnergeticValueTil,
            mProteinTil, mTotalFatTil, mSaturatedFatTil,
            mFibersTil, mSodiumTil, mPortionTil;
    private Button mSendBtn;
    private List<TextInputLayout> mTilList;
    private NutriproProvider mProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        setToolbar(true, getString(R.string.register_food_activity_title));

        findViews();

        mProvider = new NutriproProvider(this);

        createTextInputLayoutArray();

        setBtnClick();

    }

    private void setBtnClick() {
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegister();
            }
        });
    }

    private void sendRegister() {
        if (isFieldsValid()) {
            createRegister();
        }
    }

    private void createRegister() {
        final List<NutrientRequest> nutrients = new ArrayList<>();
        for (TextInputLayout til : mTilList) {
            if (til.getId() != R.id.register_food_name_til && til.getId() != R.id.register_food_quantity_til) {
                nutrients.add(new NutrientRequest(til.getHint().toString(), Integer.valueOf(til.getEditText().getText().toString())));
            }
        }
        showProgressdialog();
        mProvider.registerFood(new RegisterFoodRequest(mNameTil.getEditText().getText().toString(), nutrients),
                new NutriproProvider.OnResponseListener<CreateFoodResponse>() {
                    @Override
                    public void onResponseSuccess(CreateFoodResponse response) {
                        dismissProgressDialog();
                        Intent intent = new Intent();
                        List<NutrientModel> nutrientModels = new ArrayList<NutrientModel>();
                        for (NutrientResponse nutrient : response.getNutrients()) {
                            // TODO: 16/03/17 add numbers from til and not from response
                            for (NutrientRequest nutrientRequest : nutrients) {
                                if (nutrientRequest.getName().toLowerCase().toLowerCase().contains(nutrient.getName())) {
                                    nutrientModels.add(new NutrientModel(nutrient.getName(), nutrientRequest.getQuantity(), nutrient.getUnit()));
                                    break;
                                }
                            }
                        }

                        intent.putExtra(Constants.EXTRA_FOOD, new FoodModel(nutrientModels, mNameTil.getEditText().getText().toString(), Integer.valueOf(mPortionTil.getEditText().getText().toString()), response.getPorcao_em_g()));
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onResponseFailure(String msg, int code) {
                        dismissProgressDialog();
                        showToast(msg);
                    }
                });
    }

    private void createTextInputLayoutArray() {
        mTilList = new ArrayList<>();
        mTilList.add(mNameTil);
        mTilList.add(mCarbTil);
        mTilList.add(mEnergeticValueTil);
        mTilList.add(mProteinTil);
        mTilList.add(mTotalFatTil);
        mTilList.add(mSaturatedFatTil);
        mTilList.add(mFibersTil);
        mTilList.add(mSodiumTil);
        mTilList.add(mPortionTil);
    }

    private boolean isFieldsValid() {
        boolean isValid = true;
        for (TextInputLayout til : mTilList) {
            if (til.getEditText().getText().toString().isEmpty()) {
                til.setError(getString(R.string.register_food_error));
                isValid = false;
            }
        }
        return isValid;
    }


    private void findViews() {
        mNameTil = (TextInputLayout) findViewById(R.id.register_food_name_til);
        mEnergeticValueTil = (TextInputLayout) findViewById(R.id.register_food_energetic_value_til);
        mCarbTil = (TextInputLayout) findViewById(R.id.register_food_carbs_til);
        mProteinTil = (TextInputLayout) findViewById(R.id.register_food_protein_til);
        mTotalFatTil = (TextInputLayout) findViewById(R.id.register_food_total_fat_til);
        mSaturatedFatTil = (TextInputLayout) findViewById(R.id.register_food_saturated_fat_til);
        mFibersTil = (TextInputLayout) findViewById(R.id.register_food_fibers_til);
        mSodiumTil = (TextInputLayout) findViewById(R.id.register_food_sodium_til);
        mPortionTil = (TextInputLayout) findViewById(R.id.register_food_quantity_til);

        mSendBtn = (Button) findViewById(R.id.register_food_send_btn);
    }

}
