package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxEditText;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.WarningRuleBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.zchu.rxcache.stategy.CacheStrategy;

import butterknife.BindView;
import butterknife.OnClick;

public class WarningSettingActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.ruler_temp)
    DecimalScaleRulerView mRulerTemp;
    @BindView(R.id.tv_type)
    RxTextView mTvType;
    @BindView(R.id.edit_frequency)
    RxEditText mEditFrequency;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;

    private float defaultTemp = 0.5f;
    private WarningRuleBean mWarningRuleBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_warning_setting;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initRuleView();
    }

    private void initRuleView() {
        mRulerTemp.setTextLabel("℃");
        mRulerTemp.setColor(ContextCompat.getColor(mContext, R.color.line_C3C5CA),
                ContextCompat.getColor(mContext, R.color.line_C3C5CA), ContextCompat.getColor(mContext, R.color.colortheme));
        mRulerTemp.setParam(DrawUtil.dip2px(10), DrawUtil.dip2px(60), DrawUtil.dip2px(40),
                DrawUtil.dip2px(25), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mRulerTemp.initViewParam(defaultTemp, 0f, 2f, 1);
        mRulerTemp.setValueChangeListener(value -> mWarningRuleBean.setTempNum(value));
    }

    @Override
    public void initNetData() {
        //优先缓存，因为大部分情况下都是本地已经有数据了
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().userRuleDetail(),
                lifecycleSubject,
                "userRuleDetail",
                WarningRuleBean.class,
                CacheStrategy.firstRemote()
        )
                .subscribe(new RxNetSubscriber<WarningRuleBean>() {
                    @Override
                    protected void _onNext(WarningRuleBean warningRuleBean) {
                        mWarningRuleBean = warningRuleBean;
                        mRulerTemp.initViewParam((float) mWarningRuleBean.getTempNum(), 0f, 2f, 1);
                        mTvType.setText(mWarningRuleBean.getPointType().equals("1") ? "单点累计异常次数" : "多点累计异常次数");
                        mEditFrequency.setText(mWarningRuleBean.getBaseNum() + "");
                    }
                });
    }

    @Override
    public void initRxBus2() {

    }

    private void submitWarningRule() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().addUserRule(mWarningRuleBean)
                , lifecycleSubject)
                .compose(RxComposeTools.<String>showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String bean) {
                        onBackPressed();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    @OnClick({R.id.tv_type, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
//                List<String> list = new ArrayList<>();
//                list.add("单点累计异常次数");
//                list.add("多点累计异常次数");
//                BottomMenu.show((AppCompatActivity) mContext, list,
//                        (text, index) -> {
//                            mWarningRuleBean.setPointType((index + 1) + "");
//                            mTvType.setText(mWarningRuleBean.getPointType().equals("1") ? "单点累计异常次数" : "多点累计异常次数");
//                        }, true);

                break;
            case R.id.tv_complete:
                int stringToInt = RxDataUtils.stringToInt(mEditFrequency.getText().toString());
                if (stringToInt <= 0 || stringToInt > 100) {
                    RxToast.normal("请输入1～100数字");
                    return;
                }
                //TODO 这里暂时没有多点，直接设置为单点
                mWarningRuleBean.setPointType("1");
                mWarningRuleBean.setBaseNum(stringToInt);
                submitWarningRule();
                break;
        }
    }
}
