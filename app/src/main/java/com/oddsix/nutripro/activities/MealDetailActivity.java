package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;
import com.oddsix.nutripro.models.MealModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.DayResumeResponse;
import com.oddsix.nutripro.rest.models.responses.MealDetailResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;

/**
 * Created by filippecl on 06/11/16.
 */

public class MealDetailActivity extends BaseActivity {
    private AnalysedImgAdapter mAnalysedImgAdapter;
    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;
    private MealDetailResponse mMeal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_with_toolbar);

        final DayResumeResponse.MealResponse meal = (DayResumeResponse.MealResponse) getIntent().getSerializableExtra(Constants.EXTRA_MEAL_MODEL);

        mFeedbackHelper = new FeedbackHelper(this, (LinearLayout) findViewById(R.id.container), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(meal.getId());
            }
        });

        mProvider = new NutriproProvider(this);

        setToolbar(true, getString(R.string.meal_detail_activity_title));

        sendRequest(meal.getId());
    }

    private void sendRequest(String id) {
        mFeedbackHelper.startLoading();
        mProvider.getMealDetail(id, new NutriproProvider.OnResponseListener<MealDetailResponse>() {
            @Override
            public void onResponseSuccess(MealDetailResponse response) {
                mMeal = response;
                mFeedbackHelper.dismissFeedback();
                setListView();
                mAnalysedImgAdapter.setFoods(response.getFoods());
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                mFeedbackHelper.showErrorPlaceHolder();
            }
        });
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
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setDividerHeight(0);
        listView.setAdapter(mAnalysedImgAdapter);
        listView.addHeaderView(inflateHeader(getLayoutInflater()));
        listView.addFooterView(inflateFooter(getLayoutInflater()));
    }

    public void startFoodInfoActivity(int position) {
        Intent infoIntent = new Intent(this, FoodInfoActivity.class);
//        infoIntent.putExtra(Constants.EXTRA_FOOD_MODEL, mMeal.getFoods().get(position));
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

    public void setImage(final View headerView) {
        Glide.with(this).load(mMeal.getPictureUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ((ImageView) headerView.findViewById(R.id.header_analysed_photo_img)).setImageBitmap(resource);
                setDrawing(headerView, resource);
            }
        });
    }


    private void setDrawing(View headerView, Bitmap resource) {
//        ImageView image = ((ImageView) headerView.findViewById(R.id.header_analysed_photo_img));
        ImageView imageView = (ImageView) headerView.findViewById(R.id.header_analysed_drawing);
        Bitmap bitmap = Bitmap.createBitmap(resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wallpaint = new Paint();
        wallpaint.setColor(Color.GRAY);
        wallpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        wallpaint.setAlpha(80);
        Path wallpath = new Path();
        wallpath.reset(); // only needed when reusing this path for a new build
        wallpath.moveTo(0, 0); // used for first point
        wallpath.lineTo(100, 0);
        wallpath.lineTo(0, 100);
        wallpath.lineTo(0, 0); // there is a setLastPoint action but i found it not to work as expected

        canvas.drawPath(wallpath, wallpaint);
        imageView.setImageBitmap(bitmap);
    }
}
