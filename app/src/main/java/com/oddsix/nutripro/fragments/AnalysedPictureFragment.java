package com.oddsix.nutripro.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.FoodInfoActivity;
import com.oddsix.nutripro.activities.SearchActivity;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;
import com.oddsix.nutripro.models.DBDayMealModel;
import com.oddsix.nutripro.models.DBDietNutrientModel;
import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealNutrientModel;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.DialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Filippe on 21/10/16.
 */

public class AnalysedPictureFragment extends BaseFragment {
    private AnalysedImgAdapter mAnalysedImgAdapter;
    private View mHeaderView;
    private DialogHelper mDialogHelper;
    private Realm mRealm;
    private List<FoodModel> mFoods = new ArrayList<>();
    private DBDayMealModel mDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        mRealm = Realm.getDefaultInstance();
        mDialogHelper = new DialogHelper(getActivity());

        setMockData();

        setListView(view, inflater);
        return view;
    }

    private void setMockData() {
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 10, 2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mDay = mRealm.where(DBDayMealModel.class)
                .equalTo("dateString", dateFormat.format(cal.getTime()))
                .findFirst();
        for (DBMealFoodModel dbFoods: mDay.getMeals().get(0).getFoods()) {
            List<NutrientModel> nutrients = new ArrayList<>();
            for (DBMealNutrientModel dbNutrients: dbFoods.getNutrients()) {
                nutrients.add(new NutrientModel(dbNutrients.getName(), dbNutrients.getQuantity(), dbNutrients.getUnit()));
            }
            mFoods.add(new FoodModel(nutrients, dbFoods.getFoodName(), dbFoods.getQuantity()));
        }
    }

    private void setListView(View view, LayoutInflater inflater) {
        mAnalysedImgAdapter = new AnalysedImgAdapter(getActivity(), new AnalysedImgAdapter.OnNutrientClickListener() {
            @Override
            public void onEditValueClicked(int position) {
                functionNotImplemented();
            }

            @Override
            public void onEditNameClicked(final int position) {
                final String[] options = {"Arroz Branco", "Arroz integral", "Outro"};
                mDialogHelper.showListDialog("Selecione a opção correta", options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == options.length - 1) {
                            Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                            startActivityForResult(searchIntent, Constants.REQ_SEARCH);
                        } else {
                            mFoods.get(position).setFoodName(options[i]);
                            mAnalysedImgAdapter.setFoods(mFoods);
                        }
                    }
                });
            }

            @Override
            public void onEditInfoClicked(int position) {
                Intent infoIntent = new Intent(getActivity(), FoodInfoActivity.class);
                infoIntent.putExtra(Constants.EXTRA_FOOD_MODEL, mFoods.get(position));
                startActivity(infoIntent);
            }
        });
        mAnalysedImgAdapter.setFoods(mFoods);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(mAnalysedImgAdapter);
        mHeaderView = inflateHeader(inflater);
        listView.addHeaderView(mHeaderView);
        listView.addFooterView(inflateFooter(inflater));
    }

    private View inflateHeader(LayoutInflater inflater) {
        View headerView = inflater.inflate(R.layout.header_analysed_photo, null);
        return headerView;
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

    public void setImage(Bitmap image) {
        ((ImageView) mHeaderView.findViewById(R.id.header_analysed_photo_img)).setImageBitmap(image);
    }
}
