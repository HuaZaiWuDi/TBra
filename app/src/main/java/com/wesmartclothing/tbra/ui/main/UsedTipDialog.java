package com.wesmartclothing.tbra.ui.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.kongzue.dialog.v2.CustomDialog;
import com.vondear.rxtools.model.lifecycyle.LifeCycleEvent;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxEditText;
import com.vondear.rxtools.view.layout.RxTextView;
import com.vondear.rxtools.view.wheelhorizontal.utils.DrawUtil;
import com.vondear.rxtools.view.wheelhorizontal.view.DecimalScaleRulerView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.entity.WarningRuleBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import io.reactivex.subjects.BehaviorSubject;

/**
 * @Package com.wesmartclothing.tbra.ui.main
 * @FileName UsedTipDialog
 * @Date 2019/1/14 11:40
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class UsedTipDialog {
    private Context mContext;
    private BehaviorSubject<LifeCycleEvent> lifecycleSubject;
    CustomDialog dialog1, dialog2, dialog3;
    private WarningRuleBean mWarningRuleBean;
    private float defaultTemp = 0.5f;

    private RxNetSubscriber<WarningRuleBean> mRxNetSubscriber;

    public UsedTipDialog(Context context, BehaviorSubject<LifeCycleEvent> lifecycleSubject) {
        this.lifecycleSubject = lifecycleSubject;
        mContext = context;
        mWarningRuleBean = new WarningRuleBean();
        firstUsedTip();
    }

    public void setRxNetSubscriber(RxNetSubscriber<WarningRuleBean> rxNetSubscriber) {
        mRxNetSubscriber = rxNetSubscriber;
    }

    private void firstUsedTip() {
        dialog1 = CustomDialog.build(mContext, View.inflate(mContext, R.layout.dialog_use_tip, null), (dialog, rootView) -> {
            rootView.findViewById(R.id.tv_complete)
                    .setOnClickListener(view -> dialog1.doDismiss());
        });
        dialog2 = CustomDialog.build(mContext, View.inflate(mContext, R.layout.dialog_wear_bra_tip, null), (dialog, rootView) -> {
            rootView.findViewById(R.id.tv_complete)
                    .setOnClickListener(view -> dialog2.doDismiss());
        });
        dialog3 = CustomDialog.build(mContext, R.layout.dialog_power_setting, (dialog, rootView) -> {
            initView(rootView);
        }).setCanCancel(true);
        dialog1.showDialog();
    }

    private void initView(View rootView) {
        final RxEditText mEditFrequency = rootView.findViewById(R.id.edit_frequency);

        TextView mTvComplete = rootView.findViewById(R.id.tv_complete);
        mTvComplete.setOnClickListener(view -> {
            int stringToInt = RxDataUtils.stringToInt(mEditFrequency.getText().toString());
            if (stringToInt <= 0 || stringToInt > 100) {
                RxToast.normal("请输入1～100数字");
                return;
            }
            mWarningRuleBean.setBaseNum(stringToInt);
            submitWarningRule();
        });

        //TODO 这里暂时没有多点，直接设置为单点
        mWarningRuleBean.setPointType("1");
        RxTextView mTvType = rootView.findViewById(R.id.tv_type);
//        mTvType.setOnClickListener(view -> {
//            List<String> list = new ArrayList<>();
//            list.add("单点累计异常次数");
//            list.add("多点累计异常次数");
//            BottomMenu.show((AppCompatActivity) mContext, list, (text, index) -> {
//                mWarningRuleBean.setPointType((index + 1) + "");
//                mTvType.setText(mWarningRuleBean.getPointType().equals("1") ? "单点累计异常次数" : "多点累计异常次数");
//            }, true);
//        });

        mWarningRuleBean.setTempNum(defaultTemp);
        DecimalScaleRulerView mRulerTemp = rootView.findViewById(R.id.ruler_temp);
        mRulerTemp.setTextLabel("℃");
        mRulerTemp.setColor(ContextCompat.getColor(mContext, R.color.line_C3C5CA),
                ContextCompat.getColor(mContext, R.color.line_C3C5CA), ContextCompat.getColor(mContext, R.color.colortheme));
        mRulerTemp.setParam(DrawUtil.dip2px(10), DrawUtil.dip2px(60), DrawUtil.dip2px(40),
                DrawUtil.dip2px(25), DrawUtil.dip2px(1), DrawUtil.dip2px(12));
        mRulerTemp.initViewParam(defaultTemp, 0f, 2f, 1);
        mRulerTemp.setValueChangeListener(value -> mWarningRuleBean.setTempNum(value));
    }


    private void submitWarningRule() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().addUserRule(mWarningRuleBean)
                , lifecycleSubject)
                .compose(RxComposeTools.<String>showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String bean) {
                        dialog3.doDismiss();
                        mRxNetSubscriber.onNext(mWarningRuleBean);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


}
