package com.oddsix.nutripro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.RegisterActivity;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DBDietNutrientModel;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.utils.Constants;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Filippe on 21/10/16.
 */

public class ProfileFragment extends BaseFragment {
    private DBRegisterModel mUserData;
    private DietAdapter mAdapter;
    private View mHeaderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        setUserData();

        setListView(view);

        setDiet();

        return view;
    }

    private void setUserData() {
        DBRegisterModel register = Realm.getDefaultInstance().where(DBRegisterModel.class)
                .equalTo("mail", getActivity().getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_MAIL, ""))
                .findFirst();
        if (register != null) mUserData = register;
    }

    private void setListView(View view) {
        ListView list = (ListView) view.findViewById(R.id.listview);
        mAdapter = new DietAdapter(getActivity());
        list.setAdapter(mAdapter);
        list.addHeaderView(getHeader());
    }

    private void setDiet() {
        RealmList<DBDietNutrientModel> diet = mUserData.getDietModel().getDiet();
        mAdapter.setDiet(diet);
    }

    private View getHeader() {
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.header_profile, null);

        setHeader(mHeaderView);

        return mHeaderView;
    }

    private void setHeader(View headerView) {
        ((TextView)headerView.findViewById(R.id.header_profile_name)).setText(mUserData.getName());

        View genderContainer = headerView.findViewById(R.id.header_profile_gender);
        setProfileItem(genderContainer, getString(R.string.profile_item_gender), mUserData.getGender());

        View heightContainer = headerView.findViewById(R.id.header_profile_height);
        setProfileItem(heightContainer, getString(R.string.profile_item_height), String.valueOf(mUserData.getHeight()));

        View weightContainer = headerView.findViewById(R.id.header_profile_weight);
        setProfileItem(weightContainer, getString(R.string.profile_item_weight), String.valueOf(mUserData.getWeight()));

        View ageContainer = headerView.findViewById(R.id.header_profile_age);
        setProfileItem(ageContainer, getString(R.string.profile_item_age), String.valueOf(mUserData.getAge()));

        headerView.findViewById(R.id.register_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                intent.putExtra(Constants.EXTRA_BOOL_EDIT_REGISTER, true);
                startActivityForResult(intent, Constants.REQ_EDIT_REGISTER);
            }
        });
    }

    private void setProfileItem(View container, String label, String value) {
        TextView labelTv = (TextView) container.findViewById(R.id.profile_item_label);
        labelTv.setText(label);
        TextView valueTv = (TextView) container.findViewById(R.id.profile_item_value);
        valueTv.setText(value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQ_EDIT_REGISTER && resultCode == Activity.RESULT_OK) {
            setUserData();
            setHeader(mHeaderView);
        }
    }
}
