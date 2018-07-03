package com.aashir.bpc.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class CropSquareTransformation extends BitmapTransformation {

    private int mWidth;
    private int mHeight;

    public CropSquareTransformation(Context context) {
        super(context);
    }


    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap source, int width, int height) {
        int size = Math.min(width, height);
        mWidth = (width - size) / 2;
        mHeight = (height - size) / 2;

        Bitmap outsource = bitmapPool.get(mWidth, mHeight, Bitmap.Config.RGB_565);
        if (outsource == null) outsource = Bitmap.createBitmap(source, mWidth, mHeight, size, size);

        return outsource;
    }

    @Override
    public String getId() {
        return "CropSquareTransformation(width=" + mWidth + ", height=" + mHeight + ")";
    }
}