package com.oddsix.nutripro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioGroup;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.R;

/**
 * Created by filippecl on 03/12/16.
 */

public class ActivityLevelActivity extends BaseActivity {

    private RadioGroup mActivityLevelRg;

    public static final int UNCHECKED_RG_ID = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_level);
        setToolbar(true, getString(R.string.activity_level_title));
        mActivityLevelRg = (RadioGroup) findViewById(R.id.rg_activity_level);

    }

    public void onSendClicked(View view) {
        if(mActivityLevelRg.getCheckedRadioButtonId() != UNCHECKED_RG_ID) {
            startSuggestedDietActivity();
        } else {
//            showToast(getString(R.string.activity_level_error_not_selected));
        }
    }

    private void startSuggestedDietActivity(){
        Intent intent = new Intent(this, SuggestedDietActivity.class);
        startActivity(intent);
        finish();
    }
}
