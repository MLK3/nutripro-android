package com.oddsix.nutripro.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;
import com.oddsix.nutripro.models.AreaModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.requests.EditMealRequest;
import com.oddsix.nutripro.rest.models.responses.DayResumeResponse;
import com.oddsix.nutripro.rest.models.responses.EditMealFoodResponse;
import com.oddsix.nutripro.rest.models.responses.FoodResponse;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.RecognisedFoodResponse;
import com.oddsix.nutripro.rest.models.responses.MealDetailResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.base.BaseDialogHelper;
import com.oddsix.nutripro.utils.helpers.AppColorHelper;
import com.oddsix.nutripro.utils.helpers.DialogHelper;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 06/11/16.
 */

public class MealDetailActivity extends BaseActivity {
    private AnalysedImgAdapter mAnalysedImgAdapter;
    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;
    private MealDetailResponse mMeal;
    private AppColorHelper mColorHelper;
    private DialogHelper mDialogHelper;

    private Canvas mCanvas;

    private View mHeaderView;

    private int mEditingFoodIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_with_toolbar);

        final DayResumeResponse.MealResponse meal = (DayResumeResponse.MealResponse) getIntent().getSerializableExtra(Constants.EXTRA_MEAL_MODEL);

        mColorHelper = new AppColorHelper(this);
        mDialogHelper = new DialogHelper(this);

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
                showInputDialog(position);
            }

            @Override
            public void onEditNameClicked(int position) {
                startSearchActivityReplacingFood();
                mEditingFoodIndex = position;
            }

            @Override
            public void onEditInfoClicked(int position) {
                startFoodInfoActivity(position);
            }

            @Override
            public void onEditNameLongClicked(final int position) {
                mDialogHelper.showAlertDialog(getString(R.string.remove_food_dialog_title),
                        getString(R.string.action_remove),
                        getString(R.string.action_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMeal.getFoods().remove(position);
                                mAnalysedImgAdapter.setFoods(mMeal.getFoods());
                                mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                                setImage(mHeaderView);
                            }
                        });
            }
        });
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setDividerHeight(0);
        listView.setAdapter(mAnalysedImgAdapter);
        listView.addHeaderView(inflateHeader(getLayoutInflater()));
        listView.addFooterView(inflateFooter(getLayoutInflater()));
    }

    private void startSearchActivityReplacingFood() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_REPLACE_FOOD);
    }

    private void startSearchActivityAddingFood() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_ADD_FOOD);
    }

    private void startSearchActivityReplacingArea() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_REPLACE_BY_AREA);
    }

    private void showInputDialog(final int position) {
        mDialogHelper.showEditTextDialog(getString(R.string.meal_detail_quantity_dialog_title),
                getString(R.string.action_confirm),
                getString(R.string.action_cancel),
                new BaseDialogHelper.OnEditTextDialogClickListener() {
                    @Override
                    public void onInputConfirmed(int value) {
                        mMeal.getFoods().get(position).setQuantity(value);
                        mAnalysedImgAdapter.setFoods(mMeal.getFoods());
                    }
                });
    }

    private void startFoodInfoActivity(int position) {
        Intent infoIntent = new Intent(this, FoodInfoActivity.class);
        infoIntent.putExtra(Constants.EXTRA_FOOD_MODEL, mMeal.getFoods().get(position));
        startActivity(infoIntent);
    }

    private View inflateHeader(LayoutInflater inflater) {
        View headerView = inflater.inflate(R.layout.header_analysed_photo, null);
        ((TextView) headerView.findViewById(R.id.header_analysed_meal_name_tv)).setText(mMeal.getName());
        mHeaderView = headerView;
        setImage(headerView);
        return headerView;
    }

    private View inflateFooter(LayoutInflater inflater) {
        //Sava changes
        View footerView = inflater.inflate(R.layout.footer_analysed_photo, null);
        footerView.findViewById(R.id.footer_analysed_photo_conclude).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSaveChangesRequest();
            }
        });
        footerView.findViewById(R.id.footer_analysed_photo_add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivityAddingFood();
            }
        });
        return footerView;
    }

    public void sendSaveChangesRequest() {
        EditMealRequest editMealRequest = new EditMealRequest(mMeal.getMeal_id(), mMeal.getName());
        for (RecognisedFoodResponse food : mMeal.getFoods()) {
            editMealRequest.getFoods().add(
                    new EditMealFoodResponse(
                            food.getArea() == null ? null : food.getArea().getArea_id(),
                            food.getId(),
                            food.getQuantity()));
        }
        showProgressdialog();
        mProvider.editMeal(editMealRequest, new NutriproProvider.OnResponseListener<GeneralResponse>() {
            @Override
            public void onResponseSuccess(GeneralResponse response) {
                dismissProgressDialog();
                showToast(getString(R.string.meal_detail_edit_success));
                finish();
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
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
        ImageView imageView = (ImageView) headerView.findViewById(R.id.header_analysed_drawing);

        // Get picture dimensions
        final int resWidth = resource.getWidth();
        final int resHeight = resource.getHeight();

        // Create a canvas for the drawings
        Bitmap bitmap = Bitmap.createBitmap(resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Create a list of regions
        final List<AreaModel> areas = new ArrayList<>();

        for (int i = 0; i < mMeal.getFoods().size(); i++) {
            RecognisedFoodResponse recognisedFood = mMeal.getFoods().get(i);

            //Set color, width and alpha
            Paint wallPaint = new Paint();
            wallPaint.setColor(mColorHelper.getNextColor());
            wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            wallPaint.setStrokeWidth(1);
            wallPaint.setAlpha(80);

            //Draw polygon
            Path wallPath = new Path();
            wallPath.reset();
            List<RecognisedFoodResponse.Area.Point> points = recognisedFood.getArea().getPoints();
            //Initial point
            wallPath.moveTo(points.get(0).getX(), points.get(0).getY());
            for (int j = 1; j < recognisedFood.getArea().getPoints().size(); j++) {
                //Rest of the points
                wallPath.lineTo(points.get(j).getX(), points.get(j).getY());
            }
            wallPath.lineTo(points.get(0).getX(), points.get(0).getY());

            //Draw
            canvas.drawPath(wallPath, wallPaint);

            //Create a region
            RectF rectF = new RectF();
            wallPath.computeBounds(rectF, true);
            final Region r = new Region();
            r.setPath(wallPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

            areas.add(new AreaModel(r, recognisedFood, i));
        }

        mCanvas = canvas;

        imageView.setImageBitmap(bitmap);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Point point = new Point();
                point.x = (int) ((motionEvent.getX() * resWidth) / view.getWidth());
                point.y = (int) ((motionEvent.getY() * resHeight) / view.getHeight());

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    for (AreaModel area : areas) {
                        if (area.getRegion().contains(point.x, point.y)) {
                            mEditingFoodIndex = area.getArrayIndex();
                            startSearchActivityReplacingArea();
                            break;
                        }
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == Constants.REQ_REPLACE_FOOD || requestCode == Constants.REQ_REPLACE_BY_AREA)) {
            FoodResponse foodSelected = (FoodResponse) data.getSerializableExtra(Constants.EXTRA_FOOD);
            mMeal.getFoods().get(mEditingFoodIndex).setId(foodSelected.getId());
            mMeal.getFoods().get(mEditingFoodIndex).setName(foodSelected.getName());
            if (foodSelected.getQuantity() != null) {
                mMeal.getFoods().get(mEditingFoodIndex).setQuantity(foodSelected.getQuantity());
            }
            mAnalysedImgAdapter.setFoods(mMeal.getFoods());
        } else if (resultCode == RESULT_OK && requestCode == Constants.REQ_ADD_FOOD) {
            FoodResponse foodSelected = (FoodResponse) data.getSerializableExtra(Constants.EXTRA_FOOD);
            if (foodSelected.getQuantity() != null) {
                mMeal.getFoods().add(new RecognisedFoodResponse(foodSelected.getId(), foodSelected.getName(), foodSelected.getQuantity()));
            } else {
                mMeal.getFoods().add(new RecognisedFoodResponse(foodSelected.getId(), foodSelected.getName()));
            }
            mAnalysedImgAdapter.setFoods(mMeal.getFoods());
        }
    }
}
