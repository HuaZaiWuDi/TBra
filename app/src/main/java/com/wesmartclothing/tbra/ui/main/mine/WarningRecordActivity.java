package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.WarningRecordBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;

import butterknife.BindView;

public class WarningRecordActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerWarning)
    RecyclerView mRecyclerWarning;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;

    private BaseQuickAdapter adapter;
    private int pageNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_warning_record;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initRecyclerView();
        initTitle(mRxTitle);
        mRxTitle.setRightTextOnClickListener(view -> {
            allReaded();
        });
    }

    private void initRecyclerView() {
        mRecyclerWarning.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<WarningRecordBean.ListBean, BaseViewHolder>(R.layout.item_warning_record) {
            @Override
            protected void convert(BaseViewHolder helper, WarningRecordBean.ListBean item) {
                helper.setText(R.id.tv_date, RxFormat.setFormatDate(item.getWarningTime(), "yyyy/MM/dd HH:mm"))
                        .setVisible(R.id.iv_redDot, item.getReadState() == 0)
                        .addOnClickListener(R.id.img_delete)
                        .addOnClickListener(R.id.layout_record);
            }
        };
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if (view.getId() == R.id.img_delete) {

            } else if (view.getId() == R.id.layout_record) {
                readed(position);
            }
        });

        mRecyclerWarning.setAdapter(adapter);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                initNetData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNum = 1;
                initNetData();
            }
        });
    }

    @Override
    public void initNetData() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().warningInfo(pageNum, 10),
                lifecycleSubject,
                "warningInfo" + pageNum,
                WarningRecordBean.class,
                CacheStrategy.firstRemote()
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<WarningRecordBean>() {
                    @Override
                    protected void _onNext(WarningRecordBean warningRecordBean) {
                        if (pageNum == 1) {
                            adapter.setNewData(warningRecordBean.getList());
                        } else {
                            adapter.addData(warningRecordBean.getList());
                        }
                        pageNum++;
                        mSmartRefreshLayout.setEnableLoadMore(warningRecordBean.isHasNextPage());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mSmartRefreshLayout.finishLoadMore(true);
                        mSmartRefreshLayout.finishRefresh(true);
                    }
                });
    }

    private void readed(int position) {
        WarningRecordBean.ListBean bean = (WarningRecordBean.ListBean) adapter.getItem(position);
//        0-未读，1-已读
        if (bean.getReadState() == 1) return;
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().warningInfoReaded(bean),
                lifecycleSubject

        ).subscribe(new RxNetSubscriber<Integer>() {
            @Override
            protected void _onNext(Integer unReadCount) {
                bean.setReadState(1);
                adapter.setData(position, bean);
            }
        });
    }

    private void allReaded() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().warningInfoReadedAll(),
                lifecycleSubject
        ).subscribe(new RxNetSubscriber<Integer>() {
            @Override
            protected void _onNext(Integer unReadCount) {
                if (unReadCount != 0) {
                    List<WarningRecordBean.ListBean> listBeans = adapter.getData();
                    for (WarningRecordBean.ListBean bean : listBeans) {
                        bean.setReadState(1);
                    }
                    adapter.setNewData(listBeans);
                }
            }
        });
    }


    @Override
    public void initRxBus2() {

    }
}
