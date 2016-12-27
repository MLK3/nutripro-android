package com.oddsix.nutripro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;

import io.realm.Realm;

/**
 * Created by Filippe on 22/10/16.
 */

public class SuggestedDietActivity extends BaseActivity {
    private DietAdapter mAdapter;
    private NutriproProvider mProvider;
    private LinearLayout mFeedbackContainer;
    private SuggestedDietResponse mSuggestedDietResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        mProvider = new NutriproProvider(this);

        mFeedbackContainer = (LinearLayout) findViewById(R.id.feedback_container);

        setListView();

        setBtn();

        getSuggestedDiet();
    }


    private void getSuggestedDiet() {
        showProgressdialog();
        mProvider.getDiet(new NutriproProvider.OnResponseListener<SuggestedDietResponse>() {
            @Override
            public void onResponseSuccess(SuggestedDietResponse response) {
                mSuggestedDietResponse = response;
                dismissProgressDialog();
                mFeedbackContainer.setVisibility(View.GONE);
                setSuggestedDiet(response);
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                dismissProgressDialog();
                mFeedbackContainer.setVisibility(View.VISIBLE);
                showToast(msg);
            }
        });
    }

    private void setListView() {
        ListView list = (ListView) findViewById(R.id.listview);
        mAdapter = new DietAdapter(this);
        list.setAdapter(mAdapter);
        list.addHeaderView(setHeader());
        list.addFooterView(setFooter());
    }

    private void setSuggestedDiet(SuggestedDietResponse response) {
        mAdapter.setDiet(response.getNutrients());
    }

    private View setHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.header_suggested_diet, null);
        return headerView;
    }

    private void setBtn() {
        findViewById(R.id.feedback_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSuggestedDiet();
            }
        });
    }

    private View setFooter() {
        View footerView = getLayoutInflater().inflate(R.layout.footer_suggested_diet, null);
        footerView.findViewById(R.id.footer_suggested_diet_conclude).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(Constants.PREF_IS_LOGGED, true).apply();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra(Constants.EXTRA_DIET, mSuggestedDietResponse);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        footerView.findViewById(R.id.footer_suggested_diet_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editDietIntent = new Intent(view.getContext(), EditDietActivity.class);
                editDietIntent.putExtra(Constants.EXTRA_DIET, mSuggestedDietResponse);
                startActivityForResult(editDietIntent, Constants.REQ_EDIT_DIET);
            }
        });
        return footerView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Constants.REQ_EDIT_DIET) {
            mSuggestedDietResponse = (SuggestedDietResponse) data.getSerializableExtra(Constants.EXTRA_DIET);
            setSuggestedDiet(mSuggestedDietResponse);
        }
    }
}
