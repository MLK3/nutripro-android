package com.oddsix.nutripro.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.FoodInfoActivity;
import com.oddsix.nutripro.activities.SearchActivity;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;
import com.oddsix.nutripro.models.AreaModel;
import com.oddsix.nutripro.models.DBDayMealModel;
import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealNutrientModel;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.AnalysedPictureResponse;
import com.oddsix.nutripro.rest.models.responses.RecognisedFoodResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.AppColorHelper;
import com.oddsix.nutripro.utils.helpers.DialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import io.realm.Realm;

/**
 * Created by Filippe on 21/10/16.
 */

public class AnalysedPictureFragment extends BaseFragment {
    private AnalysedImgAdapter mAnalysedImgAdapter;
    private View mHeaderView;
    private DialogHelper mDialogHelper;
    //    private Realm mRealm;
//    private List<FoodModel> mFoods = new ArrayList<>();
//    private DBDayMealModel mDay;
    private NutriproProvider mProvider;
    private AppColorHelper mColorHelper;
    private LayoutInflater mInflater;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
//        mRealm = Realm.getDefaultInstance();
        this.view = view;
        mDialogHelper = new DialogHelper(getActivity());

        mProvider = new NutriproProvider(getActivity());

        mColorHelper = new AppColorHelper(getActivity());

//        setMockData();
        mInflater = inflater;
        setListView();
        return view;
    }

//    private void setMockData() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(2016, 10, 2);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        mDay = mRealm.where(DBDayMealModel.class)
//                .equalTo("dateString", dateFormat.format(cal.getTime()))
//                .findFirst();
//        for (DBMealFoodModel dbFoods : mDay.getMeals().get(0).getFoods()) {
//            List<NutrientModel> nutrients = new ArrayList<>();
//            for (DBMealNutrientModel dbNutrients : dbFoods.getNutrients()) {
//                nutrients.add(new NutrientModel(dbNutrients.getName(), dbNutrients.getQuantity(), dbNutrients.getUnit()));
//            }
//            mFoods.add(new FoodModel(nutrients, dbFoods.getFoodName(), dbFoods.getQuantity()));
//        }
//    }

    private void setListView() {
        mAnalysedImgAdapter = new AnalysedImgAdapter(getActivity(), new AnalysedImgAdapter.OnNutrientClickListener() {
            @Override
            public void onEditValueClicked(int position) {
                functionNotImplemented();
            }

            @Override
            public void onEditNameClicked(final int position) {
                List<String> strings = new ArrayList<>();

                for (AnalysedPictureResponse.Food.Suggestion suggestion :
                        mResponse.getFoods().get(position).getSuggestions()) {
                    strings.add(suggestion.getName());
                }
                strings.add("Outro");

                final String[] options = strings.toArray(new String[strings.size()]);
                mDialogHelper.showListDialog("Selecione a opção correta", options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == options.length - 1) {
                            Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                            startActivityForResult(searchIntent, Constants.REQ_REPLACE_FOOD);
                        } else {
                            //TODO replace food
                        }
                    }
                });
            }

            @Override
            public void onEditInfoClicked(int position) {
                Intent infoIntent = new Intent(getActivity(), FoodInfoActivity.class);
//                infoIntent.putExtra(Constants.EXTRA_FOOD_MODEL, mFoods.get(position));
                startActivity(infoIntent);
            }
        });
//        mAnalysedImgAdapter.setFoods(mFoods);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(mAnalysedImgAdapter);
        mHeaderView = inflateHeader(mInflater);
        listView.addHeaderView(mHeaderView);
        listView.addFooterView(inflateFooter(mInflater));
    }

    private View inflateHeader(LayoutInflater inflater) {
        View headerView = inflater.inflate(R.layout.header_analysed_photo, null);
        mHeaderView = headerView;
        return headerView;
    }

    public void setImage(final View headerView) {
        Glide.with(this).load(mResponse.getPictureUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
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

        for (int i = 0; i < mResponse.getFoods().size(); i++) {
            AnalysedPictureResponse.Food recognisedFood = mResponse.getFoods().get(i);

            //Set color, width and alpha
            Paint wallPaint = new Paint();
            wallPaint.setColor(mColorHelper.getNextColor());
            wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            wallPaint.setStrokeWidth(1);
            wallPaint.setAlpha(80);

            //Draw polygon
            Path wallPath = new Path();
            wallPath.reset();
            List<AnalysedPictureResponse.Food.Point> points = recognisedFood.getPoints();
            //Initial point
            wallPath.moveTo(points.get(0).getX(), points.get(0).getY());
            for (int j = 1; j < recognisedFood.getPoints().size(); j++) {
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

            //todo
//            areas.add(new AreaModel(r, recognisedFood, i));
        }

        imageView.setImageBitmap(bitmap);

        //todo
//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Point point = new Point();
//                point.x = (int) ((motionEvent.getX() * resWidth) / view.getWidth());
//                point.y = (int) ((motionEvent.getY() * resHeight) / view.getHeight());
//
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    for (AreaModel area : areas) {
//                        if (area.getRegion().contains(point.x, point.y)) {
//                            mMealIndexEditing = area.getArrayIndex();
//                            startSearchActivityReplacingArea();
//                            break;
//                        }
//                    }
//                }
//
//                return true;
//            }
//        });
    }

    private void startSearchActivityReplacingFood() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_REPLACE_FOOD);
    }

    private void startSearchActivityAddingFood() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_ADD_FOOD);
    }

    private void startSearchActivityReplacingArea() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, Constants.REQ_REPLACE_BY_AREA);
    }


    private View inflateFooter(LayoutInflater inflater) {
        View footerView = inflater.inflate(R.layout.footer_analysed_photo, null);
        ((Button) footerView.findViewById(R.id.footer_analysed_photo_conclude)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionNotImplemented();
                //todo send meal

            }
        });
        return footerView;
    }

    AnalysedPictureResponse mResponse;

    public void setImage(Bitmap image, String photo64) {
        ((ImageView) mHeaderView.findViewById(R.id.header_analysed_photo_img)).setImageBitmap(image);
        mProvider.analysePicture(photo64, new NutriproProvider.OnResponseListener<AnalysedPictureResponse>() {
            @Override
            public void onResponseSuccess(AnalysedPictureResponse response) {
                mResponse = response;
//                setImage(mHeaderView);
//                setListView();
//                mAnalysedImgAdapter.setFoods();
                //SET IMAGE
            }

            @Override
            public void onResponseFailure(String msg, int code) {
            }
        });

    }
}
