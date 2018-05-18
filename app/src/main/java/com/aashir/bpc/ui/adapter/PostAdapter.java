package com.aashir.bpc.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.DetailImageActivity;
import com.aashir.bpc.ui.DetailPostActivity;
import com.aashir.bpc.ui.adapter.viewholder.PostCardViewHolder;
import com.aashir.bpc.ui.model.PostCard;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int TYPE_IMAGE = 0;
    private int TYPE_NO_IMAGE = 1;

    private RequestManager mRequestManager;
    private ArrayList<PostCard> mCards;
    private Context mContext;
    private Resources mResources;

    public PostAdapter(RequestManager req, Context context, ArrayList<PostCard> c) {
        mRequestManager = req;
        mContext = context;
        mCards = c;
        mResources = mContext.getResources();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;

        if (mCards.get(position).getHasImage()) {
            viewType = TYPE_IMAGE;
        } else {
            viewType = TYPE_NO_IMAGE;
        }

        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == TYPE_IMAGE) {
            return new PostCardViewHolder(mInflater.inflate(R.layout.card_image, viewGroup, false));
        } else {
            return new PostCardViewHolder(mInflater.inflate(R.layout.card_no_image, viewGroup, false));
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final PostCard card = mCards.get(position);
        final PostCardViewHolder holder = (PostCardViewHolder) viewHolder;
        holder.mAuthor.setText(card.getAuthor());
        holder.mContent.setText(card.getContent());
        holder.mTime.setText(card.getTime());
        mRequestManager.load(card.getProfilePicture()).placeholder(R.drawable.placeholder_user).into(holder.mProfilePicture);

        if (getItemViewType(position) == TYPE_IMAGE) {
            mRequestManager.load(card.getImageThumb()).placeholder(R.drawable.placeholder_image).centerCrop().into(holder.mImage);
            holder.mImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(mContext, DetailImageActivity.class);
                    i.putExtra("image_id", mCards.get(position).getImageID());
                    mContext.startActivity(i);
                }

            });
        }

        if (mCards.get(position).getLike()) {
            holder.mLike.setBackground(ResourcesCompat.getDrawable(mResources, R.drawable.social_selected, null));
            holder.mLike.setImageDrawable(ResourcesCompat.getDrawable(mResources, R.drawable.ic_thumb_up_white_18dp, null));
        }

        holder.mCard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                start(position);
            }
        });

        holder.mLike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCards.get(position).getLike()) {
                    mCards.get(position).setLike(false);
                    holder.mLike.setBackground(ResourcesCompat.getDrawable(mResources, R.drawable.social_unselected, null));
                    holder.mLike.setImageDrawable(ResourcesCompat.getDrawable(mResources, R.drawable.ic_thumb_up_grey600_18dp, null));
                } else {
                    mCards.get(position).setLike(true);
                    holder.mLike.setBackground(ResourcesCompat.getDrawable(mResources, R.drawable.social_selected, null));
                    holder.mLike.setImageDrawable(ResourcesCompat.getDrawable(mResources, R.drawable.ic_thumb_up_white_18dp, null));
                }
            }
        });

        holder.mComment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                start(position);
            }
        });

        holder.mShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, card.getContent()
                        + "\n" + "~" + card.getAuthor());
                mContext.startActivity(Intent.createChooser(sharingIntent, "Share to"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void redoAll(ArrayList<PostCard> cards) {
        this.mCards = cards;
        this.notifyDataSetChanged();
        // for adding cards to bottom of list
        //this.notifyItemRangeChanged(0, cards.size());
    }

    private void start(int pos) {
        final Intent i = new Intent(mContext, DetailPostActivity.class);

        PostCard mCard = mCards.get(pos);
        i.putExtra("author", mCard.getAuthor());
        i.putExtra("content", mCard.getContent());
        i.putExtra("time", mCard.getTime());
        i.putExtra("image_thumb_url", mCard.getImageThumb());
        i.putExtra("image_id", mCard.getImageID());
        i.putExtra("pp", mCard.getProfilePicture());
        i.putExtra("hasImage", mCard.getHasImage());
        i.putExtra("objectid", mCard.getObjectID());
        i.putExtra("like", mCard.getLike());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContext.startActivity(i);
            }
        }, 50);
    }

}