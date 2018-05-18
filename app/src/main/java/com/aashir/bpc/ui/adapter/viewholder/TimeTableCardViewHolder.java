package com.aashir.bpc.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aashir.bpc.R;

public class TimeTableCardViewHolder extends RecyclerView.ViewHolder {

    public TextView mSubject;
    public TextView mTime;

    public TimeTableCardViewHolder(View itemView) {
        super(itemView);
        mSubject = (TextView) itemView.findViewById(R.id.timetable_subject);
        mTime = (TextView) itemView.findViewById(R.id.timetable_time);
    }

}