package com.wesmartclothing.tbra.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.entity.SingleHistoryPointBean;
import com.wesmartclothing.tbra.tools.BarRoundChartRenderer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Package com.wesmartclothing.tbra.view
 * @FileName HistoryTempDetailView
 * @Date 2019/1/29 15:43
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class HistoryTempDetailView extends RelativeLayout {


    public interface OnChartSelectListener {

        void select(SingleHistoryPointBean.ListBean bean);
    }

    @BindView(R.id.layout_legend)
    LinearLayout mLayoutLegend;
    @BindView(R.id.lineChart)
    LineChart mLineChart;
    @BindView(R.id.mBarChart)
    BarChart mMBarChart;

    ArrayList<BarEntry> valuesLeft = new ArrayList<>();
    ArrayList<BarEntry> valuesRight = new ArrayList<>();
    ArrayList<Entry> diffLine = new ArrayList<>();
    ArrayList<Entry> normalLine = new ArrayList<>();
    ArrayList<String> collectDateLists = new ArrayList<>();

    private OnChartSelectListener mOnChartSelectListener;
    private float maxValue = 45f;
    private float minValue = 0f;
    private final float normalTemp = 20.0f;

    public HistoryTempDetailView(Context context) {
        this(context, null);
    }

    public HistoryTempDetailView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryTempDetailView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.layout_history_monitor_detail, this);
        ButterKnife.bind(this, view);
        initView();
    }


    private void initView() {
        initChart();
    }

    public void updateUI(List<SingleHistoryPointBean.ListBean> list) {
        if (RxDataUtils.isEmpty(list)) return;
        collectDateLists.clear();
        valuesLeft.clear();
        valuesRight.clear();
        diffLine.clear();
        normalLine.clear();

        for (int i = 0; i < list.size(); i++) {
            SingleHistoryPointBean.ListBean bean = list.get(i);
            RxLogUtils.d("点位数据:" + bean.toString());
            valuesLeft.add(new BarEntry(i, (float) bean.getLeftTemp()));
            valuesRight.add(new BarEntry(i, (float) bean.getRightTemp()));
            collectDateLists.add(RxFormat.setFormatDate(bean.getCollectDate(), "MM-dd"));

            float tempDiff = valuesLeft.get(i).getY() - valuesRight.get(i).getY();
            diffLine.add(new Entry(i, (float) (normalTemp + tempDiff)));
            normalLine.add(new Entry(i, (float) bean.getAvgTemp()));
        }


        mMBarChart.getXAxis().setValueFormatter((value, axis) ->
                collectDateLists.get(Math.min(Math.max((int) value, 0), collectDateLists.size() - 1)));

        setData();

        setLine();

        if (mOnChartSelectListener != null) {
            mOnChartSelectListener.select(list.get(0));
        }

        mMBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (mOnChartSelectListener != null) {
//                    RxLogUtils.d("点击下标：" + ((int) e.getX()) / 2 + "index:" + (int) e.getX());
                    mOnChartSelectListener.select(list.get(Math.min(Math.max((int) e.getX(), 0), list.size() - 1)));
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

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
        mMBarChart.setNoDataText("");
        mMBarChart.getDescription().setEnabled(false);
        mMBarChart.getLegend().setEnabled(false);
        mMBarChart.setHighlightFullBarEnabled(false);
        mMBarChart.setScaleEnabled(false);
        mMBarChart.setEnabled(true);


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
//        xAxis.setAxisMaximum(8);
//        xAxis.setLabelCount(8);

        mMBarChart.getAxisLeft().setAxisMaximum(maxValue);
        mMBarChart.getAxisLeft().setAxisMinimum(minValue);


        //初始化线条
        mLineChart.setNoDataText("没有实时监测数据，\n穿上监测内衣试试吧!");
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setNoDataTextColor(ContextCompat.getColor(mLineChart.getContext(), R.color.color_444A59));
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setViewPortOffsets(0, 0, 0, 0);

        mLineChart.getAxisLeft().setEnabled(false);

        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.getXAxis().setEnabled(false);

        mLineChart.getAxisLeft().setAxisMaximum(maxValue);
        mLineChart.getAxisLeft().setAxisMinimum(minValue);
    }

    private void setLine() {
        LineDataSet set, set2;


        if (mLineChart.getData() != null) {
            set = (LineDataSet) mLineChart.getData().getDataSetByLabel("tempDiffLine", false);
            set2 = (LineDataSet) mLineChart.getData().getDataSetByLabel("tempNormalLine", false);
            set.setValues(diffLine);
            set2.setValues(normalLine);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(diffLine, "tempDiffLine");
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

            set2 = new LineDataSet(normalLine, "tempNormalLine");
            set2.setColor(Color.parseColor("#FD74B4"));
            set2.setLineWidth(1f);
            set2.setDrawFilled(false);

            set2.setHighlightEnabled(false);
            set2.setDrawCircleHole(false);
            set2.setDrawCircles(false);
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2.setDrawValues(false);
            set2.enableDashedLine(10f, 5f, 0);

            LineData d = new LineData();
            d.addDataSet(set);
            d.addDataSet(set2);

            mLineChart.setData(d);
        }

        mLineChart.invalidate();
    }

    private void setData() {
        BarDataSet set, set2;

        XAxis xAxis = mMBarChart.getXAxis();
        xAxis.setAxisMaximum(8);
        xAxis.setLabelCount(8);
//        xAxis.setAxisMaximum(Math.min(valuesLeft.size(), 8));
//        xAxis.setLabelCount(Math.min(valuesLeft.size(), 8));


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
            set.setColor(Color.parseColor("#6589FF"));
            set.setDrawValues(false);
            set.setHighlightEnabled(true);

            set2 = new BarDataSet(valuesRight, "barRight");
            set2.setColor(Color.parseColor("#FE68AE"));
            set2.setDrawValues(false);
            set2.setHighlightEnabled(true);

            BarData data = new BarData(set, set2);
            data.setBarWidth(0.2f);

            mMBarChart.setData(data);
        }

        float groupSpace = 0.3f;
        float barSpace = 0.15f; // x4 DataSet
        mMBarChart.groupBars(0, groupSpace, barSpace);

        mMBarChart.invalidate();

    }

    public void setOnChartSelectListener(OnChartSelectListener onChartSelectListener) {
        mOnChartSelectListener = onChartSelectListener;
    }
}
