package com.oddsix.nutripro.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.FoodInfoAdapter;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.FoodFromMealResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;

/**
 * Created by filippecl on 06/11/16.
 */

public class FoodInfoActivity extends BaseActivity {
    private FoodModel mFoodModel;
    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_with_toolbar);

        setToolbar(true, getString(R.string.food_info_activity_title));

        mProvider = new NutriproProvider(this);

        mFeedbackHelper = new FeedbackHelper(this, (ViewGroup) findViewById(R.id.container), mTryAgainClickListener);

        mFoodModel = (FoodModel) getIntent().getSerializableExtra(Constants.EXTRA_FOOD_MODEL);

//        sendRequest();
        setListView();
    }

    private View.OnClickListener mTryAgainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            sendRequest();
        }
    };

    private void sendRequest() {
        mFeedbackHelper.startLoading();
//        mProvider.getFoodById(mFoodModel.getId(), new NutriproProvider.OnResponseListener<FoodFromMealResponse>() {
//            @Override
//            public void onResponseSuccess(FoodFromMealResponse response) {
//                mFoodResponse = response;
//                mFeedbackHelper.dismissFeedback();
//                setListView();
//            }
//
//            @Override
//            public void onResponseFailure(String msg, int code) {
//                mFeedbackHelper.showErrorPlaceHolder();
//            }
//        });
    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.listview);
        View headerView = getLayoutInflater().inflate(R.layout.header_food_info, null);
        listView.setDividerHeight(0);
        listView.setClickable(false);
        setHeader(headerView);
        listView.addHeaderView(headerView);
        FoodInfoAdapter adapter = new FoodInfoAdapter(mFoodModel.getNutrients(), this);
        listView.setAdapter(adapter);
    }

    private void setHeader(View headerView) {
        TextView quantityTv = (TextView) headerView.findViewById(R.id.food_info_quantity_tv);
        quantityTv.setText(getString(R.string.food_info_quantity, mFoodModel.getQuantity(), "g"));
        TextView nameTv = (TextView) headerView.findViewById(R.id.food_info_name_tv);
        nameTv.setText(mFoodModel.getFoodName());
    }
}
