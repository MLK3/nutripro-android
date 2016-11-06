package com.oddsix.nutripro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.ListView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.utils.Constants;

import io.realm.Realm;

/**
 * Created by Filippe on 22/10/16.
 */

public class SuggestedDietActivity extends BaseActivity {
    private DBDietModel mSuggestedDiet;
    private DietAdapter mAdapter;
    private Realm mRealm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        mRealm = Realm.getDefaultInstance();

        setListView();

        setSuggestedDiet();
    }

    private void setListView() {
        ListView list = (ListView) findViewById(R.id.listview);
        mAdapter = new DietAdapter(this);
        list.setAdapter(mAdapter);
        list.addHeaderView(setHeader());
        list.addFooterView(setFooter());
    }

    private void setSuggestedDiet() {
        mSuggestedDiet = Realm.getDefaultInstance().where(DBDietModel.class)
                .findFirst();
        if (mSuggestedDiet != null) {
            mAdapter.setDiet(mSuggestedDiet.getDiet());
        }

    }

    private View setHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.header_suggested_diet, null);
        return headerView;
    }

    private View setFooter() {
        View footerView = getLayoutInflater().inflate(R.layout.footer_suggested_diet, null);
        footerView.findViewById(R.id.footer_suggested_diet_conclude).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBRegisterModel register = mRealm.where(DBRegisterModel.class)
                        .equalTo("mail", getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE).getString(Constants.PREF_MAIL, ""))
                        .findFirst();

                mRealm.beginTransaction();
                if (register != null) register.setDietModel(mSuggestedDiet);
                mRealm.commitTransaction();

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        footerView.findViewById(R.id.footer_suggested_diet_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        return footerView;
    }
}
