package com.aashir.bpc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.util.RecyclerViewListener;

public class BulletinFragment extends BaseBulletinFragment implements View.OnClickListener {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final View fab = ((ViewStub) view.findViewById(R.id.fab_stub)).inflate();
        final int fabMargin = getResources().getDimensionPixelSize(R.dimen.big_padding);

        mListView.addOnScrollListener(new RecyclerViewListener() {

            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), NewPost.class);
        i.putExtra("class", Key.Class.BULLETIN);
        startActivity(i);
    }
}