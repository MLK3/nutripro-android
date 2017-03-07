package com.oddsix.nutripro.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.rest.models.responses.DietNutrientResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filippe on 22/10/16.
 */

public class DietAdapter extends BaseAdapter {
    private List<DietNutrientResponse> diet = new ArrayList<>();
    private Context mContext;

    public DietAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDiet(List<DietNutrientResponse> diet) {
        this.diet = diet;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return diet.size();
    }

    @Override
    public Object getItem(int i) {
        return diet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DietViewHolder viewHolder;

        if (view == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(R.layout.item_diet, viewGroup, false);

            viewHolder = new DietViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.item_diet_name);
//            viewHolder.maxUnit = (TextView) view.findViewById(R.id.item_diet_max_unit);
            viewHolder.minUnit = (TextView) view.findViewById(R.id.item_diet_min_unit);
            viewHolder.minValue = (TextView) view.findViewById(R.id.item_diet_max_value);
            viewHolder.maxValue = (TextView) view.findViewById(R.id.item_diet_min_value);

            // store the holder with the view.
            view.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (DietViewHolder) view.getTag();
        }


        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.name.setText(diet.get(i).getName());
//        viewHolder.maxUnit.setText(diet.get(i).getUnit());
        viewHolder.minUnit.setText(diet.get(i).getUnit());
        viewHolder.maxValue.setText(String.valueOf(diet.get(i).getMax()));
        viewHolder.minValue.setText(String.valueOf(diet.get(i).getMin()));

        return view;

    }

    static class DietViewHolder {
        TextView name;
        TextView maxValue;
        TextView minValue;
//        TextView maxUnit;
        TextView minUnit;
    }
}
