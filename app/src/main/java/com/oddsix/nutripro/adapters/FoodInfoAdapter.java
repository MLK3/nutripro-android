package com.oddsix.nutripro.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oddsix.nutripro.R;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.rest.models.responses.NutrientResponse;

import java.util.List;

/**
 * Created by filippecl on 06/11/16.
 */

public class FoodInfoAdapter extends BaseAdapter {
    private List<NutrientModel> mNutrients;
    private Context mContext;

    public FoodInfoAdapter(List<NutrientModel> nutrients, Context context) {
        mNutrients = nutrients;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mNutrients.size();
    }

    @Override
    public Object getItem(int i) {
        return mNutrients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FoodInfoViewHolder viewHolder;

        if (view == null) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.item_food_info, viewGroup, false);

        viewHolder = new FoodInfoViewHolder();
        viewHolder.name = (TextView) view.findViewById(R.id.food_info_nutrient_name_tv);
        viewHolder.quantity = (TextView) view.findViewById(R.id.food_info_nutrient_quantity_tv);

        // store the holder with the view.
        view.setTag(viewHolder);

        } else {
        // we've just avoided calling findViewById() on resource everytime
        // just use the viewHolder
            viewHolder = (FoodInfoViewHolder) view.getTag();
        }


        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.name.setText(mNutrients.get(i).getName());
        viewHolder.quantity.setText(mContext.getString(R.string.food_info_quantity,
                mNutrients.get(i).getQuantity(), mNutrients.get(i).getUnit()));
//        viewHolder.value.setText(mContext.getString(R.string.diet_item_range_label,
//                diet.get(i).getMin(), diet.get(i).getMax(), diet.get(i).getUnit()));

        return view;

    }

    static class FoodInfoViewHolder {
        TextView name;
        TextView quantity;
    }
}
