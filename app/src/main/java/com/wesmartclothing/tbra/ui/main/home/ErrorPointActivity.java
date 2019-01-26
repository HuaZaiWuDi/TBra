package com.wesmartclothing.tbra.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.JsonDataBean;
import com.wesmartclothing.tbra.entity.PointDataBean;
import com.wesmartclothing.tbra.entity.RecordBean;
import com.wesmartclothing.tbra.entity.SinglePointBean;
import com.wesmartclothing.tbra.entity.SingleSelectBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.MapSortUtil;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
    private Map<String, List<SinglePointBean>> errorMap = new HashMap<>();

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
//        mRxTitle.setTag(latestType);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().latestSingleData(new RecordBean(latestType))
                , lifecycleSubject, latestType,
                new TypeToken<List<PointDataBean>>() {
                }.getType(),
                CacheStrategy.onlyCache())
                .subscribe(new RxNetSubscriber<List<PointDataBean>>() {
                    @Override
                    protected void _onNext(List<PointDataBean> list) {
                        updateUI(list);
                    }
                });
    }

    private void updateUI(List<PointDataBean> pointDatalist) {
        int errorTotal = 0;

        Gson gson = new Gson();
        for (PointDataBean bean : pointDatalist) {
            errorTotal += bean.getUnusualNum();
            List<JsonDataBean> errorPointlist = gson.fromJson(bean.getUnusualPoints(), new TypeToken<List<JsonDataBean>>() {
            }.getType());

            for (JsonDataBean errorBean : errorPointlist) {
                List<SinglePointBean> singlePointBeans = errorMap.get(errorBean.getNodeName());
                if (singlePointBeans == null) {
                    singlePointBeans = new ArrayList<>();
                    errorMap.put(errorBean.getNodeName(), singlePointBeans);
                }
                singlePointBeans.add(new SinglePointBean(bean.getCollectTime(), errorBean.getNodeName(), errorBean.getNodeTemp()));
            }
        }

        errorMap = MapSortUtil.sortMapByKey(errorMap);
        List<SingleSelectBean> beans = new ArrayList<>();
        for (String s : errorMap.keySet()) {
            beans.add(new SingleSelectBean(s));
        }
        if (!RxDataUtils.isEmpty(beans))
            beans.get(0).setSelect(true);
        errorPointAdapter.setNewData(beans);
        if (!RxDataUtils.isEmpty(beans))
            errorPointDetailAdapter.setNewData(errorMap.get(beans.get(0).getText()));

        if (!RxDataUtils.isEmpty(pointDatalist))
            mTvMonitorTime.setText("监测时段\t\t\t\t\t" + RxFormat.setFormatDate(pointDatalist.get(0).getCollectTime(), RxFormat.Date_Date2) + "～" +
                    RxFormat.setFormatDate(pointDatalist.get(pointDatalist.size() - 1).getCollectTime(), RxFormat.Date_Date2));
        mTvWarningCount.setText(errorTotal + "");
        mTvWarningPoint.setText(errorMap.size() + "");
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
                                R.mipmap.bg_btn : R.drawable.shape_gray_circle_42);
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
                errorPointDetailAdapter.setNewData(errorMap.get(curItem.getText()));
                mRecyclerErrorPoint.setTag(position);
            }
        });

        mRecyclerErrorPoint.setAdapter(errorPointAdapter);
    }

    private void initPointDetail() {
        mRecyclerWarningPoint.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerWarningPoint.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        errorPointDetailAdapter = new BaseQuickAdapter<SinglePointBean, BaseViewHolder>(R.layout.item_warning_point) {
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
