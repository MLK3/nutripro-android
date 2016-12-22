package com.oddsix.nutripro;


import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.oddsix.nutripro.utils.ViewUtil;
import com.oddsix.nutripro.utils.helpers.DialogHelper;

/**
 * Created by Filippe on 21/10/16.
 */

public class BaseFragment extends Fragment {
    private DialogHelper mDialogHelper;

    public void showProgressdialog() {
        mDialogHelper = (mDialogHelper == null) ? new DialogHelper(getActivity()) : mDialogHelper;
        mDialogHelper.showProgressDialog();
    }

    public void dismissProgressDialog() {
        if (mDialogHelper != null) {
            mDialogHelper.dismissProgressDialog();
        }
    }

    public void functionNotImplemented(){
        ((BaseActivity) getActivity()).functionNotImplemented();
    }

    public void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

}
