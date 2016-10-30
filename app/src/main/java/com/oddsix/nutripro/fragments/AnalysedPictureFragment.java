package com.oddsix.nutripro.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.oddsix.nutripro.BaseFragment;
import com.oddsix.nutripro.R;
import com.oddsix.nutripro.adapters.AnalysedImgAdapter;

/**
 * Created by Filippe on 21/10/16.
 */

public class AnalysedPictureFragment extends BaseFragment {
    private AnalysedImgAdapter mAnalysedImgAdapter;
    private View mHeaderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        setListView(view, inflater);
        return view;
    }

    private void setListView(View view, LayoutInflater inflater) {
        mAnalysedImgAdapter = new AnalysedImgAdapter(getActivity(), new AnalysedImgAdapter.OnNutrientClickListener() {
            @Override
            public void onEditValueClicked(int position) {
                functionNotImplemented();
            }

            @Override
            public void onEditNameClicked(int position) {
                functionNotImplemented();
            }

            @Override
            public void onEditInfoClicked(int position) {
                functionNotImplemented();
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(mAnalysedImgAdapter);
        mHeaderView = inflateHeader(inflater);
        listView.addHeaderView(mHeaderView);
        listView.addFooterView(inflateFooter(inflater));
    }

    private View inflateHeader(LayoutInflater inflater) {
        View headerView = inflater.inflate(R.layout.header_analysed_photo, null);
        return headerView;
    }

    private View inflateFooter(LayoutInflater inflater) {
        View footerView = inflater.inflate(R.layout.footer_analysed_photo, null);
        ((Button) footerView.findViewById(R.id.footer_analysed_photo_conclude)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionNotImplemented();
            }
        });
        return footerView;
    }

    public void setImage(Bitmap image) {
        ((ImageView) mHeaderView.findViewById(R.id.header_analysed_photo_img)).setImageBitmap(image);
    }
}
