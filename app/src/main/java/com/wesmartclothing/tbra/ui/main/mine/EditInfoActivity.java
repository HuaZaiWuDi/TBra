package com.wesmartclothing.tbra.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialog.v2.TipDialog;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;

import butterknife.BindView;

public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.tv_tip)
    TextView mTvTip;

    private String title, data;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    public void initBundle(Bundle bundle) {
        this.bundle = bundle;
        title = bundle.getString(Key.BUNDLE_TITLE);
        data = bundle.getString(Key.BUNDLE_DATA);
        mEditText.setText(data);
        if ("昵称".equals(title)) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            mTvTip.setText(getString(R.string.must_input, 16));
        } else {
            mTvTip.setText(getString(R.string.must_input, 30));
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        }
        mRxTitle.setTitle(title);
    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        mRxTitle.setRightTextOnClickListener(view -> {
            save();
        });
    }

    private void save() {
        String name = mEditText.getText().toString();

        if (data.equals(name)) {
            TipDialog.show(mContext, "当前未做修改");
            return;
        }

        if ("昵称".equals(title)) {
            if (!RxRegUtils.isUsername(name)) {
                RxToast.normal("请输入正确的昵称");
                return;
            }

            bundle.putString(Key.BUNDLE_DATA, name);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(UserInfoActivity.RESULT_CODE, intent);
            onBackPressed();
        } else {
            if (RxDataUtils.isNullString(name.trim())) {
                RxToast.normal("请输入正确的签名");
                return;
            }

            bundle.putString(Key.BUNDLE_DATA, name);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(UserInfoActivity.RESULT_CODE, intent);
            onBackPressed();
        }

    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
