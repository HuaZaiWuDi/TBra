package com.wesmartclothing.tbra.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.GidBean;
import com.wesmartclothing.tbra.entity.MonthOrWeekPointsBean;
import com.wesmartclothing.tbra.entity.ReportDetailBean;
import com.wesmartclothing.tbra.entity.SingleSelectBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.MapSortUtil;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.view.HistoryTempView;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ReportActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.tv_monitorTime)
    TextView mTvMonitorTime;
    @BindView(R.id.tv_collectionCount)
    TextView mTvCollectionCount;
    @BindView(R.id.tv_warningCount)
    TextView mTvWarningCount;
    @BindView(R.id.tv_pointDistribution)
    TextView mTvPointDistribution;
    @BindView(R.id.pro_errorTotal)
    RoundProgressBar mProErrorTotal;
    @BindView(R.id.tv_leftProportion)
    TextView mTvLeftProportion;
    @BindView(R.id.tv_rightProportion)
    TextView mTvRightProportion;
    @BindView(R.id.tv_errorTotal)
    TextView mTvErrorTotal;
    @BindView(R.id.tv_leftCount)
    TextView mTvLeftCount;
    @BindView(R.id.pie_left)
    PieChart mPieLeft;
    @BindView(R.id.rightCount)
    TextView mRightCount;
    @BindView(R.id.pie_right)
    PieChart mPieRight;
    @BindView(R.id.tv_pointDetail)
    TextView mTvPointDetail;
    @BindView(R.id.recyclerPoint)
    RecyclerView mRecyclerPoint;
    @BindView(R.id.recyclerPointDetail)
    RecyclerView mRecyclerWarningPoint;
    @BindView(R.id.historyTempView)
    HistoryTempView mHistoryTempView;


    private BaseQuickAdapter errorPointAdapter, errorPointDetailAdapter;
    private MonthOrWeekPointsBean mMonthOrWeekPointsBean = new MonthOrWeekPointsBean();
    private Map<String, Integer> lefeErorCountMap = new HashMap<>();
    private Map<String, Integer> rightErorCountMap = new HashMap<>();
    private int leftCount, rightCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_report;
    }

    @Override
    public void initBundle(Bundle bundle) {
        boolean isMonth = bundle.getInt(Key.BUNDLE_REPORT_TYPE) == 1;
        String gid = bundle.getString(Key.BUNDLE_GID_DATA);

        mRxTitle.setTitle(isMonth ? "监测月报" : "监测周报");
        mMonthOrWeekPointsBean.setCycleFlag(isMonth ? "month" : "week");
        mMonthOrWeekPointsBean.setGid(gid);

        String key = isMonth ? "monthInfo" + gid : "weekInfo" + gid;

        RxManager.getInstance().doNetSubscribe(
                isMonth ? NetManager.getApiService().monthInfo(new GidBean(gid)) :
                        NetManager.getApiService().weekInfo(new GidBean(gid)),
                lifecycleSubject,
                key,
                ReportDetailBean.class,
//                CacheStrategy.firstCacheTimeout(Key.CACHE_TIME_OUT)
                CacheStrategy.firstCache()
        )
                .compose(RxComposeTools.showDialog(mContext, key))
                .subscribe(new RxNetSubscriber<ReportDetailBean>() {
                    @Override
                    protected void _onNext(ReportDetailBean reportDetailBean) {
                        updateUI(reportDetailBean, isMonth);
                    }
                });
    }


    private void updateUI(ReportDetailBean reportDetailBean, boolean isMonth) {
        if (isMonth) {
            ReportDetailBean.WeekDataBean monthData = reportDetailBean.getMonthData();
            report(monthData);
        } else {
            ReportDetailBean.WeekDataBean weekData = reportDetailBean.getWeekData();
            report(weekData);
        }

        List<ReportDetailBean.PointsListBean> pointsList = reportDetailBean.getPointsList();
        List<SingleSelectBean> errorPointNameList = new ArrayList<>();

        pointsList.forEach(pointsListBean -> {
            RxLogUtils.d("异常次数：" + pointsListBean.getUnusualCount());
            RxLogUtils.d("异常名字：" + pointsListBean.getPoint());
            if ("L".equals(pointsListBean.getSide())) {
                lefeErorCountMap.put(pointsListBean.getPoint(), pointsListBean.getUnusualCount());
            } else {
                rightErorCountMap.put(pointsListBean.getPoint(), pointsListBean.getUnusualCount());
            }
            errorPointNameList.add(new SingleSelectBean(pointsListBean.getSide() + pointsListBean.getPoint()));
        });

        if (!RxDataUtils.isEmpty(errorPointNameList)) {
            errorPointNameList.get(0).setSelect(true);
            uploadSinglePointData(errorPointNameList.get(0).getText());
        }
        errorPointAdapter.setNewData(errorPointNameList);

        ArrayList<PieEntry> leftEntrys = sortUtil(lefeErorCountMap, leftCount);
        ArrayList<PieEntry> rightEntrys = sortUtil(rightErorCountMap, rightCount);

        setData(mPieLeft, "mPieLeft", leftEntrys);
        setData(mPieRight, "mPieRight", rightEntrys);

    }

    private void report(ReportDetailBean.WeekDataBean monthData) {
        if (monthData != null) {
            mTvMonitorTime.setText("监测时段\t\t\t\t\t" + RxFormat.setFormatDate(monthData.getStartTime(), RxFormat.Date_Date2) + "～" +
                    RxFormat.setFormatDate(monthData.getEndTime(), RxFormat.Date_Date2));
            mTvCollectionCount.setText(monthData.getCollectCount() + "");
            mTvWarningCount.setText(monthData.getUnusualCount() + "");

            int total = monthData.getUnusualCount();
            leftCount = monthData.getLeftUnusual();
            rightCount = monthData.getRightUnusual();

            SpannableStringBuilder stringBuilder = RxTextUtils.getBuilder("异常总数\n").setBold()
                    .append(total + "次")
                    .setProportion(0.9f).create();
            mTvErrorTotal.setText(stringBuilder);

            int sumCount = leftCount + rightCount;
            double leftProportion = RxFormatValue.format4S5R(leftCount * 100f / sumCount, 1);
            double rightProportion = RxFormatValue.format4S5R(rightCount * 100f / sumCount, 1);

            RxTextUtils.getBuilder("L\t" + leftProportion + "%\n")
                    .append(leftCount + "次").setProportion(0.9f)
                    .into(mTvLeftProportion);

            RxTextUtils.getBuilder("L\t" + rightProportion + "%\n")
                    .append(rightCount + "次").setProportion(0.9f)
                    .into(mTvRightProportion);

        }
    }

    private ArrayList<PieEntry> sortUtil(Map<String, Integer> map, int total) {
        map = MapSortUtil.sortMapByValue(map);
        ArrayList<PieEntry> entrys = new ArrayList<>();
        if (RxDataUtils.isEmpty(map)) return entrys;
        map.forEach((s, integer) -> {
            if (entrys.size() < 3) {
                entrys.add(new PieEntry(integer / total * 1f, integer + "次", s));
            }
        });
        return entrys;
    }


    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initPie(mPieLeft);
        initPie(mPieRight);

        initErrorPoint();
        initPointDetail();
    }

    private void initPie(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
//        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

//        chart.setCenterTextTypeface(tfLight);
//        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(false);
//        chart.setHoleColor(Color.WHITE);

//        chart.setTransparentCircleColor(Color.WHITE);
//        chart.setTransparentCircleAlpha(110);

//        chart.setHoleRadius(58f);
//        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(false);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

//        chart.animateY(1400, Easing.EaseInOutQuad);
        chart.spin(1000, chart.getRotationAngle(), chart.getRotationAngle() + 360, Easing.EaseInOutCubic);
        // chart.spin(2000, 0, 360);
        chart.getLegend().setEnabled(false);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(7f);
    }


    private void setData(PieChart chart, String label, ArrayList<PieEntry> entries) {

        PieDataSet dataSet = new PieDataSet(entries, label);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FB8E8A"));
        colors.add(Color.parseColor("#FB807B"));
        colors.add(Color.parseColor("#FB726D"));

//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) ->
                (String) ((PieEntry) entry).getData()
        );

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void initErrorPoint() {
        mRecyclerPoint.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        errorPointAdapter = new BaseQuickAdapter<SingleSelectBean, BaseViewHolder>(R.layout.item_point) {
            @Override
            protected void convert(BaseViewHolder helper, SingleSelectBean item) {
                helper.setText(R.id.tv_point, item.getText())
                        .setBackgroundRes(R.id.tv_point, item.isSelect() ?
                                R.drawable.shape_theme_circle_42 : R.drawable.shape_gray_circle_42);
                doAnim(helper.getView(R.id.tv_point), item.isSelect());
            }
        };
        mRecyclerPoint.setTag(0);
        errorPointAdapter.setOnItemClickListener((adapter1, view, position) -> {
            int lastIndex = (int) mRecyclerPoint.getTag();
            if (position != lastIndex) {
                SingleSelectBean lastItem = (SingleSelectBean) errorPointAdapter.getItem(lastIndex);
                SingleSelectBean curItem = (SingleSelectBean) errorPointAdapter.getItem(position);
                lastItem.setSelect(false);
                curItem.setSelect(true);
                errorPointAdapter.setData(lastIndex, lastItem);
                errorPointAdapter.setData(position, curItem);
//                errorPointDetailAdapter.setNewData(errorMap.get(curItem.getText()));
                uploadSinglePointData(curItem.getText());
                mHistoryTempView.setSingleMode(curItem.getText());
                mRecyclerPoint.setTag(position);
            }
        });

        mRecyclerPoint.setAdapter(errorPointAdapter);
    }

    private void doAnim(View view, boolean openOrClose) {
        if (openOrClose) {
            view.setScaleX(1.2f);
            view.setScaleY(1.2f);
        } else {
            view.setScaleX(1f);
            view.setScaleY(1f);
        }
    }


    private void initPointDetail() {
        mRecyclerWarningPoint.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerWarningPoint.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        errorPointDetailAdapter = new BaseQuickAdapter<ReportDetailBean.PointsListBean, BaseViewHolder>(R.layout.item_warning_point) {
            @Override
            protected void convert(BaseViewHolder helper, ReportDetailBean.PointsListBean item) {
                String pointTime = RxFormat.setFormatDate(item.getCollectDate(), RxFormat.Date_Date2) + "\n" +
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

    private void uploadSinglePointData(String pointName) {
        mMonthOrWeekPointsBean.setPointName(pointName);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().monthOrWeekPointsInfo(mMonthOrWeekPointsBean),
                lifecycleSubject,
                "monthOrWeekPointsInfo" + mMonthOrWeekPointsBean.getGid() + pointName,
                new TypeToken<List<ReportDetailBean.PointsListBean>>() {
                }.getType(),
                CacheStrategy.firstCache()
        )
                .compose(RxComposeTools.showDialog(mContext, "monthOrWeekPointsInfo" + mMonthOrWeekPointsBean.getGid() + pointName))
                .subscribe(new RxNetSubscriber<List<ReportDetailBean.PointsListBean>>() {
                    @Override
                    protected void _onNext(List<ReportDetailBean.PointsListBean> pointsListBeans) {
                        errorPointDetailAdapter.setNewData(pointsListBeans);
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
