package com.aashir.bpc.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.viewholder.TeacherCardViewHolder;
import com.aashir.bpc.ui.model.TeacherCard;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private RequestManager mRequestManager;
    private ArrayList<TeacherCard> mCards;

    public TeacherAdapter(Context con, RequestManager req, ArrayList<TeacherCard> c) {
        mContext = con;
        mRequestManager = req;
        mCards = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View vImage = mInflater.inflate(R.layout.card_teacher, viewGroup, false);

        return new TeacherCardViewHolder(vImage);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final TeacherCard card = mCards.get(position);
        TeacherCardViewHolder holder = (TeacherCardViewHolder) viewHolder;
        holder.mName.setText(card.getName());
        holder.mSubject.setText(card.getSubject());
        holder.mPhone.setText(card.getPhone());
        holder.mEmail.setText(card.getEmail());
        mRequestManager.load(card.getProfilePicture()).placeholder(R.drawable.placeholder_user).into(holder.mProfilePicture);

        holder.mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + card.getPhone().replaceAll("[^0-9]", "")));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, "No Phone app found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", card.getEmail(), null));
                mContext.startActivity(Intent.createChooser(emailIntent, "Send using"));
            }
        });
    }

    public void redoAll(ArrayList<TeacherCard> cards) {
        this.mCards = cards;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

}