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
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.NutrientResponse;
import com.oddsix.nutripro.rest.models.responses.SearchResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;

import java.util.ArrayList;
import java.util.List;

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
    private View.OnClickListener mOnTryAgainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendRequest(mSearchView.getQuery().toString());
        }
    };

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
                if (response.getFoods().isEmpty()) {
                    mFeedbackHelper.showEmptyPlaceHolder();
                } else {
                    mAdapter.setResults(response.getFoods());
                }
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
                List<NutrientModel> nutrientModels = new ArrayList<NutrientModel>();
                for (NutrientResponse nutrient : mSearchResponse.getFoods().get(i).getNutrients()) {
                    nutrientModels.add(new NutrientModel(nutrient.getName(), nutrient.getQuantity(), nutrient.getUnit()));
                }

                intent.putExtra(Constants.EXTRA_FOOD, new FoodModel(nutrientModels, mSearchResponse.getFoods().get(i).getName(), 0, mSearchResponse.getFoods().get(i).getPortion()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setSearchView() {
        mSearchView = (SearchView) findViewById(R.id.search_sv);
        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendRequest(query);
                hideSoftKeyboard();
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
        if (resultCode == RESULT_OK && requestCode == Constants.REQ_REGISTER_FOOD) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_FOOD, (FoodModel) data.getSerializableExtra(Constants.EXTRA_FOOD));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
