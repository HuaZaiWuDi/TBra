package com.wesmartclothing.tbra.ui.main.mine.relationphone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxKeyboardUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.SwitchView;
import com.vondear.rxtools.view.cardview.CardView;
import com.vondear.rxtools.view.layout.RxEditText;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.RelateAccountListBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;

public class RelationPhoneActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.tv_addPhone)
    TextView mTvAddPhone;
    @BindView(R.id.layout_notPhone)
    CardView mLayoutNotPhone;
    @BindView(R.id.recyclerPhone)
    RecyclerView mRecyclerPhone;
    @BindView(R.id.tv_addPhone_2)
    TextView mTvAddPhone2;
    @BindView(R.id.layout_relationPhone)
    LinearLayout mLayoutRelationPhone;
    @BindView(R.id.edit_nickName)
    RxEditText mEditNickName;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    @BindView(R.id.layout_editName)
    RelativeLayout mLayoutEditName;


    private boolean isEdit = false, isEnough = false;
    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_relation_phone;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        mRxTitle.setLeftOnClickListener(view -> onBackPressed());
        mRxTitle.setRightTextOnClickListener(view -> {
            isEdit = !isEdit;
            adapter.notifyDataSetChanged();
            mRxTitle.setRightText(!isEdit ? "编辑" : "完成");
            if (!isEnough)
                mTvAddPhone2.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        });
        initRecycler();
    }

    private void initRecycler() {
        mRecyclerPhone.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<RelateAccountListBean.ListBean, BaseViewHolder>(R.layout.item_relation_phone) {

            @Override
            protected void convert(BaseViewHolder helper, RelateAccountListBean.ListBean item) {
                helper.setGone(R.id.img_warning, isEdit)
                        .setText(R.id.tv_phoneDetail, item.getContactName() + "\t" + item.getContactPhone())
                        .setGone(R.id.switchView, !isEdit)
                        .addOnClickListener(R.id.img_warning)
                        .addOnClickListener(R.id.switchView);
                SwitchView switchView = helper.getView(R.id.switchView);
                //启用标识。0-关闭，1-启用
                switchView.setOpened(item.getUsingFlag() == 1);
            }
        };
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.switchView) {
                RxLogUtils.d("Switch");
                SwitchView switchView = (SwitchView) view;

                toggleBing(position, switchView);
            } else if (view.getId() == R.id.img_warning) {
                delete(position);
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (isEdit) {
                //修改
                RelateAccountListBean.ListBean bean = (RelateAccountListBean.ListBean) adapter.getItem(position);
                mEditNickName.setText(bean.getContactName());
                mEditNickName.setTag(position);
                mLayoutEditName.setVisibility(View.VISIBLE);
                RxKeyboardUtils.showSoftInput(mEditNickName);
            }
        });
        mRecyclerPhone.setAdapter(adapter);
    }

    private void delete(int position) {
        SelectDialog.show(mContext, "提示", "是否删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RelateAccountListBean.ListBean bean = (RelateAccountListBean.ListBean) adapter.getItem(position);
                RxManager.getInstance().doNetSubscribe(
                        NetManager.getApiService().removeRelateAccount(bean),
                        lifecycleSubject
                )
                        .subscribe(new RxNetSubscriber<String>() {
                            @Override
                            protected void _onNext(String s) {
                                adapter.remove(position);
                                isEnough = adapter.getData().size() >= 3;
                                mTvAddPhone2.setVisibility(isEnough ? View.GONE : View.VISIBLE);
                            }

                            @Override
                            protected void _onError(String error, int code) {
                                super._onError(error, code);
                                RxToast.normal(error);
                            }
                        });
            }
        });
    }

    @Override
    public void initNetData() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().relateAccountList(),
                lifecycleSubject,
                "relateAccountList",
                RelateAccountListBean.class,
                CacheStrategy.firstRemote()
        ).subscribe(new RxNetSubscriber<RelateAccountListBean>() {
            @Override
            protected void _onNext(RelateAccountListBean relateAccountListBean) {
                if (RxDataUtils.isEmpty(relateAccountListBean.getList())) {
                    mLayoutNotPhone.setVisibility(View.VISIBLE);
                } else {
                    mLayoutNotPhone.setVisibility(View.GONE);
                }
                adapter.setNewData(relateAccountListBean.getList());
                isEnough = adapter.getData().size() >= 3;
                mTvAddPhone2.setVisibility(isEnough ? View.GONE : View.VISIBLE);
            }

            @Override
            protected void _onError(String error, int code) {
                super._onError(error, code);
                RxToast.normal(error);
            }
        });
    }

    private void toggleBing(int position, SwitchView switchView) {
        RelateAccountListBean.ListBean bean = (RelateAccountListBean.ListBean) adapter.getItem(position);
        bean.setUsingFlag(switchView.isOpened() ? 1 : 0);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().openOrClose(bean),
                lifecycleSubject
        )
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                        switchView.toggleSwitch(!switchView.isOpened());
                    }
                });
    }

    private void editRelationPhone() {
        int position = (int) mEditNickName.getTag();
        RelateAccountListBean.ListBean bean = (RelateAccountListBean.ListBean) adapter.getItem(position);
        bean.setContactName(mEditNickName.getText().toString());
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().editRelateAccount(bean),
                lifecycleSubject
        )
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        adapter.setData(position, bean);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    @Override
    public void initRxBus2() {
        RxBus.getInstance().register2(RelateAccountListBean.ListBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<RelateAccountListBean.ListBean>() {
                    @Override
                    protected void _onNext(RelateAccountListBean.ListBean bean) {
                        initNetData();
                    }
                });
    }


    @OnClick({R.id.tv_addPhone, R.id.tv_addPhone_2, R.id.tv_cancel, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_addPhone:
            case R.id.tv_addPhone_2:
                RxActivityUtils.skipActivity(mContext, EditPhoneActivity.class);
                break;
            case R.id.tv_cancel:
                mLayoutEditName.setVisibility(View.GONE);
                RxKeyboardUtils.hideSoftInput(mEditNickName);
                break;
            case R.id.tv_complete:
                mLayoutEditName.setVisibility(View.GONE);
                RxKeyboardUtils.hideSoftInput(mEditNickName);
                editRelationPhone();
                break;
        }
    }

}
