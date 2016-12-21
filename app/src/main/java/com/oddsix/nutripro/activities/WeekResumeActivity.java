package com.oddsix.nutripro.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.WeekMealResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;
import com.oddsix.nutripro.utils.helpers.DialogHelper;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class WeekResumeActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{
    private LineChart mChart;
    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;
    private Date mDate;
    private DialogHelper mDialogHelper;

    private TextView mWeekTv;
    private TextView mNutrientTv;
    private TextView mUnitTv;

    private WeekMealResponse mWeekMeal;
    private WeekMealResponse.Nutrient mNutrient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_resume);
        setToolbar(true, getString(R.string.week_resume_activity_label));

        findViews();
        setViews();

        mChart = (LineChart) findViewById(R.id.week_resume_linechart);
        mProvider = new NutriproProvider(this);

        mFeedbackHelper = new FeedbackHelper(this, (ViewGroup) findViewById(R.id.container), mOnTryAgainClickListener);

        mDialogHelper = new DialogHelper(this);

        mDate = getFirstDayOfTheWeek(Calendar.getInstance());
        setWeekLabel();

        sendRequest();
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
                for (WeekMealResponse.Nutrient nutrient : mWeekMeal.getNutrients()) {
                    nutrients.add(nutrient.getName());
                }
                String[] strings = nutrients.toArray(new String[nutrients.size()]);
                mDialogHelper.showListDialog(getString(R.string.week_resume_dialog_title), strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mNutrient = mWeekMeal.getNutrients().get(i);
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
        mNutrientTv.setText(mNutrient.getName());
        mUnitTv.setText(mNutrient.getUnit());
    }

    private void setWeekLabel() {
        String text;
        try {
            text = DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, mDate);
        } catch (ParseException e) {
            text = "";
        }
        mWeekTv.setText(getString(R.string.week_resume_label, text));
    }

    private Date getFirstDayOfTheWeek(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }
        return calendar.getTime();
    }

    private void sendRequest() {
        mFeedbackHelper.startLoading();
        try {
            mProvider.getWeekResume(DateHelper.parseDate(Constants.STANDARD_DATE_FORMAT, mDate), new NutriproProvider.OnResponseListener<WeekMealResponse>() {
                @Override
                public void onResponseSuccess(WeekMealResponse response) {
                    mWeekMeal = response;
                    if (response.getNutrients().isEmpty()) {
                        mFeedbackHelper.showEmptyPlaceHolder();
                    } else {
                        mWeekMeal = response;
                        mNutrient = response.getNutrients().get(0);
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
            sendRequest();
        }
    };

    private void setChart() {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);

        LimitLine ll1 = new LimitLine(mNutrient.getMax(), getString(R.string.week_resume_max_label));
        ll1.setLineWidth(2f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(14f);
        ll1.setLineColor(ContextCompat.getColor(this, R.color.colorAccent));

        LimitLine ll2 = new LimitLine(mNutrient.getMin(), getString(R.string.week_resume_min_label));
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

        setData();

//        mChart.setDragEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.setContentDescription("");

    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < mNutrient.getQuantities().size(); i++) {
            values.add(new Entry(i, mNutrient.getQuantities().get(i).getSum()));
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
//            set1.setFormSize(15.f);

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
        sendRequest();
    }
}
