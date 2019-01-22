package com.wesmartclothing.tbra.ui.login;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.IllnessBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IllnessActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_nextStep)
    TextView mTvNextStep;

    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_illness;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BaseQuickAdapter<IllnessBean, BaseViewHolder>(R.layout.item_illness) {
            @Override
            protected void convert(BaseViewHolder helper, IllnessBean item) {
                helper.setText(R.id.tv_illness, item.getIllnessName())
                        .setImageResource(R.id.img_click, item.isClick() ? R.mipmap.ic_complete : 0);
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IllnessBean bean = (IllnessBean) adapter.getItem(position);
                bean.setClick(!bean.isClick());
                adapter.setData(position, bean);
                mTvNextStep.setEnabled(true);
                mTvNextStep.setAlpha(1f);
            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void initNetData() {
        requestIllness();
    }

    @Override
    public void initRxBus2() {

    }

    private void requestIllness() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().systemIllnessList()
                , lifecycleSubject)
//                .compose(RxComposeTools.<IllnessBean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<List<IllnessBean>>() {
                    @Override
                    protected void _onNext(List<IllnessBean> list) {
                        IllnessBean bean = new IllnessBean();
                        bean.setClick(false);
                        bean.setIllnessName("无");
                        list.add(bean);
                        adapter.setNewData(checkIllness(list));
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }

    private List<IllnessBean> checkIllness(List<IllnessBean> beans) {
        List<IllnessBean> list = InputInfoActivity.sInfoBean.getIllnessList();
        if (!RxDataUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < beans.size(); j++) {
                    if (beans.get(j).getIllnessName().equals(list.get(i).getIllnessName())) {
                        beans.get(j).setClick(true);
                    }
                }
            }
        }

        return beans;
    }


    @OnClick(R.id.tv_nextStep)
    public void onViewClicked() {
        List<IllnessBean> illnessBeans = new ArrayList<>();
        for (IllnessBean bean : (List<IllnessBean>) adapter.getData()) {
            if (bean.isClick() && !"无".equals(bean.getIllnessName())) {
                illnessBeans.add(bean);
            }
        }
        InputInfoActivity.sInfoBean.setIllnessList(illnessBeans);
        RxActivityUtils.skipActivity(mContext, LocationActivity.class);
    }
}
