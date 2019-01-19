package com.wesmartclothing.tbra.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseAcFragment;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.constant.PointDate;
import com.wesmartclothing.tbra.entity.BottomTabItem;
import com.wesmartclothing.tbra.entity.JsonDataBean;
import com.wesmartclothing.tbra.entity.PointDataBean;
import com.wesmartclothing.tbra.entity.RecordBean;
import com.wesmartclothing.tbra.entity.ReportDataBean;
import com.wesmartclothing.tbra.entity.SinglePointBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.tools.MapSortUtil;
import com.wesmartclothing.tbra.ui.main.MainActivity;
import com.wesmartclothing.tbra.view.BatteryView;
import com.wesmartclothing.tbra.view.HistoryTempView;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
    @BindView(R.id.tv_seeMore)
    TextView mTvSeeMore;
    @BindView(R.id.img_battery)
    BatteryView mImgBattery;
    @BindView(R.id.reportCommonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.reportRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.historyTempView)
    HistoryTempView mHistoryTempView;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout mTitleCommonTabLayout;
    @BindView(R.id.errorRecyclerView)
    RecyclerView mErrorRecyclerView;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }


    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTitleTabItems = new ArrayList<>();
    private List<PointDataBean> pointDatalist;
    private BaseQuickAdapter adapter, errorPointAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initBundle(Bundle bundle) {
    }

    @Override
    public void initViews() {
        initTitleTab();
        initTab();
        initRecyclerView();
        initErrorPoint();
        bleConnectState(BleTools.getInstance().isConnected());

    }

    private void bleConnectState(boolean isConnected) {
        mTvConnectState.setText(isConnected ? "设备已连接" : "设备未连接");
//        mImgBattery.setVisibility(isConnected ? View.VISIBLE : View.INVISIBLE);
        mImgBattery.setBatteryValue(50);
    }

    private void initErrorPoint() {
        mErrorRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 8));
        errorPointAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_error_point) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_errorPoint, item)
                        .setTextColor(R.id.tv_errorPoint, ContextCompat.getColor(mActivity,
                                helper.getAdapterPosition() >= 3 ? R.color.font_C3C5CA : R.color.colortheme));
            }
        };
        mErrorRecyclerView.setAdapter(errorPointAdapter);

        mHistoryTempView.setOnErrorPointListener(errorPoints -> {
            for (String key : errorPoints.keySet()) {
                RxLogUtils.d("异常点位：key" + key + "--value:" + errorPoints.get(key));
            }
            List<String> errorPoint = new ArrayList<>(errorPoints.keySet());
            errorPointAdapter.setNewData(errorPoint);
        });

    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        adapter = new BaseQuickAdapter<ReportDataBean.ListBean, BaseViewHolder>(R.layout.item_report) {

            @Override
            protected void convert(BaseViewHolder helper, ReportDataBean.ListBean item) {
                SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder("监测时长\t")
                        .append("205min").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .append("\n采集次数\t")
                        .append(item.getCollectCount() + "").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .append("\n异常点位\t")
                        .append("L03,L04,L05").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .append("\n告警次数\t")
                        .append(item.getUnusualCount() + "").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .create();

                helper.setImageResource(R.id.img_date, mCommonTabLayout.getCurrentTab() == 0 ? R.mipmap.ic_week : R.mipmap.ic_month)
                        .setImageResource(R.id.img_mark, item.getWarningFlag() == 0 ? R.mipmap.ic_security_green : R.mipmap.ic_warning_red)
                        .setText(R.id.tv_reportDetail, stringBuilder)
                        .setImageResource(R.id.img_complete, R.mipmap.ic_complete_pink);
            }
        };
        mRecyclerView.setAdapter(adapter);
//        adapter.setNewData(Arrays.asList("1", "2", "3"));
    }

    private void initTab() {
        mBottomTabItems.clear();
        mBottomTabItems.add(new BottomTabItem("周报"));
        mBottomTabItems.add(new BottomTabItem("月报"));

        mCommonTabLayout.setTabData(mBottomTabItems);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                getReportData(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    @Override
    public void initNetData() {
        getPointData(PointDate.week);
        getReportData(0);
    }

    @Override
    public void initRxBus2() {

    }


    private void initTitleTab() {
        mTitleTabItems.clear();
        mTitleTabItems.add(new BottomTabItem("近一周", PointDate.week));
        mTitleTabItems.add(new BottomTabItem("近一月", PointDate.oneMonth));
        mTitleTabItems.add(new BottomTabItem("近三个月", PointDate.quarter));
        mTitleTabItems.add(new BottomTabItem("近半年", PointDate.halfYear));
        mTitleTabItems.add(new BottomTabItem("近一年", PointDate.year));
//        mBottomTabItems.add(new BottomTabItem("全部"));

        mTitleCommonTabLayout.setTabData(mTitleTabItems);
        mTitleCommonTabLayout.setCurrentTab(0);
        mTitleCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mHistoryTempView.pause();
                getPointData(((BottomTabItem) mTitleTabItems.get(position)).getTag());
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    clearHistoryData();
                }
            }
        });
    }

    private void getReportData(int position) {
        RxManager.getInstance().doNetSubscribe(
                position == 0 ? NetManager.getApiService().weekDataList(1, 7) :
                        NetManager.getApiService().monthDataList(1, 7),
                lifecycleSubject,
                position == 0 ? "weekDataList" : "monthDataList",
                ReportDataBean.class,
                CacheStrategy.firstCacheTimeout(Key.CACHE_TIME_OUT)
        )
//                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxSubscriber<ReportDataBean>() {
                    @Override
                    protected void _onNext(ReportDataBean pointDataBeans) {
                        adapter.setNewData(pointDataBeans.getList());
                    }
                });
    }


    private void getPointData(String tag) {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().latestSingleData(new RecordBean(tag))
                , lifecycleSubject)
                .compose(RxCache.getDefault().transformObservable(
                        "latestSingleData" + tag,
                        new TypeToken<List<PointDataBean>>() {
                        }.getType(),
                        CacheStrategy.firstCacheTimeout(Key.CACHE_TIME_OUT)))//保存数据一周
                .map(new CacheResult.MapFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (!RxCache.getDefault().containsKey("latestSingleData" + tag)) {
                        WaitDialog.show(mContext, "正在加载");
                    }
                })
                .doFinally(() -> {
                    WaitDialog.dismiss();
                })
                .subscribe(new RxNetSubscriber<List<PointDataBean>>() {
                    @Override
                    protected void _onNext(List<PointDataBean> list) {
                        RxLogUtils.d("当前线程：" + Thread.currentThread().getName());
                        pointDatalist = list;
                        mHistoryTempView.setData(list);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private void initWarningDialogData(View rootView) {
        rootView.findViewById(R.id.img_close)
                .setOnClickListener(view -> CustomDialog.unloadAllDialog());

        //起始，结束时间
        TextView mTvMonitorTime = rootView.findViewById(R.id.tv_monitorTime);
        //异常点位
        TextView mTvMonitorPoint = rootView.findViewById(R.id.tv_monitorPoint);
        //异常次数
        TextView mTvWarningCount = rootView.findViewById(R.id.tv_warningCount);
        //异常点位
        TextView mTvWarningPoint = rootView.findViewById(R.id.tv_warningPoint);
        RecyclerView mRecyclerWarningPoint = rootView.findViewById(R.id.recycler_WaringPoint);
        mRecyclerWarningPoint.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerWarningPoint.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        BaseQuickAdapter adapter = new BaseQuickAdapter<SinglePointBean, BaseViewHolder>(R.layout.item_warning_point) {
            @Override
            protected void convert(BaseViewHolder helper, SinglePointBean item) {
                String pointTime = RxFormat.setFormatDate(item.getTime(), RxFormat.Date_Date2) + "\n" +
                        "异常点位：" + item.getPointName();
                SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder("异常温度：")
                        .append(item.getTemp() + "°C").setForegroundColor(Color.parseColor("#FF756E"))
                        .create();
                helper.setText(R.id.tv_point_time, pointTime)
                        .setText(R.id.tv_temp, stringBuilder);
            }
        };
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Key.BUNDLE_LATEST_TYPE, ((BottomTabItem) mTitleTabItems.get(mTitleCommonTabLayout.getCurrentTab())).getTag());
            bundle.putString(Key.BUNDLE_POINT_NAME, ((SinglePointBean) adapter1.getItem(position)).getPointName());
            RxActivityUtils.skipActivity(mContext, MainActivity.class, bundle);
        });

        mRecyclerWarningPoint.setAdapter(adapter);

        int errorTotal = 0;
        Map<String, Integer> errorMap = new HashMap<>();
        ArrayList<SinglePointBean> singlePointBeans = new ArrayList<>();
        Gson gson = new Gson();
        for (PointDataBean bean : pointDatalist) {
            errorTotal += bean.getUnusualNum();
            List<JsonDataBean> errorPointlist = gson.fromJson(bean.getUnusualPoints(), new TypeToken<List<JsonDataBean>>() {
            }.getType());
            for (JsonDataBean errorBean : errorPointlist) {
                errorMap.merge(errorBean.getNodeName(), 1, Integer::sum);
                singlePointBeans.add(new SinglePointBean(bean.getCollectTime(), errorBean.getNodeName(), errorBean.getNodeTemp()));
            }
        }
        adapter.setNewData(singlePointBeans);
        mTvMonitorTime.setText("监测时段\t" + RxFormat.setFormatDate(pointDatalist.get(0).getCollectTime(), RxFormat.Date_Date2) + "～" +
                RxFormat.setFormatDate(pointDatalist.get(pointDatalist.size() - 1).getCollectTime(), RxFormat.Date_Date2));
        mTvWarningCount.setText(errorTotal + "");
        mTvWarningPoint.setText(errorMap.size() + "");

        errorMap = MapSortUtil.sortMapByValue(errorMap, true);

        Object[] keys = errorMap.keySet().toArray();
        StringJoiner joiner = new StringJoiner("、");
        for (int i = 1; i < keys.length; i++) {
            joiner.add((String) keys[i]);
        }

        SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder("异常点位\t")
                .append(keys[0].toString()).setForegroundColor(Color.parseColor("#FF756E"))
                .append("、" + joiner.toString())
                .create();

        mTvMonitorPoint.setText(stringBuilder);

    }

    /**
     * 上传蓝牙数据成功后清除本地数据
     */
    private void clearHistoryData() {
        RxCache.getDefault().remove("latestSingleData" + PointDate.week);
        RxCache.getDefault().remove("latestSingleData" + PointDate.oneMonth);
        RxCache.getDefault().remove("latestSingleData" + PointDate.quarter);
        RxCache.getDefault().remove("latestSingleData" + PointDate.halfYear);
        RxCache.getDefault().remove("latestSingleData" + PointDate.year);
    }


    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    protected void onInvisible() {
        mHistoryTempView.pause();
        super.onInvisible();
    }

    @OnClick({R.id.tv_seeMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_seeMore:
                if (pointDatalist == null) return;
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_LATEST_TYPE, ((BottomTabItem) mTitleTabItems.get(mTitleCommonTabLayout.getCurrentTab())).getTag());
                RxActivityUtils.skipActivity(mContext, ErrorPointActivity.class, bundle);
                break;
            default:
                break;
        }
    }

}
