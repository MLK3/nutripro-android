package com.oddsix.nutripro.fragments;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.FoodInfoActivity;
import com.oddsix.nutripro.activities.MainActivity;
import com.oddsix.nutripro.activities.SearchActivity;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;
import com.oddsix.nutripro.models.AreaModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.requests.CreateMealRequest;
import com.oddsix.nutripro.rest.models.requests.FoodRequest;
import com.oddsix.nutripro.rest.models.responses.AnalysedPictureResponse;
import com.oddsix.nutripro.rest.models.responses.AnalysedRecognisedFoodResponse;
import com.oddsix.nutripro.rest.models.responses.FoodResponse;
import com.oddsix.nutripro.rest.models.responses.GeneralResponse;
import com.oddsix.nutripro.rest.models.responses.RecognisedFoodResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.base.BaseDialogHelper;
import com.oddsix.nutripro.utils.helpers.AppColorHelper;
import com.oddsix.nutripro.utils.helpers.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippe on 21/10/16.
 */

public class AnalysedPictureFragment extends BaseFragment {
    private AnalysedImgAdapter mAnalysedImgAdapter;
    private DialogHelper mDialogHelper;
    private NutriproProvider mProvider;
    private AppColorHelper mColorHelper;
    private AnalysedPictureResponse mMeal;

    private LayoutInflater mInflater;
    private View mRootView;
    private View mHeaderView;

    private int mEditingFoodIndex = 0;

    private Canvas mCanvas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        this.mRootView = view;
        mDialogHelper = new DialogHelper(getActivity());

        mProvider = new NutriproProvider(getActivity());

        mColorHelper = new AppColorHelper(getActivity());

        mInflater = inflater;

        setListView();
        return view;
    }

    @SuppressWarnings("unchecked")
    private void setListView() {
        mAnalysedImgAdapter = new AnalysedImgAdapter(getActivity(), new AnalysedImgAdapter.OnNutrientClickListener() {
            @Override
            public void onEditValueClicked(int position) {
                showInputDialog(position);
            }

            @Override
            public void onEditNameClicked(final int position) {
                showListDialog(position);
            }

            @Override
            public void onEditInfoClicked(int position) {
                startFoodInfoActivity(position);
            }

            @Override
            public void onEditNameLongClicked(final int position) {
                mDialogHelper.showAlertDialog("Tem certeza que deseja remover este alimento?",
                        "Remover",
                        "Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMeal.getFoods().remove(position);
                                mAnalysedImgAdapter.setFoods((List<RecognisedFoodResponse>) (List<?>) mMeal.getFoods());
                                mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                                setImage(mHeaderView);
                            }
                        });
            }
        });
        ListView listView = (ListView) mRootView.findViewById(R.id.listview);
        listView.setDividerHeight(0);
        listView.setAdapter(mAnalysedImgAdapter);
        mHeaderView = inflateHeader(mInflater);
        listView.addHeaderView(mHeaderView);
        listView.addFooterView(inflateFooter(mInflater));
    }

    @SuppressWarnings("unchecked")
    private void showInputDialog(final int position) {
        mDialogHelper.showEditTextDialog(getString(R.string.meal_detail_quantity_dialog_title),
                getString(R.string.action_confirm),
                getString(R.string.action_cancel),
                new BaseDialogHelper.OnEditTextDialogClickListener() {
                    @Override
                    public void onInputConfirmed(int value) {
                        mMeal.getFoods().get(position).setQuantity(value);
                        mAnalysedImgAdapter.setFoods((List<RecognisedFoodResponse>) (List<?>) mMeal.getFoods());
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void showListDialog(final int position) {
        //TODO PUT STRINGS IN XML
        List<String> strings = new ArrayList<>();

        for (AnalysedRecognisedFoodResponse.Suggestion suggestion :
                mMeal.getFoods().get(position).getSuggestions()) {
            strings.add(suggestion.getName());
        }
        strings.add("Outro");

        final String[] options = strings.toArray(new String[strings.size()]);
        mDialogHelper.showListDialog("Selecione a opção correta", options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mEditingFoodIndex = position;
                if (i == options.length - 1) {
                    Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                    startActivityForResult(searchIntent, Constants.REQ_REPLACE_FOOD);
                } else {
                    AnalysedRecognisedFoodResponse.Suggestion chosenSuggestion = mMeal.getFoods().get(position).getSuggestions().get(i);
                    mMeal.getFoods().get(mEditingFoodIndex).setName(options[i]);
                    mMeal.getFoods().get(mEditingFoodIndex).setId(chosenSuggestion.getId());
                    mMeal.getFoods().get(mEditingFoodIndex).setQuantity(chosenSuggestion.getQuantity());
                    mAnalysedImgAdapter.setFoods((List<RecognisedFoodResponse>) (List<?>) mMeal.getFoods());
                }
            }
        });
    }

    private View inflateHeader(LayoutInflater inflater) {
        View headerView = inflater.inflate(R.layout.header_analysed_photo, null);
        mHeaderView = headerView;
        return headerView;
    }

    private View inflateFooter(LayoutInflater inflater) {
        View footerView = inflater.inflate(R.layout.footer_analysed_photo, null);
        ((Button) footerView.findViewById(R.id.footer_analysed_photo_conclude)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FoodRequest> foods = new ArrayList<FoodRequest>();
                for (AnalysedRecognisedFoodResponse food : mMeal.getFoods()) {
                    foods.add(new FoodRequest(food.getArea().getArea_id(), food.getId(), food.getQuantity()));
                }
                mProvider.createMeal(new CreateMealRequest(
                                mMeal.getPicture_id(),
                                ((TextView) mHeaderView.findViewById(R.id.header_analysed_meal_name_tv)).getText().toString(),
                                foods),
                        new NutriproProvider.OnResponseListener<GeneralResponse>() {
                            @Override
                            public void onResponseSuccess(GeneralResponse response) {
                                showToast("Refeição cadastrada com sucesso.");
                                ((MainActivity) getActivity()).resetTabIndex();
                            }

                            @Override
                            public void onResponseFailure(String msg, int code) {
                                showToast(msg);
                            }
                        });
            }
        });
        ((Button) footerView.findViewById(R.id.footer_analysed_photo_conclude)).setText(getString(R.string.meal_detail_btn_save));
        //Add a new Item
        footerView.findViewById(R.id.footer_analysed_photo_add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivityAddingFood();
            }
        });
        return footerView;
    }

    public void setImage(final View headerView) {
        Glide.with(this).load(mMeal.getPictureUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //TODO UNCOMMENT THIS LINE IF THE REQUEST RETURNS THE PICTURE URL
//                ((ImageView) headerView.findViewById(R.id.header_analysed_photo_img)).setImageBitmap(resource);
                setDrawing(headerView, resource);
            }
        });
    }

    private void startFoodInfoActivity(int position) {
        Intent infoIntent = new Intent(getActivity(), FoodInfoActivity.class);
        infoIntent.putExtra(Constants.EXTRA_FOOD_MODEL, (RecognisedFoodResponse) mMeal.getFoods().get(position));
        startActivity(infoIntent);
    }

    private void startSearchActivityAddingFood() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_ADD_FOOD);
    }

    private void startSearchActivityReplacingArea() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_REPLACE_BY_AREA);
    }

    //TODO PUT DRAWING METHODS IN OTHER CLASS
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
            AnalysedRecognisedFoodResponse recognisedFood = mMeal.getFoods().get(i);

            //Set color, width and alpha
            Paint wallPaint = new Paint();
            wallPaint.setColor(mColorHelper.getNextColor());
            wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            wallPaint.setStrokeWidth(1);
            wallPaint.setAlpha(80);

            //Draw polygon
            Path wallPath = new Path();
            wallPath.reset();
            List<AnalysedRecognisedFoodResponse.Area.Point> points = recognisedFood.getArea().getPoints();
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
            public boolean onTouch(View mRootView, MotionEvent motionEvent) {
                Point point = new Point();
                point.x = (int) ((motionEvent.getX() * resWidth) / mRootView.getWidth());
                point.y = (int) ((motionEvent.getY() * resHeight) / mRootView.getHeight());

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

    @SuppressWarnings("unchecked")
    public void setImage(final Bitmap image, String photo64) {
        showProgressdialog();
        mProvider.analysePicture(photo64, new NutriproProvider.OnResponseListener<AnalysedPictureResponse>() {
            @Override
            public void onResponseSuccess(AnalysedPictureResponse response) {
                dismissProgressDialog();
                mMeal = response;
                ((ImageView) mHeaderView.findViewById(R.id.header_analysed_photo_img)).setImageBitmap(image);
                setImage(mHeaderView);
                mAnalysedImgAdapter.setFoods((List<RecognisedFoodResponse>) (List<?>) response.getFoods());
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                dismissProgressDialog();
                showToast(msg);
                ((MainActivity) getActivity()).resetTabIndex();
            }
        });

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && (requestCode == Constants.REQ_REPLACE_FOOD || requestCode == Constants.REQ_REPLACE_BY_AREA)) {
            FoodResponse foodSelected = (FoodResponse) data.getSerializableExtra(Constants.EXTRA_FOOD);
            mMeal.getFoods().get(mEditingFoodIndex).setId(foodSelected.getId());
            mMeal.getFoods().get(mEditingFoodIndex).setName(foodSelected.getName());
            mAnalysedImgAdapter.setFoods((List<RecognisedFoodResponse>) (List<?>) mMeal.getFoods());
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQ_ADD_FOOD) {
            FoodResponse foodSelected = (FoodResponse) data.getSerializableExtra(Constants.EXTRA_FOOD);
            mMeal.getFoods().add(new AnalysedRecognisedFoodResponse(foodSelected.getId(), foodSelected.getName()));
            mAnalysedImgAdapter.setFoods((List<RecognisedFoodResponse>) (List<?>) mMeal.getFoods());
        }
    }
}
