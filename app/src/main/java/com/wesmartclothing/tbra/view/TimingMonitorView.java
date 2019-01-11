package com.wesmartclothing.tbra.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRandom;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.tools.BarRoundChartRenderer;

import java.util.ArrayList;

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

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.layout_timing_monitor, this);
        ButterKnife.bind(this, view);

        initChart();

        updateUI(testData());
    }


    private float[][] testData() {
        float[][] testData = new float[2][8];
        float[] leftValue = new float[8];
        float[] rightValue = new float[8];
        for (int i = 0; i < 8; i++) {
            leftValue[i] = 1000f;
            rightValue[i] = 500f;
        }
        testData[0] = leftValue;
        testData[1] = rightValue;
        return testData;
    }

    public void updateUI(float[][] temps) {
        if (temps.length != 2) {
            RxLogUtils.e("数据源不正确");
        }

        valuesLeft.clear();
        valuesRight.clear();
        for (int j = 0; j < temps[0].length; j++) {
            valuesLeft.add(new BarEntry(j, temps[0][j]));
        }
        for (int j = 0; j < temps[1].length; j++) {
            valuesRight.add(new BarEntry(j, temps[1][j]));
        }

        for (int i = 0; i < temps[0].length; i++) {
            valueLine.add(new Entry(i, RxRandom.getRandom(500, 1000)));
        }

        setData();

        setLine();
    }


    private void initChart() {
        //替换系统的渲染器，设置成圆角柱状图
        mMBarChart.setRenderer(new BarRoundChartRenderer(
                mMBarChart,
                mMBarChart.getAnimator(),
                mMBarChart.getViewPortHandler(),
                true,
                150f
        ));
        mMBarChart.getDescription().setEnabled(false);
        mMBarChart.getLegend().setEnabled(false);
        mMBarChart.setHighlightFullBarEnabled(false);
        mMBarChart.setScaleEnabled(false);
        mMBarChart.setEnabled(false);

        YAxis yAxis = mMBarChart.getAxisLeft();
        yAxis.setEnabled(false);
        yAxis.setAxisMaximum(1200f);
        yAxis.setAxisMinimum(0f);

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
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                return data.get(Math.min(Math.max((int) value, 0), data.size() - 1)).xAxisValue;
                return "0.5";
            }
        });


        //初始化线条
        mLineChart.getDescription().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setViewPortOffsets(0, 0, 0, 0);

        mLineChart.getAxisLeft().setEnabled(false);


        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.getXAxis().setEnabled(false);


    }


    private void setLine() {
        mLineChart.getAxisLeft().setAxisMaximum(1200f);
        mLineChart.getAxisLeft().setAxisMinimum(0f);
        LineDataSet set;
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set.setValues(valueLine);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(valueLine, "Line");
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
            set.setDrawValues(false);
            LineData d = new LineData();
            d.addDataSet(set);

            mLineChart.setData(d);
        }

        mLineChart.invalidate();
    }

    private void setData() {
        YAxis yAxis = mMBarChart.getAxisLeft();
        yAxis.setAxisMaximum(1200f);
        yAxis.setAxisMinimum(0f);

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

            float groupSpace = 0.3f;
            float barSpace = 0.15f; // x4 DataSet
            mMBarChart.groupBars(0, groupSpace, barSpace);
        }

        mMBarChart.invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
