package com.wesmartclothing.tbra.ui.guide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jack
 * @date on 2018/12/27
 * @describe TODO启动页
 * @org 智裳科技
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.mRxTitleBar)
    RxTitle mMRxTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        RxActivityUtils.skipActivity(this, MainActivity.class);


//        String signatureSHA1 = RxSignaturesUtils.signatureSHA1(this.getApplication());
//        RxLogUtils.e("SHA1：" + signatureSHA1);

        mMRxTitleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RxNetManager.getInstance().doNetSubscribe(
//                        NetManager.getApiService().fetchWeightInfo(1, 10),
//                        lifecycleSubject,
//                        "fetchWeightInfo",
//                        WeightDataBean.class,
//                        CacheStrategy.firstCache())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new RxNetSubscriber<WeightDataBean>() {
//                            @Override
//                            protected void _onNext(WeightDataBean weightDataBean) {
//                                RxLogUtils.d("当前线程：" + Thread.currentThread().getName());
//                                RxLogUtils.d("getWeight:" + weightDataBean.getWeight());
//                            }
//                        });
            }
        });


    }


}
