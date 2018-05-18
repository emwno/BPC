package com.aashir.bpc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aashir.bpc.R;

public class DetailAssignment extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mToggle;

    private int mTabNo;
    private int mPosition;
    private boolean mComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_assignment_activity);

        Bundle mAssignment = getIntent().getExtras();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mSubject = (TextView) findViewById(R.id.detail_assignment_subject);
        TextView mTitle = (TextView) findViewById(R.id.detail_assignment_title);
        TextView mTime = (TextView) findViewById(R.id.detail_assignment_time);
        TextView mContent = (TextView) findViewById(R.id.detail_assignment_content);
        TextView mTeacher = (TextView) findViewById(R.id.detail_assignment_teacher);
        TextView mDueDate = (TextView) findViewById(R.id.detail_assignment_due_date);
        TextView mPostDate = (TextView) findViewById(R.id.detail_assignment_post_date);
        mToggle = (FloatingActionButton) findViewById(R.id.detail_assignment_toggle);

        setActionBar();

        mSubject.setText(mAssignment.getString("subject"));
        mTitle.setText(mAssignment.getString("title"));
        mTime.setText(mAssignment.getString("time"));
        mContent.setText(mAssignment.getString("content"));
        mTeacher.setText(mAssignment.getString("teacher"));
        mDueDate.setText(mAssignment.getString("due_time"));
        mPostDate.setText(mAssignment.getString("post_time"));
        mToggle.setOnClickListener(this);

        mComplete = mAssignment.getBoolean("is_completed");
        mPosition = mAssignment.getInt("position");
        mTabNo = mAssignment.getInt("tab_no");
        if (mComplete) {
            mToggle.setImageResource(R.drawable.ic_close_white_18dp);
        }
    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar aB = getSupportActionBar();
        aB.setDisplayHomeAsUpEnabled(true);
        aB.setDisplayShowTitleEnabled(false);
        aB.setDisplayUseLogoEnabled(false);
        aB.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (mComplete) {
            mComplete = false;
            mToggle.setImageResource(R.drawable.ic_check_white_18dp);
        } else {
            mComplete = true;
            mToggle.setImageResource(R.drawable.ic_close_white_18dp);
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", mPosition);
        returnIntent.putExtra("tab_no", mTabNo);
        setResult(777, returnIntent);
        finish();
    }
}
