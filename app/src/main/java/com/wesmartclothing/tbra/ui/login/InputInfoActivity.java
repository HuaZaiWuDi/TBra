package com.wesmartclothing.tbra.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.view.picker.CustomDatePicker;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

public class InputInfoActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.tv_year)
    TextView mTvYear;
    @BindView(R.id.tv_month)
    TextView mTvMonth;
    @BindView(R.id.tv_day)
    TextView mTvDay;
    @BindView(R.id.tv_nextStep)
    TextView mTvNextStep;

    public static UserInfoBean sInfoBean = new UserInfoBean();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_input_info;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }


    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        CustomDatePicker datePicker = new CustomDatePicker(mActivity);
        datePicker.setRangeStart(1940, 01, 01);
        datePicker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTimeInMillis(sInfoBean.getBirthday());
        RxLogUtils.d("日期：" + calendar.toString());
        RxLogUtils.d("日期：" + calendar.getTimeInMillis());
        RxLogUtils.d("日期：" + sInfoBean.getBirthday());
        datePicker.setTextColor(getResources().getColor(R.color.Gray));
        datePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
            RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);

            Date date = RxFormat.setParseDate(year + "-" + month + "-" + day, RxFormat.Date);
            sInfoBean.setBirthday(date.getTime());
            mTvNextStep.setEnabled(true);
            mTvNextStep.setAlpha(1f);

            mTvYear.setText(year + "年");
            mTvMonth.setText(month + "月");
            mTvDay.setText(day + "日");
        });
        datePicker.show();
    }


    @OnClick({R.id.tv_year, R.id.tv_month, R.id.tv_day, R.id.tv_nextStep})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_year:
            case R.id.tv_month:
            case R.id.tv_day:
                showDate();
                break;
            case R.id.tv_nextStep:
                RxActivityUtils.skipActivity(mContext, IllnessActivity.class);
                break;
        }
    }
}
