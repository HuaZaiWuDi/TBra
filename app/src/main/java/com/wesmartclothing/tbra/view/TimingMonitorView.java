package com.wesmartclothing.tbra.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxUtils;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.entity.JsonDataBean;
import com.wesmartclothing.tbra.tools.BarRoundChartRenderer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Package com.wesmartclothing.tbra.view
 * @FileName TimingMonitorView
 * @Date 2019/1/8 14:48
 * @Author JACK
 * @Describe TODO封装实时监测界面图标统计
 * @Project tbra
 */
public class TimingMonitorView extends LinearLayout {

    @BindView(R.id.layout_legend)
    LinearLayout mLayoutLegend;
    @BindView(R.id.lineChart)
    LineChart mLineChart;
    @BindView(R.id.mBarChart)
    BarChart mMBarChart;
    @BindView(R.id.tv_L1)
    TextView mTvL1;
    @BindView(R.id.tv_R1)
    TextView mTvR1;
    @BindView(R.id.tv_L2)
    TextView mTvL2;
    @BindView(R.id.tv_R2)
    TextView mTvR2;
    @BindView(R.id.tv_L3)
    TextView mTvL3;
    @BindView(R.id.tv_R3)
    TextView mTvR3;
    @BindView(R.id.tv_L4)
    TextView mTvL4;
    @BindView(R.id.tv_R4)
    TextView mTvR4;
    @BindView(R.id.tv_L5)
    TextView mTvL5;
    @BindView(R.id.tv_R5)
    TextView mTvR5;
    @BindView(R.id.tv_L6)
    TextView mTvL6;
    @BindView(R.id.tv_R6)
    TextView mTvR6;
    @BindView(R.id.tv_L7)
    TextView mTvL7;
    @BindView(R.id.tv_R7)
    TextView mTvR7;
    @BindView(R.id.tv_L8)
    TextView mTvL8;
    @BindView(R.id.tv_R8)
    TextView mTvR8;
    @BindView(R.id.layout_temp)
    LinearLayout mLayoutTemp;


    public TimingMonitorView(Context context) {
        this(context, null);
    }

    public TimingMonitorView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimingMonitorView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    ArrayList<BarEntry> valuesLeft = new ArrayList<>();
    ArrayList<BarEntry> valuesRight = new ArrayList<>();
    ArrayList<Entry> valueLine = new ArrayList<>();
    ArrayList<Float> tempDiffs = new ArrayList<>();

    private float maxValue = 42f;
    private float minValue = 10f;
    private float maxDiffValue = 2f;
    private float minDiffValue = -1f;
    private final float normalTemp = 35.0f;

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.layout_timing_monitor, this);
        ButterKnife.bind(this, view);
        initChart();
    }


    public void updateUI(List<JsonDataBean> jsonDataBeans) {
        if (RxDataUtils.isEmpty(jsonDataBeans)) return;
        tempDiffs.clear();
        valuesLeft.clear();
        valuesRight.clear();
        valueLine.clear();

        for (int i = 0; i < jsonDataBeans.size(); i++) {
            if (i < 8) {
                valuesLeft.add(new BarEntry(i, (float) jsonDataBeans.get(i).getNodeTemp()));
            } else {
                valuesRight.add(new BarEntry((i - 8), (float) jsonDataBeans.get(i).getNodeTemp()));
            }
        }

        for (int i = 0; i < valuesRight.size(); i++) {
            float tempDiff = (float) RxFormatValue.format4S5R(valuesLeft.get(i).getY() - valuesRight.get(i).getY(), 1);
            tempDiffs.add(Math.abs(tempDiff));
            valueLine.add(new Entry(i, Math.abs(tempDiff)));
        }

        mMBarChart.getXAxis().setValueFormatter((value, axis) ->
                tempDiffs.get(Math.min(Math.max((int) value, 0), tempDiffs.size() - 1)) + "");


        setData();

        setLine();

        initTemp();

    }

    private void initTemp() {
        mTvL1.setText(valuesLeft.get(0).getY() + "");
        mTvL2.setText(valuesLeft.get(1).getY() + "");
        mTvL3.setText(valuesLeft.get(2).getY() + "");
        mTvL4.setText(valuesLeft.get(3).getY() + "");
        mTvL5.setText(valuesLeft.get(4).getY() + "");
        mTvL6.setText(valuesLeft.get(5).getY() + "");
        mTvL7.setText(valuesLeft.get(6).getY() + "");
        mTvL8.setText(valuesLeft.get(7).getY() + "");


        mTvR1.setText(valuesRight.get(0).getY() + "");
        mTvR2.setText(valuesRight.get(1).getY() + "");
        mTvR3.setText(valuesRight.get(2).getY() + "");
        mTvR4.setText(valuesRight.get(3).getY() + "");
        mTvR5.setText(valuesRight.get(4).getY() + "");
        mTvR6.setText(valuesRight.get(5).getY() + "");
        mTvR7.setText(valuesRight.get(6).getY() + "");
        mTvR8.setText(valuesRight.get(7).getY() + "");

    }


    private void initChart() {
        //替换系统的渲染器，设置成圆角柱状图
        mMBarChart.setRenderer(new BarRoundChartRenderer(
                mMBarChart,
                mMBarChart.getAnimator(),
                mMBarChart.getViewPortHandler(),
                true,
                10f
        ));
        mMBarChart.setNoDataText("");
        mMBarChart.getDescription().setEnabled(false);
        mMBarChart.getLegend().setEnabled(false);
        mMBarChart.setHighlightFullBarEnabled(false);
        mMBarChart.setScaleEnabled(false);
        mMBarChart.setEnabled(false);

        YAxis yAxis = mMBarChart.getAxisLeft();
        yAxis.setEnabled(false);
        yAxis.setAxisMaximum(maxValue);
        yAxis.setAxisMinimum(minValue);

        YAxis yRAxis = mMBarChart.getAxisRight();
        yRAxis.setEnabled(false);

        XAxis xAxis = mMBarChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.parseColor("#FD74B4"));
        xAxis.setTextSize(10f);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(8);
        xAxis.setLabelCount(8);


        //初始化线条
        mLineChart.setNoDataText("");
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setViewPortOffsets(RxUtils.dp2px(20), 0, RxUtils.dp2px(20), RxUtils.dp2px(5));

        mLineChart.getAxisLeft().setEnabled(false);

        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.getXAxis().setEnabled(false);

    }


    private void setLine() {
//        mLineChart.getAxisLeft().setAxisMaximum(maxDiffValue);
        mLineChart.getAxisLeft().setAxisMinimum(minDiffValue);

        LineDataSet set;
        if (mLineChart.getData() != null) {
            set = (LineDataSet) mLineChart.getData().getDataSetByLabel("lineChart", false);
            set.setValues(valueLine);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(valueLine, "lineChart");
            set.setColor(Color.parseColor("#FD74B4"));
            set.setLineWidth(1f);
            set.setDrawFilled(true);
            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(mMBarChart.getContext(), R.drawable.fade_red);
                set.setFillDrawable(drawable);
            } else {
                set.setFillColor(Color.BLACK);
            }
            set.setHighlightEnabled(false);
            set.setDrawCircleHole(false);
            set.setDrawCircles(false);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            set.setMode(LineDataSet.Mode.LINEAR);
            set.setDrawValues(false);
            LineData d = new LineData();
            d.addDataSet(set);

            mLineChart.setData(d);
        }

        mLineChart.invalidate();
    }

    private void setData() {
        BarDataSet set, set2;
        int green = Color.parseColor("#6589FF");
        int red = Color.parseColor("#FE68AE");

        if (mMBarChart.getData() != null &&
                mMBarChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) mMBarChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mMBarChart.getData().getDataSetByIndex(1);
            set.setValues(valuesLeft);
            set2.setValues(valuesRight);
            mMBarChart.getData().notifyDataChanged();
            mMBarChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(valuesLeft, "barLeft");
            set.setColor(green);
            set.setDrawValues(false);
            set.setHighlightEnabled(false);

            set2 = new BarDataSet(valuesRight, "barRight");
            set2.setColor(red);
            set2.setDrawValues(false);
            set2.setHighlightEnabled(false);

            BarData data = new BarData(set, set2);
            data.setBarWidth(0.2f);

            mMBarChart.setData(data);
        }

        float groupSpace = 0.3f;
        float barSpace = 0.15f; // x4 DataSet
        mMBarChart.groupBars(0, groupSpace, barSpace);

        mMBarChart.invalidate();

    }


}
