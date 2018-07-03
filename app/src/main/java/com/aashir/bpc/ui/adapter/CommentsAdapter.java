package com.aashir.bpc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.CommentCard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {

    private ArrayList<CommentCard> mComments;
    private Context mContext;
    private LayoutInflater mInflater;

    public CommentsAdapter(Context context, ArrayList<CommentCard> data) {
        super();
        this.mComments = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_comment, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        CommentCard mCard = mComments.get(position);
        mHolder.mAuthor.setText(mCard.getAuthor());
        mHolder.mContent.setText(mCard.getContent());
        mHolder.mTime.setText(mCard.getTime());
        Glide.with(mContext).load(mCard.getProfilePicture()).placeholder(R.drawable.placeholder_user).into(mHolder.mPP);

        return convertView;
    }

    public static class ViewHolder {

        TextView mAuthor;
        TextView mContent;
        TextView mTime;
        ImageView mPP;

        public ViewHolder(View v) {
            mAuthor = (TextView) v.findViewById(R.id.comment_author);
            mContent = (TextView) v.findViewById(R.id.comment_content);
            mTime = (TextView) v.findViewById(R.id.comment_time);
            mPP = (ImageView) v.findViewById(R.id.comment_profilepicture);
        }
    }

}