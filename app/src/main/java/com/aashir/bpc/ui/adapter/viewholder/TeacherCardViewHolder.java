package com.aashir.bpc.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.R;

public class TeacherCardViewHolder extends RecyclerView.ViewHolder {

    public TextView mName;
    public ImageView mProfilePicture;
    public TextView mSubject;
    public TextView mPhone;
    public TextView mEmail;

    public TeacherCardViewHolder(View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.teacher_name);
        mProfilePicture = (ImageView) itemView.findViewById(R.id.teacher_profilepicture);
        mSubject = (TextView) itemView.findViewById(R.id.teacher_subject);
        mPhone = (TextView) itemView.findViewById(R.id.teacher_phone);
        mEmail = (TextView) itemView.findViewById(R.id.teacher_email);
    }

}