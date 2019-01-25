package com.wesmartclothing.tbra.ui.main.mine.relationphone;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;

import butterknife.BindView;

public class AddRelationPhoneActivity extends BaseActivity {

    @BindView(R.id.parent)
    LinearLayout mParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_add_relation_phone;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        String url = "http://mc.vip.qq.com/demo/indexv3";

//        FragmentUtils.replace(getSupportFragmentManager(), BaseWebFragment.getInstance(url), R.id.parent);


    }



    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
