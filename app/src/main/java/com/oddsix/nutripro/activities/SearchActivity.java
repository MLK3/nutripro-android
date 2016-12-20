package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.SearchAdapter;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.FoodResponse;
import com.oddsix.nutripro.rest.models.responses.SearchResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;

/**
 * Created by filippecl on 20/11/16.
 */

public class SearchActivity extends BaseActivity {
    private SearchView mSearchView;
    private SearchAdapter mAdapter;
    private NutriproProvider mNutriproProvider;
    private SearchResponse mSearchResponse;
    private FeedbackHelper mFeedbackHelper;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbar(true, getString(R.string.search_activity_title));

        mNutriproProvider = new NutriproProvider(this);


        setClick();

        mFeedbackHelper = new FeedbackHelper(this, (ViewGroup) findViewById(R.id.container), mOnTryAgainClickListener);

        setListView();

        setSearchView();
    }

    private View.OnClickListener mOnTryAgainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendRequest(mSearchView.getQuery().toString());
        }
    };

    private void setClick() {
        findViewById(R.id.search_register_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewFoodActivity.class);
                startActivityForResult(intent, Constants.REQ_REGISTER_FOOD);
            }
        });
    }

    private void sendRequest(String query) {
        mFeedbackHelper.startLoading();
        mNutriproProvider.searchFood(query, new NutriproProvider.OnResponseListener<SearchResponse>() {
            @Override
            public void onResponseSuccess(SearchResponse response) {
                mFeedbackHelper.dismissFeedback();
                if(response.getFoods().isEmpty()) {
                    mFeedbackHelper.showEmptyPlaceHolder();
                }
                mAdapter.setResults(response.getFoods());
                mSearchResponse = response;
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                mFeedbackHelper.showErrorPlaceHolder();
            }
        });
    }

    private void setListView() {
        ListView list = (ListView) findViewById(R.id.listview);
        mAdapter = new SearchAdapter(this);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_FOOD, mSearchResponse.getFoods().get(i));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setSearchView(){
        mSearchView = (SearchView) findViewById(R.id.search_sv);
        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendRequest(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Constants.REQ_REGISTER_FOOD) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_FOOD, (FoodResponse) data.getSerializableExtra(Constants.EXTRA_FOOD));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
