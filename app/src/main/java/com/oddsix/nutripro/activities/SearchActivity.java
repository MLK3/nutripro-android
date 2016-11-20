package com.oddsix.nutripro.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ListView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.BasicStringAdapter;
import com.oddsix.nutripro.adapters.DayResumeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 20/11/16.
 */

public class SearchActivity extends BaseActivity {
    private SearchView mSearchView;
    private BasicStringAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbar(true, getString(R.string.search_activity_title));

        setListView();

        setSearchView();
        setMock();

    }

    private void setListView() {
        ListView list = (ListView) findViewById(R.id.listview);
        mAdapter = new BasicStringAdapter(this);
        list.setAdapter(mAdapter);
    }

    private void setMock() {
        List<String> strings = new ArrayList<>();
        strings.add("Lasagna");
        strings.add("Couve");
        strings.add("Frango");
        strings.add("Maminha assada");
        strings.add("Brócolis");
        mAdapter.setStrings(strings);
    }

    private void setSearchView(){
        mSearchView = (SearchView) findViewById(R.id.search_sv);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo search
            }
        });
    }
}
