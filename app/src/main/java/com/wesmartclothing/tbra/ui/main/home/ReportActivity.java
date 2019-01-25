package com.wesmartclothing.tbra.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.roundprogressbar.RoundProgressBar;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class ReportActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.tv_monitorTime)
    TextView mTvMonitorTime;
    @BindView(R.id.tv_warningPoint)
    TextView mTvWarningPoint;
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

    }

    @Override
    public void initViews() {
        initPie(mPieLeft);
        initPie(mPieRight);
        setData(mPieLeft, "mPieLeft");
        setData(mPieRight, "mPieRight");
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


    private void setData(PieChart chart, String label) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < 3; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 100) + 100 / 5), "01次", "01"));
        }

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


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
