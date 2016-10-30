package com.oddsix.nutripro.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oddsix.nutripro.BaseActivity;
import com.oddsix.nutripro.BuildConfig;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.utils.helpers.DialogHelper;

/**
 * Created by filippecl on 30/10/16.
 */

public class SettingsActivity extends BaseActivity {
    private DialogHelper mDialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar(true, getString(R.string.settings_title));

        mDialogHelper = new DialogHelper(this);

        setVersionCode();

        setButtonClicklistener();
    }

    private void setVersionCode() {
        ((TextView) findViewById(R.id.settings_version_name_tv)).setText(getString(R.string.settings_version, BuildConfig.VERSION_NAME));
    }

    private void setButtonClicklistener() {
        ((Button) findViewById(R.id.settings_logout_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogHelper.showAlertDialog(getString(R.string.settings_logout_dialog_title), getString(R.string.settings_logout_dialog_positive),
                        getString(R.string.settings_logout_dialog_negative), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startLoginActivity();
                            }
                        });
            }
        });
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
