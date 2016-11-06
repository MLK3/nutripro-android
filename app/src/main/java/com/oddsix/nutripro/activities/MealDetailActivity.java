package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;
import com.oddsix.nutripro.models.MealModel;
import com.oddsix.nutripro.utils.Constants;

/**
 * Created by filippecl on 06/11/16.
 */

public class MealDetailActivity extends BaseActivity {
    private MealModel mMeal;
    private AnalysedImgAdapter mAnalysedImgAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_with_toolbar);
        mMeal = (MealModel) getIntent().getSerializableExtra(Constants.EXTRA_MEAL_MODEL);
        setToolbar(true, getString(R.string.meal_detail_activity_title));

        setListView();
    }

    private void setListView() {
        mAnalysedImgAdapter = new AnalysedImgAdapter(this, new AnalysedImgAdapter.OnNutrientClickListener() {
            @Override
            public void onEditValueClicked(int position) {
                functionNotImplemented();
            }

            @Override
            public void onEditNameClicked(int position) {
                functionNotImplemented();
            }

            @Override
            public void onEditInfoClicked(int position) {
                startFoodInfoActivity(position);
            }
        });
        mAnalysedImgAdapter.setFoods(mMeal.getFoods());
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setDividerHeight(0);
        listView.setAdapter(mAnalysedImgAdapter);
        listView.addHeaderView(inflateHeader(getLayoutInflater()));
        listView.addFooterView(inflateFooter(getLayoutInflater()));
    }

    public void startFoodInfoActivity(int position){
        Intent infoIntent = new Intent(this, FoodInfoActivity.class);
        infoIntent.putExtra(Constants.EXTRA_FOOD_MODEL, mMeal.getFoods().get(position));
        startActivity(infoIntent);
    }

    private View inflateHeader(LayoutInflater inflater) {
        View headerView = inflater.inflate(R.layout.header_analysed_photo, null);
        ((TextView) headerView.findViewById(R.id.header_analysed_meal_name_tv)).setText(mMeal.getName());
        setImage(headerView);
        return headerView;
    }

    private View inflateFooter(LayoutInflater inflater) {
        View footerView = inflater.inflate(R.layout.footer_analysed_photo, null);
        ((Button) footerView.findViewById(R.id.footer_analysed_photo_conclude)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionNotImplemented();
            }
        });
        ((Button) footerView.findViewById(R.id.footer_analysed_photo_conclude)).setText(getString(R.string.meal_detail_btn_save));
        return footerView;
    }

    public void setImage(View headerView) {
        ((ImageView) headerView.findViewById(R.id.header_analysed_photo_img)).setImageResource(R.drawable.ic_logo_nutripro);
    }
}
