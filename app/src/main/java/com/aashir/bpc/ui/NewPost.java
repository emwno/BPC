package com.aashir.bpc.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.util.CropTransformation;
import com.aashir.bpc.util.UploadService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.io.ByteArrayOutputStream;

public class NewPost extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEditText;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEditText = (EditText) findViewById(R.id.new_post_edittext);
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newpost_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            final ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this).load(data.getData()).asBitmap().transform(new CropTransformation(this))
                    .into(new ImageViewTarget<Bitmap>(imageView) {
                @Override
                protected void setResource(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                    mBitmap = bitmap;
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) mBitmap.recycle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_send:
                if (mEditText.getText().length() == 0) {
                    Toast.makeText(this, "Enter text", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "You can track the post progress in your notifications", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, UploadService.class);
                    i.putExtra("class", getIntent().getStringExtra("class"));
                    i.putExtra("content", mEditText.getText().toString());

                    if (mBitmap != null) {
                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
                        i.putExtra("byteArray", bs.toByteArray());
                    }

                    startService(i);
                    onBackPressed();
                }
                break;

            case R.id.action_pick:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }

        return true;
    }

}
