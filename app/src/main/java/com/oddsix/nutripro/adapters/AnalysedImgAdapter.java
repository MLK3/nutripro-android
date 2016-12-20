package com.oddsix.nutripro.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.rest.models.responses.RecognisedFoodResponse;
import com.oddsix.nutripro.utils.helpers.AppColorHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 30/10/16.
 */

public class AnalysedImgAdapter extends BaseAdapter {
    private Context mContext;
    private OnNutrientClickListener mOnNutrientClickListener;
    private List<RecognisedFoodResponse> mFoods = new ArrayList<>();
    private AppColorHelper mAppColorHelper;

    public AnalysedImgAdapter(Context context, OnNutrientClickListener onNutrientClickListener) {
        mContext = context;
        mOnNutrientClickListener = onNutrientClickListener;
        mAppColorHelper = new AppColorHelper(context);
    }

    public void setFoods(List<RecognisedFoodResponse> foods) {
        mFoods = foods;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFoods.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        NutrientsViewHolder viewHolder;

        if (view == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(R.layout.item_analysed_photo, viewGroup, false);

            viewHolder = new NutrientsViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.analysed_photo_name_et);
            viewHolder.underline = view.findViewById(R.id.analysed_photo_name_underline);
            viewHolder.value = (TextView) view.findViewById(R.id.analysed_photo_value_et);
            viewHolder.info = (ImageView) view.findViewById(R.id.analysed_photo_info_ic);

            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNutrientClickListener.onEditNameClicked(i);
                }
            });

            viewHolder.value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNutrientClickListener.onEditValueClicked(i);
                }
            });

            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNutrientClickListener.onEditInfoClicked(i);
                }
            });

            // store the holder with the view.
            view.setTag(viewHolder);

        } else {
//             we've just avoided calling findViewById() on resource everytime
//             just use the viewHolder
            viewHolder = (NutrientsViewHolder) view.getTag();
        }

        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.name.setText(mFoods.get(i).getName());
        viewHolder.value.setText(String.valueOf(mFoods.get(i).getQuantity()) + "g");
        if(mFoods.get(i).getArea() != null){
            viewHolder.underline.setBackgroundColor(mAppColorHelper.getColorAtIndex(i));
        }
        return view;

    }

    static class NutrientsViewHolder {
        TextView name;
        TextView value;
        View underline;
        ImageView info;
    }

    public interface OnNutrientClickListener{
        void onEditValueClicked(int position);
        void onEditNameClicked(int position);
        void onEditInfoClicked(int position);
    }
}
