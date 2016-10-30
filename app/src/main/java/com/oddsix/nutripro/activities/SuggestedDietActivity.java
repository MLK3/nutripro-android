package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.DietAdapter;
import com.oddsix.nutripro.models.DietUnitModel;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Filippe on 22/10/16.
 */

public class SuggestedDietActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        setListView();
    }

    private void setListView() {
        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(new DietAdapter(this, new RealmList<DietUnitModel>()));

        list.addHeaderView(setHeader());
        list.addFooterView(setFooter());
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
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
