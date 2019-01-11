package com.wesmartclothing.tbra.ui.main.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseAcFragment;
import com.wesmartclothing.tbra.entity.BottomTabItem;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * @Package com.wesmartclothing.tbra.ui.main.home
 * @FileName HomeFragment
 * @Date 2019/1/8 11:00
 * @Author JACK
 * @Describe TODO首页
 * @Project tbra
 */
public class HomeFragment extends BaseAcFragment {


    @BindView(R.id.tv_connectState)
    TextView mTvConnectState;
    @BindView(R.id.img_battery)
    ImageView mImgBattery;
    @BindView(R.id.reportCommonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.reportRecyclerView)
    RecyclerView mRecyclerView;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }


    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();

    @Override
    public int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTab();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));
        BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_report) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        };
        mRecyclerView.setAdapter(adapter);

        adapter.setNewData(Arrays.asList("", "", ""));
    }

    private void initTab() {
        mBottomTabItems.clear();
        mBottomTabItems.add(new BottomTabItem("周报"));
        mBottomTabItems.add(new BottomTabItem("月报"));

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

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

}
