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

import io.realm.RealmList;

/**
 * Created by Filippe on 22/10/16.
 */

public class DietAdapter extends BaseAdapter {
    private RealmList<NutrientModel> diet = new RealmList<>();
    private Context mContext;

    public DietAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDiet(RealmList<NutrientModel> diet) {
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
            viewHolder.value = (TextView) view.findViewById(R.id.item_diet_value);

            // store the holder with the view.
            view.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (DietViewHolder) viewGroup.getTag();
        }


        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.name.setText(diet.get(i).getName());
        viewHolder.value.setText(mContext.getString(R.string.diet_item_range_label,
                diet.get(i).getMin(), diet.get(i).getMax(), diet.get(i).getUnit()));

        return view;

    }

    static class DietViewHolder {
        TextView name;
        TextView value;
    }
}
