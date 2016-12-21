package com.oddsix.nutripro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.activities.EditDietActivity;
import com.oddsix.nutripro.activities.MainActivity;
import com.oddsix.nutripro.activities.RegisterActivity;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DBDietNutrientModel;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.RegisterResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Filippe on 21/10/16.
 */

public class ProfileFragment extends BaseFragment {
    private DietAdapter mAdapter;
    private View mHeaderView;
    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;
    private RegisterResponse mRegister;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_listview, container, false);

        mFeedbackHelper = new FeedbackHelper(getActivity(), (LinearLayout) mView.findViewById(R.id.container), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegister();
            }
        });

        mProvider = new NutriproProvider(getActivity());

        if(mRegister == null) getRegister();
        else setListView(mView);

        return mView;
    }

    private void getRegister() {
        mFeedbackHelper.startLoading();
        mProvider.getRegister(new NutriproProvider.OnResponseListener<RegisterResponse>() {
            @Override
            public void onResponseSuccess(RegisterResponse response) {
                mFeedbackHelper.dismissFeedback();
                mRegister = response;
                setListView(mView);

            }

            @Override
            public void onResponseFailure(String msg, int code) {
                mFeedbackHelper.showErrorPlaceHolder();
            }
        });
    }

    private void setListView(View view) {
        ListView list = (ListView) view.findViewById(R.id.listview);
        mAdapter = new DietAdapter(getActivity());
        list.setAdapter(mAdapter);
        mAdapter.setDiet(((MainActivity) getActivity()).getSuggestedDiet().getNutrients());
        list.addHeaderView(getHeader());
    }

    private View getHeader() {
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.header_profile, null);

        mHeaderView.findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditDietActivity.class);
                intent.putExtra(Constants.EXTRA_DIET, ((MainActivity) getActivity()).getSuggestedDiet());
                startActivityForResult(intent, Constants.REQ_EDIT_DIET);
            }
        });
        setHeader(mHeaderView);

        return mHeaderView;
    }

    private void setHeader(View headerView) {
        ((TextView)headerView.findViewById(R.id.header_profile_name)).setText(mRegister.getName());

        View genderContainer = headerView.findViewById(R.id.header_profile_gender);
        setProfileItem(genderContainer, getString(R.string.profile_item_gender), mRegister.getGender());

        View heightContainer = headerView.findViewById(R.id.header_profile_height);
        setProfileItem(heightContainer, getString(R.string.profile_item_height), String.valueOf(mRegister.getAltura()));

        View weightContainer = headerView.findViewById(R.id.header_profile_weight);
        setProfileItem(weightContainer, getString(R.string.profile_item_weight), String.valueOf(mRegister.getPeso()));

        View ageContainer = headerView.findViewById(R.id.header_profile_age);
        setProfileItem(ageContainer, getString(R.string.profile_item_age), String.valueOf(mRegister.getAge()));

        headerView.findViewById(R.id.register_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                intent.putExtra(Constants.EXTRA_REGISTER_MODEL, mRegister);
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
            mRegister = (RegisterResponse) data.getSerializableExtra(Constants.EXTRA_REGISTER_MODEL);
            setHeader(mHeaderView);
        } else if (requestCode == Constants.REQ_EDIT_DIET && resultCode == Activity.RESULT_OK) {
            ((MainActivity) getActivity()).setSuggestedDiet((SuggestedDietResponse) data.getSerializableExtra(Constants.EXTRA_DIET));
            mAdapter.setDiet(((MainActivity) getActivity()).getSuggestedDiet().getNutrients());
        }
    }
}
