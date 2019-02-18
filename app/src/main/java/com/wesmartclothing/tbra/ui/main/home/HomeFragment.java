package com.wesmartclothing.tbra.ui.main.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.ExplainException;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseAcFragment;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.constant.PointDate;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.AddTempDataBean;
import com.wesmartclothing.tbra.entity.BottomTabItem;
import com.wesmartclothing.tbra.entity.DeviceBatteryInfoBean;
import com.wesmartclothing.tbra.entity.RecordBean;
import com.wesmartclothing.tbra.entity.ReportDataBean;
import com.wesmartclothing.tbra.entity.SingleDataDetailBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.entity.rxbus.ConnectStateBus;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.AddTempData;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.view.BatteryView;
import com.wesmartclothing.tbra.view.HistoryTempView;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

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
    @BindView(R.id.pro_syncData)
    RxRoundProgressBar mProSyncData;
    @BindView(R.id.layout_syncData)
    RelativeLayout mLayoutSyncData;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    private static boolean FIRST_CONNECT = false;
    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTitleTabItems = new ArrayList<>();
    private List<SingleDataDetailBean> pointDatalist;
    private BaseQuickAdapter adapter, errorPointAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initBundle(Bundle bundle) {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initViews() {
        initTitleTab();
        initTab();
        initRecyclerView();
        initErrorPoint();

        mRecyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //允许ScrollView截断点击事件，ScrollView可滑动
                mScrollView.requestDisallowInterceptTouchEvent(false);
            } else {
                //不允许ScrollView截断点击事件，点击事件由子View处理
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

    }

    private void bleConnectState(boolean isConnected) {
        mTvConnectState.setText(isConnected ? "设备已连接" : "设备未连接");
        mImgBattery.setVisibility(isConnected ? View.VISIBLE : View.INVISIBLE);
        mImgBattery.setBatteryValue(100);
        FIRST_CONNECT = isConnected;
        if (isConnected) BleAPI.getBattery();

        if (isVisibled()) {
            uploadTempData();
        }

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
            if (errorPoints != null) {
                List<String> errorPoint = new ArrayList<>(errorPoints.keySet());
                errorPointAdapter.setNewData(errorPoint);
            }
        });
        mHistoryTempView.setOnSelectParentListener(() -> {
            Bundle bundle = new Bundle();
            bundle.putString(Key.BUNDLE_LATEST_TYPE, ((BottomTabItem) mTitleTabItems.get(mTitleCommonTabLayout.getCurrentTab())).getTag());
            RxActivityUtils.skipActivity(mContext, HistoryMonitorActivity.class, bundle);
        });
    }


    /**
     * 周报月报
     */
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        adapter = new BaseQuickAdapter<ReportDataBean.ListBean, BaseViewHolder>(R.layout.item_report) {

            @Override
            protected void convert(BaseViewHolder helper, ReportDataBean.ListBean item) {
                SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder("监测时长\t")
                        .append(item.getCollectCount() * 5 + "min").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .append("\n采集次数\t")
                        .append(item.getCollectCount() + "").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
//                        .append("\n异常点位\t")
//                        .append("L03,L04,L05").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .append("\n告警次数\t")
                        .append(item.getUnusualCount() + "").setForegroundColor(ContextCompat.getColor(mContext, R.color.color_444A59))
                        .create();

                helper.setImageResource(R.id.img_date, mCommonTabLayout.getCurrentTab() == 0 ? R.mipmap.ic_week : R.mipmap.ic_month)
                        .setImageResource(R.id.img_mark, item.getWarningFlag() == 0 ? R.mipmap.ic_security_green : R.mipmap.ic_warning_red)
                        .setText(R.id.tv_reportDetail, stringBuilder)
                        .setImageResource(R.id.img_complete, item.getReadState() == 0 ? R.color.white : R.mipmap.ic_complete_pink);
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ReportDataBean.ListBean item = (ReportDataBean.ListBean) adapter.getItem(position);
            if (item.getReadState() == 0) {
                item.setReadState(1);
                adapter.setData(position, item);
            }

            Bundle bundle = new Bundle();
            bundle.putInt(Key.BUNDLE_REPORT_TYPE, mCommonTabLayout.getCurrentTab());
            bundle.putString(Key.BUNDLE_GID_DATA, ((ReportDataBean.ListBean) adapter.getItem(position)).getGid());
            RxActivityUtils.skipActivity(mContext, ReportActivity.class, bundle);
        });

        mRecyclerView.setAdapter(adapter);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
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
        RxBus.getInstance().register2(ConnectStateBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<ConnectStateBus>() {
                    @Override
                    protected void _onNext(ConnectStateBus connectStateBus) {
                        bleConnectState(connectStateBus.isConnect());
                    }
                });

        RxBus.getInstance().register2(DeviceBatteryInfoBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<DeviceBatteryInfoBean>() {
                    @Override
                    protected void _onNext(DeviceBatteryInfoBean deviceBatteryInfoBean) {
                        mImgBattery.setBatteryValue(deviceBatteryInfoBean.getBatteryValue());
                    }
                });
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
                //测试功能
                if (BuildConfig.DEBUG) {
                    if (position == 0) {
                        clearHistoryData();
                    } else if (position == 1) {
                        List<AddTempDataBean> tempDataBeans = new ArrayList<>();
                        BleAPI.getTempData(0, 10, new RxSubscriber<AddTempDataBean>() {
                            @Override
                            protected void _onNext(AddTempDataBean addTempDataBean) {
                                tempDataBeans.add(addTempDataBean);
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean checkSameDate(long targetTime) {
        return RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date)
                .equals(RxFormat.setFormatDate(targetTime, RxFormat.Date));
    }

    //一号更新月报，星期一更新周报
    private void getReportData(int position) {
        RxManager.getInstance().doNetSubscribe(
                position == 0 ? NetManager.getApiService().weekDataList(1, 30) :
                        NetManager.getApiService().monthDataList(1, 30),
                lifecycleSubject,
                position == 0 ? "weekDataList" : "monthDataList",
                ReportDataBean.class,
                CacheStrategy.firstCacheTimeout(Key.CACHE_TIME_OUT_DAY)
        )
                .subscribe(new RxSubscriber<ReportDataBean>() {
                    @Override
                    protected void _onNext(ReportDataBean pointDataBeans) {
                        adapter.setNewData(pointDataBeans.getList());
                    }
                });
    }


    private void getPointData(String tag) {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getBigFileService().latestSingleData(new RecordBean(tag))
                , lifecycleSubject,
                "latestSingleData" + tag,
                new TypeToken<List<SingleDataDetailBean>>() {
                }.getType(),
                CacheStrategy.firstCache()
        )
                .compose(RxComposeTools.showDialog(mContext, "latestSingleData" + tag))
                .subscribe(new RxNetSubscriber<List<SingleDataDetailBean>>() {
                    @Override
                    protected void _onNext(List<SingleDataDetailBean> list) {
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

    UserInfoBean userInfo;

    /**
     * 上传蓝牙数据成功后清除本地数据
     */
    private void clearHistoryData() {

        //保留用户数据
        RxCache.getDefault().<UserInfoBean>load(SPKey.SP_UserInfo, UserInfoBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfoBean) {
                        userInfo = userInfoBean;
                    }
                });

        RxCache.getDefault().clear()
                .subscribe(new RxNetSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        RxLogUtils.d("缓存是否清空：" + aBoolean);
                        if (aBoolean) {
                            RxCache.getDefault().save(SPKey.SP_UserInfo, userInfo)
                                    .subscribe(new RxNetSubscriber<Boolean>() {
                                        @Override
                                        protected void _onNext(Boolean aBoolean) {

                                        }
                                    });
                            getPointData(PointDate.week);
                        }

                    }
                });
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        uploadTempData();

    }

    private void uploadTempData() {
        if (FIRST_CONNECT) {
            FIRST_CONNECT = false;
            AddTempData addTempData = new AddTempData();

            addTempData.setRxSubscriber(new RxSubscriber<Integer>() {
                //开始
                @Override
                public void onSubscribe(Disposable d) {
                    super.onSubscribe(d);
                    RxLogUtils.d("有未同步的数据");

                    CustomDialog show = CustomDialog.show(mContext, R.layout.dialog_data_sync);
                    new Handler().postDelayed(() -> {
                        show.doDismiss();
                    }, 1500);

                    RxAnimationUtils.animateHeight(RxUtils.dp2px(1), RxUtils.dp2px(39), mLayoutSyncData);
                }

                //进度
                @Override
                protected void _onNext(Integer integer) {
                    RxLogUtils.d("进度：" + integer);
                    mProSyncData.setProgress(integer);
                }

                //上传完成
                @Override
                public void onComplete() {
                    super.onComplete();
                    RxLogUtils.d("上传完成");
                    RxAnimationUtils.animateHeight(RxUtils.dp2px(39), RxUtils.dp2px(0), mLayoutSyncData);
                    RxLogUtils.d("蓝牙数据获取完成：");
                    if (mContext != null) {
                        TipDialog tipDialog = TipDialog.build(mContext, "获取数据成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                        tipDialog.setCanCancel(true);
                        tipDialog.showDialog();
                    }
                    clearHistoryData();
                }

                //异常
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    RxLogUtils.d("异常");
                    RxAnimationUtils.animateHeight(RxUtils.dp2px(39), RxUtils.dp2px(0), mLayoutSyncData);
                    if (mContext != null) {
                        TipDialog tipDialog = TipDialog.build(mContext, ((ExplainException) e).getMsg(), TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                        tipDialog.setCanCancel(true);
                        tipDialog.showDialog();
                    }
                }
            });

            addTempData.uploadCacheOrBleData();
        }
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
                if (RxDataUtils.isEmpty(pointDatalist)) return;
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_LATEST_TYPE, ((BottomTabItem) mTitleTabItems.get(mTitleCommonTabLayout.getCurrentTab())).getTag());
                RxActivityUtils.skipActivity(mContext, ErrorPointActivity.class, bundle);
                break;
            default:
                break;
        }
    }


}
