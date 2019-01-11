package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanDeviceActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.deviceRecyclerView)
    RecyclerView mDeviceRecyclerView;
    @BindView(R.id.tv_btn)
    TextView mTvBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_scan_device;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mDeviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter adapter = new BaseQuickAdapter<BleDevice, BaseViewHolder>(R.layout.item_scan_deivce) {
            @Override
            protected void convert(BaseViewHolder helper, BleDevice item) {

            }
        };
        mDeviceRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    @OnClick(R.id.tv_btn)
    public void onViewClicked() {
    }
}
