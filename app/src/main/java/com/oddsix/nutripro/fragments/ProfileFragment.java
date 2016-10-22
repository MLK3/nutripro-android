package com.oddsix.nutripro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DietUnitModel;

import io.realm.RealmList;

/**
 * Created by Filippe on 21/10/16.
 */

public class ProfileFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        setListView(view);

        return view;
    }

    private void setListView(View view) {
        ListView list = (ListView) view.findViewById(R.id.listview);
        list.setAdapter(new DietAdapter(getActivity(), new RealmList<DietUnitModel>()));

        list.addHeaderView(setHeader());
    }

    private View setHeader() {
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.header_profile, null);
        return headerView;

    }
}
