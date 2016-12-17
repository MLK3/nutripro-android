package com.oddsix.nutripro.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.FoodModel;
import com.oddsix.nutripro.rest.models.responses.FoodResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 30/10/16.
 */

public class AnalysedImgAdapter extends BaseAdapter {
    private Context mContext;
    private OnNutrientClickListener mOnNutrientClickListener;
    private List<FoodResponse> mFoods = new ArrayList<>();

    public AnalysedImgAdapter(Context context, OnNutrientClickListener onNutrientClickListener) {
        mContext = context;
        mOnNutrientClickListener = onNutrientClickListener;
    }

    public void setFoods(List<FoodResponse> foods) {
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
            viewHolder.name = (EditText) view.findViewById(R.id.analysed_photo_name_et);
            viewHolder.value = (EditText) view.findViewById(R.id.analysed_photo_value_et);
            viewHolder.editName = (ImageView) view.findViewById(R.id.analysed_photo_edit_name_ic);
            viewHolder.editValue = (ImageView) view.findViewById(R.id.analysed_photo_edit_value_ic);
            viewHolder.info = (ImageView) view.findViewById(R.id.analysed_photo_info_ic);

            viewHolder.editName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNutrientClickListener.onEditNameClicked(i);
                }
            });

            viewHolder.editValue.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.value.setText(String.valueOf(mFoods.get(i).getQuantity()));

        return view;

    }

    static class NutrientsViewHolder {
        EditText name;
        EditText value;
        ImageView editName;
        ImageView editValue;
        ImageView info;
    }

    public interface OnNutrientClickListener{
        void onEditValueClicked(int position);
        void onEditNameClicked(int position);
        void onEditInfoClicked(int position);
    }
}
