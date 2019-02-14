package com.wesmartclothing.tbra.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kongzue.dialog.v2.SelectDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.entity.rxbus.RefreshUserInfoBus;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.GlideImageLoader;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.view.picker.AddressPickTask;
import com.wesmartclothing.tbra.view.picker.CustomDatePicker;
import com.wesmartclothing.tbra.view.picker.CustomNumberPicker;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoActivity extends BaseActivity {

    public static final int RESULT_CODE = 500;
    public static final int REQUEST_CODE = 501;
    public static final int IMAGE_PICKER = 503;


    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.iv_userImg)
    RxImageView mIvUserImg;
    @BindView(R.id.layout_userImg)
    RxRelativeLayout mLayoutUserImg;
    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.layout_userName)
    RxRelativeLayout mLayoutUserName;
    @BindView(R.id.tv_userSex)
    TextView mTvUserSex;
    @BindView(R.id.layout_userSex)
    RxRelativeLayout mLayoutUserSex;
    @BindView(R.id.tv_Birth)
    TextView mTvBirth;
    @BindView(R.id.layout_userBirth)
    RxRelativeLayout mLayoutUserBirth;
    @BindView(R.id.tv_userHeight)
    TextView mTvUserHeight;
    @BindView(R.id.layout_userHeight)
    RxRelativeLayout mLayoutUserHeight;
    @BindView(R.id.tv_userWeight)
    TextView mTvUserWeight;
    @BindView(R.id.layout_userWeight)
    RxRelativeLayout mLayoutUserWeight;
    @BindView(R.id.tv_userCity)
    TextView mTvUserCity;
    @BindView(R.id.layout_userCity)
    RxRelativeLayout mLayoutUserCity;
    @BindView(R.id.tv_userSign)
    TextView mTvUserSign;
    @BindView(R.id.layout_userSign)
    RxRelativeLayout mLayoutUserSign;


    private UserInfoBean userInfo;
    private boolean userImgIsChange = false;//是否需要上传头像
    private boolean userInfoChange = false;//是否需要上传头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        mRxTitle.setRightTextOnClickListener(view -> {
            if (userInfoChange) {
                if (userImgIsChange) {
                    uploadImage();
                } else {
                    requestSaveUserInfo();
                }
            } else {
                TipDialog.show(mContext, "信息没有更改");
            }
        });
        initImagePicker();
    }

    /**
     * 头像图片选择
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(GlideImageLoader.getInstance());   //设置图片加载器
        imagePicker.setShowCamera(true);//显示拍照按钮
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    /**
     * 选择日期
     */
    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        CustomDatePicker datePicker = new CustomDatePicker(mActivity);
        datePicker.setRangeStart(1940, 01, 01);
        datePicker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTimeInMillis(userInfo.getBirthday());
        datePicker.setTextColor(getResources().getColor(R.color.Gray));
        datePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
            RxLogUtils.d("年：" + year + "------月：" + month + "---------日：" + day);
            mTvBirth.setText(year + "-" + month + "-" + day);
            Date date = RxFormat.setParseDate(year + "-" + month + "-" + day, RxFormat.Date);
            userInfo.setBirthday(date.getTime());
            userInfoChange = true;
        });
        datePicker.show();
    }

    /**
     * 选择用户地址
     */
    private void showAddress() {
        AddressPickTask task = new AddressPickTask(mActivity);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                RxLogUtils.e("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                mTvUserCity.setText(province.getName() + "," + city.getName());
                userInfo.setProvince(province.getName());
                userInfo.setCity(city.getName());
                userInfoChange = true;
            }
        });
        task.execute(RxDataUtils.isNullString(userInfo.getProvince()) ? "" : userInfo.getProvince(),
                RxDataUtils.isNullString(userInfo.getCity()) ? "" : userInfo.getCity());
    }

    /**
     * 选择身高
     */
    public void showHeight() {
        CustomNumberPicker picker = new CustomNumberPicker(mActivity);
        picker.setTextColor(getResources().getColor(R.color.Gray));
        picker.setRange(120, 200, 1);//数字范围
        picker.setSelectedItem(userInfo.getHeight());
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                RxLogUtils.d("身高：" + item);
                mTvUserHeight.setText(item + "cm");
                userInfo.setHeight((int) item);
                userInfoChange = true;
            }
        });
        picker.show();
    }


    @Override
    public void initNetData() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().userInfo(),
                lifecycleSubject,
                SPKey.SP_UserInfo,
                UserInfoBean.class,
                CacheStrategy.firstCache()
        )
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfoBean) {
                        userInfo = userInfoBean;
                        GlideImageLoader.getInstance().displayImage(mContext, userInfoBean.getAvatar(), mIvUserImg);
                        mTvUserName.setText(userInfoBean.getUserName());
                        mTvBirth.setText(RxFormat.setFormatDate(userInfoBean.getBirthday(), RxFormat.Date));
                        mTvUserHeight.setText(userInfoBean.getHeight() + "cm");
                        mTvUserCity.setText(userInfoBean.getProvince() + "," + userInfoBean.getCity());
                        mTvUserSign.setText(userInfoBean.getSignature());
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.normal(error);
                    }
                });
    }


    /**
     * 上传头像
     */
    private void uploadImage() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(RxImageUtils.compressImage(userInfo.getAvatar())));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", userInfo.getAvatar(), requestFile);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().uploadUserImg(body),
                lifecycleSubject
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        userInfo.setAvatar(s);
                        userInfoChange = true;
                        requestSaveUserInfo();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    /**
     * 上传用户数据
     */
    private void requestSaveUserInfo() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().saveUserInfo(userInfo),
                lifecycleSubject)
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        TipDialog.show(mContext, "保存成功", TipDialog.TYPE_FINISH);
                        RxCache.getDefault().save(SPKey.SP_UserInfo, userInfo)
                                .subscribe(new RxNetSubscriber<Boolean>() {
                                    @Override
                                    protected void _onNext(Boolean aBoolean) {
                                        RxLogUtils.d("保存用户信息是否成功:" + aBoolean);
                                        //刷新数据
                                        RxBus.getInstance().post(new RefreshUserInfoBus(userInfo));
                                    }
                                });

                        RxActivityUtils.finishActivity();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    @Override
    public void initRxBus2() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                GlideImageLoader.getInstance().displayImage(mActivity, images.get(0).path, R.mipmap.ic_launcher, mIvUserImg);

                //成功后将本地图片设置到imageView中，并在退回到个人中心时，刷新贴图url
                userInfo.setAvatar(images.get(0).path);
                userImgIsChange = true;
                userInfoChange = true;
            }
        } else if (resultCode == UserInfoActivity.RESULT_CODE && requestCode == UserInfoActivity.REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            String title = bundle.getString(Key.BUNDLE_TITLE);
            if ("昵称".equals(title)) {
                mTvUserName.setText(bundle.getString(Key.BUNDLE_DATA));
                userInfo.setUserName(bundle.getString(Key.BUNDLE_DATA));
            } else {
                mTvUserSign.setText(bundle.getString(Key.BUNDLE_DATA));
                userInfo.setSignature(bundle.getString(Key.BUNDLE_DATA));
            }
            userInfoChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        RxLogUtils.d("用户数据：" + userInfo.toString());
        if (userInfoChange) {
            SelectDialog.show(mContext, "提示", "您已经修改信息\n是否保存？"
                    , "保存", (dialogInterface, i) -> {
                        if (userImgIsChange) {
                            uploadImage();
                        } else {
                            requestSaveUserInfo();
                        }
                    }, "退出", (dialogInterface, i) -> {
                        RxActivityUtils.finishActivity();
                    });
        } else
            super.onBackPressed();
    }


    @OnClick({R.id.layout_userImg, R.id.layout_userName, R.id.layout_userSex, R.id.layout_userBirth, R.id.layout_userHeight, R.id.layout_userWeight, R.id.layout_userCity, R.id.layout_userSign})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.layout_userImg:
                Intent intent = new Intent(mContext, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.layout_userName:
                bundle.putString(Key.BUNDLE_TITLE, "昵称");
                bundle.putString(Key.BUNDLE_DATA, userInfo.getUserName());
                RxActivityUtils.skipActivityForResult(mActivity, EditInfoActivity.class, bundle, UserInfoActivity.REQUEST_CODE);
                break;
            case R.id.layout_userSex:
                break;
            case R.id.layout_userBirth:
                showDate();
                break;
            case R.id.layout_userHeight:
                showHeight();
                break;
            case R.id.layout_userWeight:
                break;
            case R.id.layout_userCity:
                showAddress();
                break;
            case R.id.layout_userSign:
                bundle.putString(Key.BUNDLE_TITLE, "签名");
                bundle.putString(Key.BUNDLE_DATA, userInfo.getSignature());
                RxActivityUtils.skipActivityForResult(mActivity, EditInfoActivity.class, bundle, UserInfoActivity.REQUEST_CODE);
                break;
        }
    }
}
