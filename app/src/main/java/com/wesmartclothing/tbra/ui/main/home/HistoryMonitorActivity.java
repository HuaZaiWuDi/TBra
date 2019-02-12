package com.wesmartclothing.tbra.ui.main.home;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.SingleHistoryPointBean;
import com.wesmartclothing.tbra.entity.SingleSelectBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.view.HistoryTempDetailView;
import com.wesmartclothing.tbra.view.HistoryTempView;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;

public class HistoryMonitorActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerPoint)
    RecyclerView mRecyclerPoint;
    @BindView(R.id.tv_pointTime)
    TextView mTvPointTime;
    TextView mTvNormalTemp;
    @BindView(R.id.historyTempDetailView)
    HistoryTempDetailView mHistoryTempDetailView;
    @BindView(R.id.tv_leftPoint)
    TextView mTvLeftPoint;
    @BindView(R.id.tv_rightTemp)
    TextView mTvRightTemp;
    @BindView(R.id.historyTempView)
    HistoryTempView mHistoryTempView;

    private String lastetType;

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
        lastetType = bundle.getString(Key.BUNDLE_LATEST_TYPE);
        selectItem("01");
    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initRecycler();
        initChart();
    }

    private void initChart() {
        mHistoryTempDetailView.setOnChartSelectListener(bean -> {
            RxTextUtils.getBuilder("L" + bean.getPoint() + ":")
                    .append(bean.getLeftTemp() + "℃").setForegroundColor(ContextCompat.getColor(mContext,
                    bean.getLeftWarning() == 1 ? R.color.color_FE746E : R.color.color_2C272D))
                    .append("\n标准温度:" + bean.getAvgTemp() + "℃")
                    .into(mTvLeftPoint);

            RxTextUtils.getBuilder("R" + bean.getPoint() + ":")
                    .append(bean.getRightTemp() + "℃")
                    .setForegroundColor(ContextCompat.getColor(mContext, bean.getRightWarning() == 1 ? R.color.color_FE746E : R.color.color_2C272D))
                    .append("\n对称温差:")
                    .append(RxFormatValue.fromat4S5R(Math.abs(bean.getLeftTemp() - bean.getRightTemp()), 2) + "℃")
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.color_FE746E))
                    .into(mTvRightTemp);

            mTvPointTime.setText(RxFormat.setFormatDate(bean.getCollectDate(), RxFormat.Date_Date2));

            mHistoryTempView.setDoubleMode(bean.getPoint());
        });
    }

    private void initRecycler() {
        mRecyclerPoint.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerPoint.setTag(0);
        BaseQuickAdapter adapter = new BaseQuickAdapter<SingleSelectBean, BaseViewHolder>(R.layout.item_point) {
            @Override
            protected void convert(BaseViewHolder helper, SingleSelectBean item) {
                helper.setText(R.id.tv_point, item.getText())
                        .setBackgroundRes(R.id.tv_point, item.isSelect() ?
                                R.drawable.shape_theme_circle_42 : R.drawable.shape_gray_circle_42);
                View view = helper.getView(R.id.tv_point);
                if (item.isSelect()) {
                    view.setScaleX(1.2f);
                    view.setScaleY(1.2f);
                } else {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
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

                selectItem(curItem.getText());
            }
        });
        adapter.addData(new SingleSelectBean("01", true));
        for (int i = 1; i < 8; i++) {
            adapter.addData(new SingleSelectBean("0" + (i + 1)));
        }

        mRecyclerPoint.setAdapter(adapter);
    }


    private void selectItem(String pointName) {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().symmetryPointInfo(lastetType, pointName, 1, 30),
                lifecycleSubject,
                "unusualPointData" + lastetType + pointName,
                SingleHistoryPointBean.class,
                CacheStrategy.firstCache()
        )
                .compose(RxComposeTools.showDialog(mContext, "unusualPointData" + lastetType + pointName))
                .subscribe(new RxNetSubscriber<SingleHistoryPointBean>() {
                    @Override
                    protected void _onNext(SingleHistoryPointBean bean) {
                        mHistoryTempDetailView.updateUI(bean.getList());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
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
