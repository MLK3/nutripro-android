package com.oddsix.nutripro.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.DBAllMealsByDayModel;
import com.oddsix.nutripro.models.DBDayMealModel;
import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealModel;
import com.oddsix.nutripro.models.DBMealNutrientModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.DietNutrientResponse;
import com.oddsix.nutripro.rest.models.responses.NutrientWeekResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.rest.models.responses.WeekMealResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;
import com.oddsix.nutripro.utils.helpers.DialogHelper;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by filippecl on 20/12/16.
 */

public class WeekResumeActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    private final String CHART_DATE_FORMAT = "EEE";
    private LineChart mChart;
    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;
    private Calendar mDate;
    private DialogHelper mDialogHelper;
    private TextView mWeekTv;
    private Realm mRealm;
    private TextView mNutrientTv;
    private TextView mUnitTv;

    private List<DBDayMealModel> mDays;

    private WeekMealResponse mWeekMeal;

    private NutrientWeekResponse mNutrientResponse;
    private SuggestedDietResponse mSuggestedDiet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_resume);
        setToolbar(true, getString(R.string.week_resume_activity_label));

        findViews();
        setViews();

        mChart = (LineChart) findViewById(R.id.week_resume_linechart);
        mProvider = new NutriproProvider(this);

        mRealm = Realm.getDefaultInstance();

        mFeedbackHelper = new FeedbackHelper(this, (ViewGroup) findViewById(R.id.container), mOnTryAgainClickListener);

        mDialogHelper = new DialogHelper(this);

        mDate = getFirstDayOfTheWeek(Calendar.getInstance());

        DBDietModel dietModel = mRealm.where(DBDietModel.class)
                .equalTo("email", SharedPreferencesHelper.getInstance().getUserEmail()).findFirst();

        setWeekLabel();

        if (dietModel != null) {
            mSuggestedDiet = new SuggestedDietResponse(dietModel);
            getAllMealsInvolved();
        }
    }

    private void getAllMealsInvolved() {
        mDays = new ArrayList<>();
        mFeedbackHelper.startLoading();
        DBAllMealsByDayModel allMealsByDayModel = mRealm.where(DBAllMealsByDayModel.class)
                .equalTo("email", SharedPreferencesHelper.getInstance().getUserEmail()).findFirst();

        //initialize all meals if there is no meal
        if (allMealsByDayModel == null) {
            mFeedbackHelper.showEmptyPlaceHolder();
        } else {
            Date date = mDate.getTime();
            //get date meals
            for (int i = 0; i < 7; i++) {
                for (DBDayMealModel dayMealModel : allMealsByDayModel.getAllDays()) {
                    try {
                        if (DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT,
                                dayMealModel.getDate()).equals(DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, date))) {
                            mDays.add(dayMealModel);
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                date = DateHelper.addDay(1, date);
            }
            if (mDays.isEmpty()) {
                mFeedbackHelper.showEmptyPlaceHolder();
            } else {
                buildWeekMeal();
                mFeedbackHelper.dismissFeedback();
            }
        }
    }

    private void buildWeekMeal() {
        mWeekMeal = new WeekMealResponse();

        DBDietModel dietModel = mRealm.where(DBDietModel.class)
                .equalTo("email", SharedPreferencesHelper.getInstance().getUserEmail()).findFirst();
        if (dietModel != null) {
            mSuggestedDiet = new SuggestedDietResponse(dietModel);
        }

        for (DietNutrientResponse dietNutrients : mSuggestedDiet.getNutrients()) {
            NutrientWeekResponse nutrientResponse = new NutrientWeekResponse(dietNutrients.getName(), dietNutrients.getUnit(),
                    dietNutrients.getMin(), dietNutrients.getMax());
            mWeekMeal.getNutrientResponses().add(nutrientResponse);
        }
        for (NutrientWeekResponse dietNutrient : mWeekMeal.getNutrientResponses()) {
            for (DBDayMealModel day : mDays) {
                int sum = 0;
                for (DBMealModel meal : day.getMeals()) {
                    for (DBMealFoodModel food : meal.getFoods()) {
                        for (DBMealNutrientModel nutrient : food.getNutrients()) {
                            if (dietNutrient.getName().equalsIgnoreCase(nutrient.getName())) {
                                sum += nutrient.getQuantity();
                                break;
                            }
                        }
                    }
                }
                dietNutrient.addQuantity(day.getDate(), sum);
            }
        }

        mNutrientResponse = mWeekMeal.getNutrientResponses().get(0);
        setChart();
        setNutrientLabel();
    }

    private void findViews() {
        mWeekTv = (TextView) findViewById(R.id.week_resume_week_tv);
        mNutrientTv = (TextView) findViewById(R.id.week_resume_nutrient_tv);
        mUnitTv = (TextView) findViewById(R.id.week_resume_linechart_yaxis_label);
    }

    private void setViews() {
        mNutrientTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> nutrients = new ArrayList<String>();
                for (NutrientWeekResponse nutrientResponse : mWeekMeal.getNutrientResponses()) {
                    nutrients.add(nutrientResponse.getName());
                }
                String[] strings = nutrients.toArray(new String[nutrients.size()]);
                mDialogHelper.showListDialog(getString(R.string.week_resume_dialog_title), strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mNutrientResponse = mWeekMeal.getNutrientResponses().get(i);
                        setChart();
                        setNutrientLabel();
                        mChart.invalidate();
                    }
                });
            }
        });
        mWeekTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void setNutrientLabel() {
        mNutrientTv.setText(mNutrientResponse.getName());
        mUnitTv.setText(mNutrientResponse.getUnit());
    }

    private void setWeekLabel() {
        String text;
        try {
            text = DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, mDate.getTime());
        } catch (ParseException e) {
            text = "";
        }
        mWeekTv.setText(getString(R.string.week_resume_label, text));
    }

    private Calendar getFirstDayOfTheWeek(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }
        return calendar;
    }

    private void sendRequest() {
        mFeedbackHelper.startLoading();
        try {
            mProvider.getWeekResume(DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, mDate.getTime()),
                    new NutriproProvider.OnResponseListener<WeekMealResponse>() {
                        @Override
                        public void onResponseSuccess(WeekMealResponse response) {
                            mWeekMeal = response;
                            if (response.getNutrientResponses().isEmpty()) {
                                mFeedbackHelper.showEmptyPlaceHolder();
                            } else {
                                mWeekMeal = response;
                                mNutrientResponse = response.getNutrientResponses().get(0);
                                setChart();
                                setNutrientLabel();
                                mFeedbackHelper.dismissFeedback();
                            }
                        }

                        @Override
                        public void onResponseFailure(String msg, int code) {
                            mFeedbackHelper.showErrorPlaceHolder();
                        }
                    });
        } catch (ParseException e) {
            mFeedbackHelper.dismissFeedback();
            e.printStackTrace();
        }
    }

    private View.OnClickListener mOnTryAgainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            sendRequest();
        }
    };

    private void setChart() {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            private FormattedStringCache.Generic<Long, Date> mFormattedStringCache = new FormattedStringCache.Generic<>(new SimpleDateFormat(CHART_DATE_FORMAT));

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Long v = (long) value;
                return mFormattedStringCache.getFormattedValue(new Date(v), v);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        LimitLine ll1 = new LimitLine(mNutrientResponse.getMax(), getString(R.string.week_resume_max_label));
        ll1.setLineWidth(2f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(14f);
        ll1.setLineColor(ContextCompat.getColor(this, R.color.colorAccent));

        LimitLine ll2 = new LimitLine(mNutrientResponse.getMin(), getString(R.string.week_resume_min_label));
        ll2.setLineWidth(2f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(14f);
        ll2.setLineColor(ContextCompat.getColor(this, R.color.color_red));

        mChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLimitLinesBehindData(true);

        try {
            setData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        mChart.setDragEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.setContentDescription("");

    }

    private void setData() throws ParseException {
        ArrayList<Entry> values = new ArrayList<Entry>();
        Calendar cal= Calendar.getInstance();
        cal.setTime(mNutrientResponse.getQuantities().get(0).getDate());
        cal = getFirstDayOfTheWeek(cal);
        for (int i = 0; i < 7; i++) {
            Date date = DateHelper.addDay(i, cal.getTime());
            boolean dayExists = false;
            for (int j = 0; j < mNutrientResponse.getQuantities().size(); j++) {
                if(DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, date).equalsIgnoreCase(DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, mNutrientResponse.getQuantities().get(j).getDate()))) {
                    values.add(new Entry(mNutrientResponse.getQuantities().get(j).getDate().getTime(), mNutrientResponse.getQuantities().get(j).getSum()));
                    dayExists = true;
                    break;
                }
            }
            if(!dayExists) {
                values.add(new Entry(date.getTime(), 0));
            }
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setColor(ContextCompat.getColor(this, R.color.color_black));
            set1.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
            set1.setLineWidth(1f);
            set1.setCircleRadius(5f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            // set1.setFormSize(15.f);

            set1.setFillColor(Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dpd.show(this.getFragmentManager(), "dpd");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        mDate = getFirstDayOfTheWeek(cal);
        setWeekLabel();
        getAllMealsInvolved();
//        sendRequest();
    }
}
