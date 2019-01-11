package com.wesmartclothing.tbra.tools;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wesmartclothing.tbra.R;

import java.io.File;


/**
 * @author Jack
 * @date on 2018/10/8
 * @describe 图片加载器
 * @org 智裳科技
 */
public class GlideImageLoader implements ImageLoader {

    public static GlideImageLoader getInstance() {
        return new GlideImageLoader();
    }

    private GlideImageLoader() {
    }

    public void displayImage(Context activity, Object path, ImageView imageView) {
        Glide.with(activity)
                .load(path)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public void displayImage(Context activity, Object path, @DrawableRes int defaultImg, ImageView imageView) {
        Glide.with(activity)
                .load(path)
                .asBitmap()
                .placeholder(defaultImg)
                .centerCrop()
                .into(imageView);
    }


    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .crossFade(500)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .crossFade(500)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}