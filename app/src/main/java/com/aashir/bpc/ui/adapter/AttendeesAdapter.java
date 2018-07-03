package com.aashir.bpc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.UserCard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AttendeesAdapter extends BaseAdapter {

    private ArrayList<UserCard> mCards;
    private Context mContext;
    private LayoutInflater mInflater;

    public AttendeesAdapter(Context context, ArrayList<UserCard> data) {
        super();
        this.mCards = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public Object getItem(int position) {
        return mCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_user, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        UserCard mCard = mCards.get(position);
        mHolder.mName.setText(mCard.getName());
        Glide.with(mContext).load(mCard.getProfilePicture()).placeholder(R.drawable.placeholder_user).into(mHolder.mPP);

        return convertView;
    }

    public static class ViewHolder {

        TextView mName;
        ImageView mPP;

        public ViewHolder(View v) {
            mName = (TextView) v.findViewById(R.id.user_name);
            mPP = (ImageView) v.findViewById(R.id.user_profilepicture);
        }
    }

}