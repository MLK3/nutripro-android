package com.oddsix.nutripro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DayResumeAdapter;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Filippe on 21/10/16.
 */

public class DayResumeFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private TextView mDayTv;
    private DayResumeAdapter mAdapter;
    private View mHeaderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_resume, container, false);

        setDayLabel(view);

        setListView(view);

        return view;
    }

    private void setListView(View view) {
        ListView list = (ListView) view.findViewById(R.id.day_resume_lv);
        mAdapter = new DayResumeAdapter(getActivity());
        list.setAdapter(mAdapter);
        list.addHeaderView(getHeader());
        list.setHeaderDividersEnabled(false);
        mHeaderView.setClickable(false);
    }


    private View getHeader() {
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.header_day_resume, null);

        setHeader(mHeaderView);

        return mHeaderView;
    }

    private void setHeader(View headerView) {

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
