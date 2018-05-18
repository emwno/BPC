package com.aashir.bpc.intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.aashir.bpc.R;

public class ChoiceAdapter extends BaseAdapter {

    private String[] mDataset;
    private Context mContext;
    private int mLayout;

    public ChoiceAdapter(Context c, int layout, String[] data) {
        mContext = c;
        mLayout = layout;
        mDataset = data;
    }

    @Override
    public int getCount() {
        return mDataset.length;
    }

    @Override
    public Object getItem(int pos) {
        return mDataset[pos];
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mLayout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mChecked = (CheckedTextView) view.findViewById(R.id.intro_subject_checktv);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mChecked.setText(mDataset[position]);

        return view;
    }

    static class ViewHolder {
        CheckedTextView mChecked;
    }


}
