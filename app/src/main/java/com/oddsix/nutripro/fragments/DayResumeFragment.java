package com.oddsix.nutripro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DayResumeAdapter;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DayMealModel;
import com.oddsix.nutripro.models.DietModel;
import com.oddsix.nutripro.models.DietNutrientModel;
import com.oddsix.nutripro.models.MealFoodModel;
import com.oddsix.nutripro.models.MealModel;
import com.oddsix.nutripro.models.MealNutrientModel;
import com.oddsix.nutripro.models.RegisterModel;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_resume, container, false);

        setDayLabel(view);

        mRealm = Realm.getDefaultInstance();

        metrics = new DisplayMetrics();

        setListView(view);

        return view;
    }

    private void setListView(View view) {
        mListView = (ListView) view.findViewById(R.id.day_resume_lv);
        mAdapter = new DayResumeAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(getHeader());
        mListView.setHeaderDividersEnabled(false);
        mHeaderView.setClickable(false);
    }


    private View getHeader() {
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.header_day_resume, null);

        setHeader(mHeaderView);

        return mHeaderView;
    }

    private void setHeader(View headerView) {
        LinearLayout container = (LinearLayout) headerView.findViewById(R.id.chart_container);

        Calendar cal = Calendar.getInstance();
        cal.set(2016, 10, 2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DayMealModel day = mRealm.where(DayMealModel.class)
                .equalTo("dateString", dateFormat.format(cal.getTime()))
                .findFirst();

        mAdapter.setMeals(day.getMeals());

        DietModel diet = mRealm.where(RegisterModel.class)
                .equalTo("mail", getActivity().getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_MAIL, ""))
                .findFirst().getDietModel();

        for (DietNutrientModel dietNutrient : diet.getDiet()) {
            View bar = getActivity().getLayoutInflater().inflate(R.layout.partial_horizontal_bar_chart, container, false);

            setBar(bar, day, dietNutrient);

            container.addView(bar);
        }

        View maxBar = headerView.findViewById(R.id.chart_max_bar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) maxBar.getLayoutParams();
        params.setMargins((metrics.widthPixels * 100) / 150, 0, 0, 0);
        maxBar.setLayoutParams(params);
    }

    private void setBar(View bar, DayMealModel day, DietNutrientModel dietNutrient) {
        int quantity = 0;
        for (MealModel meal : day.getMeals()) {
            for (MealFoodModel food : meal.getFoods()) {
                for (MealNutrientModel nutrient : food.getNutrients()) {
                    if (dietNutrient.getName().equals(nutrient.getName())) {
                        quantity += nutrient.getQuantity();
                    }
                }
            }
        }

        View barValue = bar.findViewById(R.id.chart_bar);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        barValue.getLayoutParams().width = (int) (((quantity * metrics.widthPixels)) / (1.5 * dietNutrient.getMax()));

        ((TextView) bar.findViewById(R.id.chart_item_name)).setText(dietNutrient.getName());
        ((TextView) bar.findViewById(R.id.chart_item_value)).setText(String.valueOf(quantity));
    }

    private void setDayLabel(View view) {
        mDayTv = (TextView) view.findViewById(R.id.resume_day_label);
        setDateLabel(Calendar.getInstance());
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
    }
}
