package com.oddsix.nutripro.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.FoodInfoAdapter;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.utils.Constants;

/**
 * Created by filippecl on 06/11/16.
 */

public class FoodInfoActivity extends BaseActivity {
    private FoodModel mMealFoodModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_with_toolbar);
        setToolbar(true, getString(R.string.food_info_activity_title));
        mMealFoodModel = (FoodModel) getIntent().getSerializableExtra(Constants.EXTRA_FOOD_MODEL);
        setListView();
    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.listview);
        View headerView = getLayoutInflater().inflate(R.layout.header_food_info, null);
        listView.setDividerHeight(0);
        setHeader(headerView);
        listView.addHeaderView(headerView);
        FoodInfoAdapter adapter = new FoodInfoAdapter(mMealFoodModel.getNutrients(), this);
        listView.setAdapter(adapter);
    }

    private void setHeader(View headerView) {
        TextView quantityTv = (TextView) headerView.findViewById(R.id.food_info_quantity_tv);
        quantityTv.setText(getString(R.string.food_info_quantity, mMealFoodModel.getQuantity(), "g"));
        TextView nameTv = (TextView) headerView.findViewById(R.id.food_info_name_tv);
        nameTv.setText(mMealFoodModel.getFoodName());
    }
}
