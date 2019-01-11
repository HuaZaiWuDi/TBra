package com.wesmartclothing.tbra.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.entity.BottomTabItem;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Package com.wesmartclothing.tbra.view
 * @FileName HistoryTempView
 * @Date 2019/1/8 16:48
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class HistoryTempView extends LinearLayout {
    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.errorRecyclerView)
    RecyclerView mErrorRecyclerView;


    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();

    public HistoryTempView(Context context) {
        this(context, null);
    }

    public HistoryTempView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryTempView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = View.inflate(context, R.layout.layout_history_temp_data, this);
        ButterKnife.bind(this, view);
        initTab();
        initRecyclerView();
    }

    private void initRecyclerView() {


    }

    private void initTab() {
        mBottomTabItems.clear();
        mBottomTabItems.add(new BottomTabItem("全部"));
        mBottomTabItems.add(new BottomTabItem("近一年"));
        mBottomTabItems.add(new BottomTabItem("近半年"));
        mBottomTabItems.add(new BottomTabItem("近三个月"));
        mBottomTabItems.add(new BottomTabItem("近一月"));
        mBottomTabItems.add(new BottomTabItem("近一周"));

        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }
}
