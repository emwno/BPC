package com.aashir.bpc.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class CropTransformation extends BitmapTransformation {

        int mWidth;
        int mHeight;

        public CropTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source,
                                   int outWidth, int outHeight) {
            mWidth = source.getWidth();
            mHeight = source.getHeight();

            int maxHeight = 900;
            int maxWidth = 800;

            float ratioBitmap = (float) mWidth / (float) mHeight;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;

            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }

            source = Bitmap.createScaledBitmap(source, finalWidth, finalHeight, true);

            return source;
        }

        @Override
        public String getId() {
            return "CropTransformation(width=" + mWidth + ", height=" + mHeight + ")";
        }
    }