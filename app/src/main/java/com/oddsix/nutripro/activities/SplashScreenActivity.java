package com.oddsix.nutripro.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.rest.NutriproProvider;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.FeedbackHelper;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

/**
 * Created by filippecl on 17/12/16.
 */

public class SplashScreenActivity extends BaseActivity {

    private NutriproProvider mProvider;
    private FeedbackHelper mFeedbackHelper;
    private Activity mActivity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProvider = new NutriproProvider(this);

        if (!SharedPreferencesHelper.getInstance().isLogged()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            mFeedbackHelper = new FeedbackHelper(this, (LinearLayout) findViewById(R.id.container), mOnTryAgainClickListener);
            sendRequest();
        }
    }

    private View.OnClickListener mOnTryAgainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mFeedbackHelper.dismissFeedback();
            sendRequest();
        }
    };

    private void sendRequest() {
        mProvider.getDiet(new NutriproProvider.OnResponseListener<SuggestedDietResponse>() {
            @Override
            public void onResponseSuccess(SuggestedDietResponse response) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra(Constants.EXTRA_DIET, response);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponseFailure(String msg, int code) {
                mFeedbackHelper.showErrorPlaceHolder();
            }
        });
    }
}
