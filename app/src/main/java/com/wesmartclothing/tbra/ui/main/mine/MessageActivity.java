package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.MessageBean;
import com.wesmartclothing.tbra.tools.GlideImageLoader;

import butterknife.BindView;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerMessage)
    RecyclerView mRecyclerMessage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int layoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initRecyclerView();
        initTitle(mRxTitle);
        mRxTitle.setRightTextOnClickListener(view -> {

        });
    }

    private void initRecyclerView() {
        mRecyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter adapter = new BaseQuickAdapter<MessageBean.ListBean, BaseViewHolder>(R.layout.item_message) {

            @Override
            protected void convert(BaseViewHolder helper, MessageBean.ListBean item) {
                GlideImageLoader.getInstance().displayImage(mActivity, R.mipmap.ic_launcher, helper.getView(R.id.iv_img));
                helper.setVisible(R.id.iv_redDot, item.getReadState() == 0)
                        .setText(R.id.tv_title, item.getTitle())
                        .setText(R.id.tv_date, RxFormat.setFormatDate(item.getPushTime(), RxFormat.Date_Date2))
                        .setText(R.id.tv_content, item.getContent())
                        .addOnClickListener(R.id.img_delete);
            }
        };
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if (view.getId() == R.id.img_delete) {

            }
        });
//        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshLayout) {
//                initData();
//            }
//
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                pageNum = 1;
//                initData();
//            }
//        });

        mRecyclerMessage.setAdapter(adapter);
        adapter.addData(new MessageBean.ListBean());
        adapter.addData(new MessageBean.ListBean());
        adapter.addData(new MessageBean.ListBean());
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
