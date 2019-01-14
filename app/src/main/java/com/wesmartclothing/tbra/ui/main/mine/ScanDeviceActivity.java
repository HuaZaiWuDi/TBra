package com.wesmartclothing.tbra.ui.main.mine;

import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.tools.BLEUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanDeviceActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.deviceRecyclerView)
    RecyclerView mDeviceRecyclerView;
    @BindView(R.id.tv_btn)
    TextView mTvBtn;


    BaseQuickAdapter adapter;

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
        adapter = new BaseQuickAdapter<BleDevice, BaseViewHolder>(R.layout.item_scan_deivce) {
            @Override
            protected void convert(BaseViewHolder helper, BleDevice item) {

                String deviceDetail = item.getName() + "\t" +
                        item.getMac().substring(12, item.getMac().length()) + "\n" +
                        "距离：" + BLEUtil.rssi2Distance(item.getRssi(), 2) + "米";

                helper.setText(R.id.tv_deviceDetails, deviceDetail);

            }
        };
        adapter.bindToRecyclerView(mDeviceRecyclerView);
        adapter.setEmptyView(R.layout.layout_empty_device);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                connectDevice(((BleDevice) adapter.getItem(position)).getMac());
            }
        });
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    @OnClick(R.id.tv_btn)
    public void onViewClicked() {
        startScan();
    }

    private void startScan() {
        final BleScanRuleConfig bleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[]{UUID.fromString(BLEKey.UUID_Servie)})
                .setScanTimeOut(15000)
                .build();
        BleTools.getBleManager().initScanRule(bleConfig);

        BleTools.getBleManager().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                RxLogUtils.d("扫描结果:" + scanResultList.size());
                if (scanResultList.isEmpty()) {

                }
            }

            @Override
            public void onScanStarted(boolean success) {
                RxLogUtils.d("扫描开始：" + success);
                //清空列表
                if (success) {
                    adapter.setNewData(null);
                }
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                RxLogUtils.d("正在扫描：" + bleDevice.getMac());
                adapter.addData(bleDevice);
            }
        });
    }


    private void connectDevice(String mac) {
        BleTools.getBleManager().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                WaitDialog.show(mContext, "正在连接");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                WaitDialog.dismiss();
                TipDialog.show(mContext, "连接失败", TipDialog.TYPE_ERROR);
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                WaitDialog.dismiss();
                TipDialog.show(mContext, "连接成功", TipDialog.TYPE_FINISH);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                WaitDialog.dismiss();
                TipDialog.show(mContext, "断开连接", TipDialog.TYPE_ERROR);
            }
        });
    }


    @Override
    protected void onDestroy() {
        BleTools.getInstance().stopScan();
        BleTools.getBleManager().destroy();
        BleTools.getBleManager().removeConnectGattCallback(BleTools.getInstance().getBleDevice());
        super.onDestroy();
    }
}
