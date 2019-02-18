package com.wesmartclothing.tbra.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tmall.ultraviewpager.UltraViewPager;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.entity.CarouselPictureBean;
import com.wesmartclothing.tbra.tools.GlideImageLoader;

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


    private List<CarouselPictureBean> carouselPictureBeans;

    public interface SelectImgListener {
        void selectItem(CarouselPictureBean bean);
    }

    private SelectImgListener mSelectImgListener;

    public void setSelectImgListener(SelectImgListener selectImgListener) {
        mSelectImgListener = selectImgListener;
    }

    public UltraPagerAdapter(List<CarouselPictureBean> carouselPictureBeans, UltraViewPager ultraViewPager) {
        this.carouselPictureBeans = carouselPictureBeans;
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
        ultraViewPager.getIndicator().setGravity(Gravity.END | Gravity.BOTTOM);
        ultraViewPager.getIndicator().setMargin(0, 0, RxUtils.dp2px(8), RxUtils.dp2px(11));
        ultraViewPager.getIndicator().setIndicatorPadding(RxUtils.dp2px(4));
        //构造indicator,绑定到UltraViewPager
        ultraViewPager.getIndicator().build();


        //设定页面循环播放
        ultraViewPager.setInfiniteLoop(true);
        //设定页面自动切换  间隔2秒
        ultraViewPager.setAutoScroll(2000);
    }

    @Override
    public int getCount() {
        return carouselPictureBeans == null ? 0 : carouselPictureBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = View.inflate(container.getContext(), R.layout.layout_wheel_planting, null);
        RxImageView rxImageView = view.findViewById(R.id.img_details);
        RxTextView rxTextView = view.findViewById(R.id.tv_details);

        GlideImageLoader.getInstance().displayImage(
                container.getContext(),
                carouselPictureBeans.get(position).getImgUrl(),
                R.mipmap.ic_launcher,
                rxImageView);

        rxTextView.setText(carouselPictureBeans.get(position).getTitle());

        rxImageView.setOnClickListener(view1 -> {
            if (mSelectImgListener != null)
                mSelectImgListener.selectItem(carouselPictureBeans.get(position));
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
