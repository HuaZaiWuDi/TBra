package com.wesmartclothing.tbra.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tmall.ultraviewpager.UltraViewPager;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxImageView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.tools.GlideImageLoader;

import java.util.Arrays;
import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.adapter
 * @FileName UltraPagerAdapter
 * @Date 2019/1/8 11:37
 * @Author JACK
 * @Describe TODO轮播图
 * @Project tbra
 */
public class UltraPagerAdapter extends PagerAdapter {


    private List<String> imgs;


    public UltraPagerAdapter(List<String> imgs, UltraViewPager ultraViewPager) {
        this.imgs = imgs;
        RxLogUtils.d("图片：" + Arrays.toString(imgs.toArray()));
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        //内置indicator初始化
        ultraViewPager.initIndicator();
        //设置indicator样式
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.parseColor("#FD74B4"))
                .setNormalColor(Color.WHITE)
                .setRadius(RxUtils.dp2px(3));
        //设置indicator对齐方式
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraViewPager.getIndicator().setMargin(0, 0, 0, RxUtils.dp2px(11));
        ultraViewPager.getIndicator().setIndicatorPadding(RxUtils.dp2px(8));
        //构造indicator,绑定到UltraViewPager
        ultraViewPager.getIndicator().build();

        //设定页面循环播放
        ultraViewPager.setInfiniteLoop(true);
        //设定页面自动切换  间隔2秒
        ultraViewPager.setAutoScroll(2000);
    }

    @Override
    public int getCount() {
        return imgs == null ? 0 : imgs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RxImageView rxImageView = new RxImageView(container.getContext());
        RelativeLayout.MarginLayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.MarginLayoutParams.MATCH_PARENT,
                RelativeLayout.MarginLayoutParams.MATCH_PARENT
        );
        rxImageView.setLayoutParams(params);
        rxImageView.getHelper().setCorner(RxUtils.dp2px(8));
        GlideImageLoader.getInstance().displayImage(
                container.getContext(),
                imgs.get(position),
                R.mipmap.ic_launcher,
                rxImageView);
        container.addView(rxImageView);
        return rxImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RxImageView) object);
    }

}
