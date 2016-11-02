package com.oddsix.nutripro.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oddsix.nutripro.R;

/**
 * Created by filippecl on 02/11/16.
 */

public class DayResumeAdapter extends BaseAdapter {
    Context mContext;

    public DayResumeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        DietAdapter.DietViewHolder viewHolder;

        if (view == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(R.layout.item_day_resume, viewGroup, false);

            viewHolder = new DietAdapter.DietViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.item_day_resume_meal);

            // store the holder with the view.
            view.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (DietAdapter.DietViewHolder) viewGroup.getTag();
        }


        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
//        viewHolder.name.setText(diet.get(i).getName());
//        viewHolder.value.setText(mContext.getString(R.string.diet_item_range_label,
//                diet.get(i).getMin(), diet.get(i).getMax(), diet.get(i).getUnit()));

        return view;

    }

    static class DayResumeViewHolder {
        TextView name;
    }
}
