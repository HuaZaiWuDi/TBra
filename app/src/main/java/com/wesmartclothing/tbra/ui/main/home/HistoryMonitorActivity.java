package com.wesmartclothing.tbra.ui.main.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.SingleHistoryPointBean;
import com.wesmartclothing.tbra.entity.SingleSelectBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;

public class HistoryMonitorActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerPoint)
    RecyclerView mRecyclerPoint;
    @BindView(R.id.lineChart)
    LineChart mLineChart;
    @BindView(R.id.tv_pointTime)
    TextView mTvPointTime;
    @BindView(R.id.tv_pointTemp)
    TextView mTvPointTemp;
    @BindView(R.id.tv_normalTemp)
    TextView mTvNormalTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_history_mointor;
    }

    @Override
    public void initBundle(Bundle bundle) {
        String lastetType = bundle.getString(Key.BUNDLE_LATEST_TYPE);
        String pointName = bundle.getString(Key.BUNDLE_POINT_NAME);

        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().unusualPointData(lastetType, pointName, 1, 10),
                lifecycleSubject,
                "unusualPointData" + lastetType + pointName,
                SingleHistoryPointBean.class,
                CacheStrategy.firstCache()
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<SingleHistoryPointBean>() {
                    @Override
                    protected void _onNext(SingleHistoryPointBean bean) {
                        RxLogUtils.d("当前线程：" + Thread.currentThread().getName());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initRecycler();
    }

    private void initRecycler() {
        mRecyclerPoint.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerPoint.setTag(0);
        BaseQuickAdapter adapter = new BaseQuickAdapter<SingleSelectBean, BaseViewHolder>(R.layout.item_point) {
            @Override
            protected void convert(BaseViewHolder helper, SingleSelectBean item) {
                helper.setText(R.id.tv_point, item.getText())
                        .setBackgroundRes(R.id.tv_point, item.isSelect() ?
                                R.mipmap.bg_btn : R.drawable.shape_gray_circle_42);
            }
        };

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            int lastIndex = (int) mRecyclerPoint.getTag();
            if (position != lastIndex) {
                SingleSelectBean lastItem = (SingleSelectBean) adapter.getItem(lastIndex);
                SingleSelectBean curItem = (SingleSelectBean) adapter.getItem(position);
                lastItem.setSelect(false);
                curItem.setSelect(true);
                adapter.setData(lastIndex, lastItem);
                adapter.setData(position, curItem);
                mRecyclerPoint.setTag(position);
            }
        });

        mRecyclerPoint.setAdapter(adapter);
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
