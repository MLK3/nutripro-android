package com.oddsix.nutripro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;

/**
 * Created by Filippe on 21/10/16.
 */

public class AnalysedPictureFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }
}
