package com.oddsix.nutripro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.models.RegisterModel;
import com.oddsix.nutripro.utils.Constants;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Filippe on 21/10/16.
 */

public class ProfileFragment extends BaseFragment {
    private RegisterModel mUserData;
    private DietAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        RegisterModel register = Realm.getDefaultInstance().where(RegisterModel.class)
                .equalTo("mail", getActivity().getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_MAIL, ""))
                .findFirst();
        if (register != null) mUserData = register;

        setListView(view);

        setDiet();

        return view;
    }

    private void setListView(View view) {
        ListView list = (ListView) view.findViewById(R.id.listview);
        mAdapter = new DietAdapter(getActivity());
        list.setAdapter(mAdapter);
        list.addHeaderView(setHeader());
    }

    private void setDiet() {
        RealmList<NutrientModel> diet = mUserData.getDietModel().getDiet();
        mAdapter.setDiet(diet);
    }

    private View setHeader() {
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.header_profile, null);

        ((TextView)headerView.findViewById(R.id.header_profile_name)).setText(mUserData.getName());

        View genderContainer = headerView.findViewById(R.id.header_profile_gender);
        setProfileItem(genderContainer, getString(R.string.profile_item_gender), mUserData.getGender());

        View heightContainer = headerView.findViewById(R.id.header_profile_height);
        setProfileItem(heightContainer, getString(R.string.profile_item_height), String.valueOf(mUserData.getHeight()));

        View weightContainer = headerView.findViewById(R.id.header_profile_weight);
        setProfileItem(weightContainer, getString(R.string.profile_item_weight), String.valueOf(mUserData.getWeight()));

        View ageContainer = headerView.findViewById(R.id.header_profile_age);
        setProfileItem(ageContainer, getString(R.string.profile_item_age), String.valueOf(mUserData.getAge()));

        return headerView;
    }

    private void setProfileItem(View container, String label, String value) {
        TextView labelTv = (TextView) container.findViewById(R.id.profile_item_label);
        labelTv.setText(label);
        TextView valueTv = (TextView) container.findViewById(R.id.profile_item_value);
        valueTv.setText(value);
    }
}
