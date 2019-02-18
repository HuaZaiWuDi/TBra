package com.wesmartclothing.tbra.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.ErrorRecordPointDetailBean;
import com.wesmartclothing.tbra.entity.PointDataBean;
import com.wesmartclothing.tbra.entity.RecordBean;
import com.wesmartclothing.tbra.entity.SingleSelectBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ErrorPointActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.tv_monitorTime)
    TextView mTvMonitorTime;
    @BindView(R.id.tv_warningPoint)
    TextView mTvWarningPoint;
    @BindView(R.id.tv_warningCount)
    TextView mTvWarningCount;
    @BindView(R.id.tv_monitorPoint)
    TextView mTvMonitorPoint;
    @BindView(R.id.recyclerErrorPoint)
    RecyclerView mRecyclerErrorPoint;
    @BindView(R.id.recyclerErrorDetail)
    RecyclerView mRecyclerWarningPoint;

    private BaseQuickAdapter errorPointDetailAdapter, errorPointAdapter;
    private long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_error_point;
    }

    @Override
    public void initBundle(Bundle bundle) {
        String latestType = bundle.getString(Key.BUNDLE_LATEST_TYPE);
        RxLogUtils.d("类型：" + latestType);

        if (latestType.startsWith("warningInfoReaded")) {
            RxCache.getDefault().<PointDataBean>load(latestType, PointDataBean.class)
                    .compose(RxComposeUtils.bindLife(lifecycleSubject))
                    .map(new CacheResult.MapFunc())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxNetSubscriber<PointDataBean>() {
                        @Override
                        protected void _onNext(PointDataBean bean) {
                            updateUI(bean);
                        }
                    });
        } else {
            RxManager.getInstance().doNetSubscribe(
                    NetManager.getApiService().unusualData(new RecordBean(latestType))
                    , lifecycleSubject,
                    "unusualData" + latestType,
                    PointDataBean.class,
                    CacheStrategy.firstCache())
                    .subscribe(new RxNetSubscriber<PointDataBean>() {
                        @Override
                        protected void _onNext(PointDataBean bean) {
                            updateUI(bean);
                        }
                    });
        }
    }

    private void updateUI(PointDataBean bean) {
        startTime = bean.getStartTime();
        endTime = bean.getEndTime();

        mTvMonitorTime.setText("监测时段\t\t\t\t\t" + RxFormat.setFormatDate(startTime, RxFormat.Date_Date2) + "～" +
                RxFormat.setFormatDate(endTime, RxFormat.Date_Date2));

        List<SingleSelectBean> beans = new ArrayList<>();
        bean.getPointsList().forEach(pointsListBean -> {
            if (pointsListBean.getWarningFlag() == 1) {
                beans.add(new SingleSelectBean(pointsListBean.getSide() + pointsListBean.getPoint()));
            }
        });

        //警告次数
        mTvWarningCount.setText(bean.getUnusualCount() + "");

        //异常点位个数
        mTvWarningPoint.setText(beans.size() + "");

        if (!RxDataUtils.isEmpty(beans)) {
            beans.get(0).setSelect(true);
            getErrorPointDetail(beans.get(0).getText());
        }
        errorPointAdapter.setNewData(beans);

    }


    private void getErrorPointDetail(String pointName) {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().unusualPointData(
                        pointName.substring(0, 1),
                        pointName.substring(1, pointName.length()),
                        startTime,
                        endTime,
                        1,
                        30
                )
                , lifecycleSubject, "unusualPointData" + pointName + startTime + endTime,
                ErrorRecordPointDetailBean.class,
                CacheStrategy.firstCache())
                .subscribe(new RxNetSubscriber<ErrorRecordPointDetailBean>() {
                    @Override
                    protected void _onNext(ErrorRecordPointDetailBean bean) {
                        errorPointDetailAdapter.setNewData(bean.getList());
                    }


                });
    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initPointDetail();
        initErrorPoint();
    }

    private void initErrorPoint() {
        mRecyclerErrorPoint.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        errorPointAdapter = new BaseQuickAdapter<SingleSelectBean, BaseViewHolder>(R.layout.item_point) {
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
        mRecyclerErrorPoint.setTag(0);
        errorPointAdapter.setOnItemClickListener((adapter1, view, position) -> {
            int lastIndex = (int) mRecyclerErrorPoint.getTag();
            if (position != lastIndex) {
                SingleSelectBean lastItem = (SingleSelectBean) errorPointAdapter.getItem(lastIndex);
                SingleSelectBean curItem = (SingleSelectBean) errorPointAdapter.getItem(position);
                lastItem.setSelect(false);
                curItem.setSelect(true);
                errorPointAdapter.setData(lastIndex, lastItem);
                errorPointAdapter.setData(position, curItem);

                getErrorPointDetail(curItem.getText());
                mRecyclerErrorPoint.setTag(position);
            }
        });

        mRecyclerErrorPoint.setAdapter(errorPointAdapter);
    }

    private void initPointDetail() {
        mRecyclerWarningPoint.setLayoutManager(new LinearLayoutManager(mContext));
        errorPointDetailAdapter = new BaseQuickAdapter<ErrorRecordPointDetailBean.ListBean, BaseViewHolder>(R.layout.item_warning_point) {
            @Override
            protected void convert(BaseViewHolder helper, ErrorRecordPointDetailBean.ListBean item) {
                String pointTime = RxFormat.setFormatDate(item.getCollectDate(), RxFormat.Date) + "\n" +
                        "异常点位：" + item.getSide() + item.getPoint();
                //异常温度通过最大，最小温度与平均温度的偏差对比，
                double errorTemp = Math.abs(item.getMaxTemp() - item.getAvgTemp()) >
                        Math.abs(item.getMinTemp() - item.getAvgTemp()) ? item.getMaxTemp() : item.getMinTemp();
                SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder("异常温度：")
                        .append(errorTemp + "°C").setForegroundColor(Color.parseColor("#FF756E"))
                        .create();
                helper.setText(R.id.tv_point_time, pointTime)
                        .setText(R.id.tv_temp, stringBuilder);
            }
        };
//        errorPointDetailAdapter.setOnItemClickListener((adapter1, view, position) -> {
//            Bundle bundle = new Bundle();
//            bundle.putString(Key.BUNDLE_LATEST_TYPE, (String) mRxTitle.getTag());
//            bundle.putString(Key.BUNDLE_POINT_NAME, ((SinglePointBean) adapter1.getItem(position)).getPointName());
//            RxActivityUtils.skipActivity(mContext, HistoryMonitorActivity.class, bundle);
//        });

        mRecyclerWarningPoint.setAdapter(errorPointDetailAdapter);
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
