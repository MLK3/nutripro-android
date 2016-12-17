package com.oddsix.nutripro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.MainActivity;
import com.oddsix.nutripro.activities.MealDetailActivity;
import com.oddsix.nutripro.adapters.DayResumeAdapter;
import com.oddsix.nutripro.models.DBDayMealModel;
import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBDietNutrientModel;
import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealModel;
import com.oddsix.nutripro.models.DBMealNutrientModel;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.models.MealModel;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.DayResumeResponse;
import com.oddsix.nutripro.rest.models.responses.DietNutrientResponse;
import com.oddsix.nutripro.rest.models.responses.NutrientResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Filippe on 21/10/16.
 */

public class DayResumeFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private TextView mDayTv;
    private DayResumeAdapter mAdapter;
    private View mHeaderView;
    private Realm mRealm;
    private ListView mListView;
    private DisplayMetrics metrics;
    private FeedbackHelper mFeedbackHelper;
    private NutriproProvider mProvider;

    private View mView;
    private DayResumeResponse mDay;
    private Calendar mDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_day_resume, container, false);

        mProvider = new NutriproProvider(getActivity());

        mFeedbackHelper = new FeedbackHelper(getActivity(), (LinearLayout) mView.findViewById(R.id.container), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDay == null) getMealByDay(mDate);
            }
        });
        setDayLabel(mView);

//        mRealm = Realm.getDefaultInstance();

        metrics = new DisplayMetrics();

//        setListView(mView);

        if (mDate == null) {
            mDate = Calendar.getInstance();
            getMealByDay(mDate);
        } else if (mDay == null) {
            getMealByDay(mDate);
        } else {
            setListView(mView);
            setDateLabel(mDate);
        }

        return mView;
    }

    private void getMealByDay(Calendar date) {
        setDateLabel(date);
        mFeedbackHelper.startLoading();
        try {
            mProvider.getMealsByDay(date.getTime(), new NutriproProvider.OnResponseListener<DayResumeResponse>() {
                @Override
                public void onResponseSuccess(DayResumeResponse response) {
                    mDay = response;
                    setListView(mView);
                    mFeedbackHelper.dismissFeedback();
                }

                @Override
                public void onResponseFailure(String msg, int code) {
                    mFeedbackHelper.showErrorPlaceHolder();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
            mFeedbackHelper.showErrorPlaceHolder();
        }
    }

    private void setListView(View view) {
        mListView = (ListView) view.findViewById(R.id.day_resume_lv);
        mAdapter = new DayResumeAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(getHeader());
        mListView.setHeaderDividersEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //header position
                if (i != 0) {
                    int arrayPosition = --i;
                    startMealDetailActivity(mDay.getMeals().get(arrayPosition));
                }
            }
        });
        mHeaderView.setClickable(false);
    }

    private void startMealDetailActivity(DayResumeResponse.MealResponse meal) {
        Intent mealDetailIntent = new Intent(getActivity(), MealDetailActivity.class);
        mealDetailIntent.putExtra(Constants.EXTRA_MEAL_MODEL, meal);
        startActivity(mealDetailIntent);
    }


    private View getHeader() {
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.header_day_resume, null);

        setHeader();

        return mHeaderView;
    }


    private void setHeader() {
        LinearLayout container = (LinearLayout) mHeaderView.findViewById(R.id.chart_container);

//        Calendar cal = Calendar.getInstance();
//        cal.set(2016, 10, 2);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        mDay = mRealm.where(DBDayMealModel.class)
//                .equalTo("dateString", dateFormat.format(cal.getTime()))
//                .findFirst();

        mAdapter.setMeals(mDay.getMeals());

//        DBDietModel diet = mRealm.where(DBRegisterModel.class)
//                .equalTo("mail", getActivity().getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_MAIL, ""))
//                .findFirst().getDietModel();

        for (NutrientResponse nutrient : mDay.getNutrients()) {
            for (DietNutrientResponse dietNutrient : ((MainActivity) getActivity()).getSuggestedDiet().getNutrients()) {
                if (nutrient.getName().equalsIgnoreCase(dietNutrient.getName())) {
                    View bar = getActivity().getLayoutInflater().inflate(R.layout.partial_horizontal_bar_chart, container, false);

                    setBar(bar, dietNutrient, nutrient);

                    container.addView(bar);
                }
            }
        }
    }

    private void setBar(View bar, DietNutrientResponse dietNutrient, NutrientResponse nutrient) {
        View barValue = bar.findViewById(R.id.chart_bar);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        barValue.getLayoutParams().width = (int) (((nutrient.getQuantity() * metrics.widthPixels)) / (1.5 * dietNutrient.getMax()));

        View maxBar = bar.findViewById(R.id.chart_max_bar);
        RelativeLayout.LayoutParams maxParams = (RelativeLayout.LayoutParams) maxBar.getLayoutParams();
        maxParams.setMargins((metrics.widthPixels * 100) / 150, 0, 0, 0);
        maxBar.setLayoutParams(maxParams);

        View minBar = bar.findViewById(R.id.chart_min_bar);
        RelativeLayout.LayoutParams minParams = (RelativeLayout.LayoutParams) minBar.getLayoutParams();
        minParams.setMargins((metrics.widthPixels * (dietNutrient.getMin()) * 100 / (dietNutrient.getMax())) / 150, 0, 0, 0);
        minBar.setLayoutParams(minParams);

        ((TextView) bar.findViewById(R.id.chart_item_name)).setText(dietNutrient.getName());
        ((TextView) bar.findViewById(R.id.chart_item_value)).setText(getString(R.string.food_info_quantity, nutrient.getQuantity(), dietNutrient.getUnit()));
    }

    private void setDayLabel(View view) {
        mDayTv = (TextView) view.findViewById(R.id.resume_day_label);
        mDayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void setDateLabel(Calendar cal) {
        try {
            mDayTv.setText(DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        //TODO DISABLE DAYS WITHOUT DATA
//        Calendar[] calendars = {calendar};
//        dpd.setDisabledDays(calendars);
        dpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        dpd.show(getActivity().getFragmentManager(), "dpd");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(Calendar.YEAR, year);
        selectedCal.set(Calendar.MONTH, monthOfYear);
        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setDateLabel(selectedCal);
        mDate = selectedCal;
        getMealByDay(selectedCal);
    }
}
