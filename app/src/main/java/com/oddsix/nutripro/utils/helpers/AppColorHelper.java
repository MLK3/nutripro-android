package com.oddsix.nutripro.utils.helpers;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.oddsix.nutripro.R;

/**
 * Created by filippecl on 18/12/16.
 */

public class AppColorHelper {
    private Context mContext;
    private int[] mAppColors;
    private int mCounter = 0;

    public AppColorHelper(Context context) {
        mContext = context;
        mAppColors = context.getResources().getIntArray(R.array.app_colors);
    }

    public int getNextColor() {
        int color = mAppColors[mCounter];
        addOneToCounter();
        return color;
    }

    public int getColorAtIndex(int index) {
        if(index > mAppColors.length){
            index = index % mAppColors.length;
        }
        int color = mAppColors[index];
        addOneToCounter();
        return color;
    }

    private void addOneToCounter() {
        if (mCounter + 1 < mAppColors.length - 1) {
            mCounter++;
        } else {
            mCounter = 0;
        }
    }

    public int getMaximumNumberOfColors() {
        return mAppColors.length;
    }
}
