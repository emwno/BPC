package com.aashir.bpc.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.Key;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

public class UploadService extends IntentService {

    private String mImageID;
    private String mClass;
    private String mContent;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    public UploadService() {
        super("UploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mClass = intent.getStringExtra("class");
        mContent = intent.getStringExtra("content");
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Posting...").setSmallIcon(R.drawable.ic_launcher).setOngoing(true);

        if (intent.getByteArrayExtra("byteArray") != null) {
            withImages(intent.getByteArrayExtra("byteArray"));
        } else {
            withoutImages(null);
        }
    }

    private void withoutImages(ParseFile file) {
        ParseObject mObject = new ParseObject(mClass);
        mObject.put(Key.Post.AUTHOR, ParseUser.getCurrentUser());
        mObject.put(Key.Post.TIME, System.currentTimeMillis());
        mObject.put(Key.Post.CONTENT, mContent);
        if (file != null) mObject.put(Key.Post.IMAGE_THUMB, file);
        if (mImageID != null) mObject.put(Key.Post.IMAGE, mImageID);
        mObject.saveInBackground(mObjectSaveCallback);
    }

    private void withImages(final byte[] array) {
        mBuilder.setContentText("Uploading Image");

        ParseFile file = new ParseFile("image.png", array);
        file.saveInBackground(mProgressCallback);

        final ParseObject mImage = new ParseObject(Key.Class.IMAGES);
        mImage.put(Key.Image.IMAGE, file);
        mImage.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mBuilder.setContentText("Image Upload Complete").setProgress(0, 0, false);
                    mNotifyManager.notify(42, mBuilder.build());
                    mImageID = mImage.getObjectId();
                    Glide.with(UploadService.this).load(array)
                            .asBitmap()
                            .toBytes(Bitmap.CompressFormat.JPEG, 85)
                            .transform(new CropSquareTransformation(UploadService.this))
                            .into(mTarget);
                } else {
                    mBuilder.setContentTitle("Error").setContentText(e.getMessage()).setOngoing(false);
                    mNotifyManager.notify(42, mBuilder.build());
                    Toast.makeText(UploadService.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    SimpleTarget mTarget = new SimpleTarget<byte[]>() {

        @Override
        public void onResourceReady(byte[] bitmap, GlideAnimation glideAnimation) {
            mBuilder.setContentText("Uploading Thumbnail");

            final ParseFile file = new ParseFile("thumb.png", bitmap);
            file.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        mBuilder.setContentText("Thumbnail Upload Complete").setProgress(0, 0, false);
                        withoutImages(file);
                    } else {
                        mBuilder.setContentTitle("Error").setContentText(e.getMessage()).setOngoing(false);
                        Toast.makeText(UploadService.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    mNotifyManager.notify(42, mBuilder.build());

                }

            }, mProgressCallback);
        }
    };

    ProgressCallback mProgressCallback = new ProgressCallback() {

        public void done(Integer percentDone) {
            mBuilder.setProgress(100, percentDone, false);
            mNotifyManager.notify(42, mBuilder.build());
        }
    };

    SaveCallback mObjectSaveCallback = new SaveCallback() {

        @Override
        public void done(ParseException e) {
            if (e == null) {
                ParsePush push = new ParsePush();
                push.setChannel(mClass);
                push.setMessage(ParseUser.getCurrentUser().getString("name") + " posted an update");
                push.sendInBackground();
                mBuilder.setContentTitle("Done").setContentText("Update has been posted.").setOngoing(false);
            } else {
                Toast.makeText(UploadService.this, e.getMessage(), Toast.LENGTH_LONG).show();
                mBuilder.setContentTitle("Error").setContentText(e.getMessage()).setOngoing(false);
            }

            mNotifyManager.notify(42, mBuilder.build());
        }

    };

}
