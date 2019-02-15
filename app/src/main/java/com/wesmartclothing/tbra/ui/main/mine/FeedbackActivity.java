package com.wesmartclothing.tbra.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kongzue.dialog.v2.BottomMenu;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxRelativeLayout;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.FeedBackBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.GlideImageLoader;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.wesmartclothing.tbra.ui.main.mine.UserInfoActivity.IMAGE_PICKER;
import static com.wesmartclothing.tbra.ui.main.mine.UserInfoActivity.REQUEST_CODE;
import static com.wesmartclothing.tbra.ui.main.mine.UserInfoActivity.RESULT_CODE;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.btn_type)
    RxTextView mBtnType;
    @BindView(R.id.btn_times)
    RxTextView mBtnTimes;
    @BindView(R.id.edit_proble)
    EditText mEditProble;
    @BindView(R.id.tv_InputCount)
    TextView mTvInputCount;
    @BindView(R.id.layout_problem)
    RxRelativeLayout mLayoutProblem;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.layout_phone)
    RxRelativeLayout mLayoutPhone;
    @BindView(R.id.tv_imgs_title)
    TextView mTvImgsTitle;
    @BindView(R.id.recycler_imgs)
    RecyclerView mRecyclerImgs;
    @BindView(R.id.btn_submit)
    RxTextView mBtnSubmit;

    private List<String> problemType, problemTimes;
    private BaseQuickAdapter adapter;
    private ArrayList<ImageItem> imageLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initImagePicker();
        initRecyclerView();
        listener();
        problemType = Arrays.asList(getResources().getStringArray(R.array.problemType));
        problemTimes = Arrays.asList(getResources().getStringArray(R.array.problemTimes));
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(GlideImageLoader.getInstance());   //设置图片加载器
        imagePicker.setShowCamera(false);//显示拍照按钮
        imagePicker.setMultiMode(true);
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(4);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void initRecyclerView() {
        mRecyclerImgs.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter = new BaseQuickAdapter<Object, BaseViewHolder>(R.layout.item_choose_img) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                if (item instanceof Integer)
                    GlideImageLoader.getInstance().displayImage(mActivity, item, helper.getView(R.id.iv_img));
                else {
                    GlideImageLoader.getInstance().displayImage(mActivity, ((ImageItem) item).path, helper.getView(R.id.iv_img));
                }
            }
        };
        mRecyclerImgs.setAdapter(adapter);
        adapter.addData(R.mipmap.icon_add_white);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Object img = adapter.getData().get(position);
            if (img instanceof ImageItem) {
//                ImageItem imageItem = (ImageItem) img;
//                RxDialogScaleView scaleView = new RxDialogScaleView(mActivity);
//                scaleView.setImagePath(imageItem.path);
//                scaleView.show();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Key.BUNDLE_DATA, imageLists);
                RxActivityUtils.skipActivityForResult(mActivity, PhotoDetailActivity.class, bundle, UserInfoActivity.REQUEST_CODE);

            } else if (img instanceof Integer) {
                Intent intent = new Intent(mContext, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageLists);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
    }

    private boolean checkData() {
        if (problemType.indexOf(mBtnType.getText().toString()) < 0) {
            mBtnType.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.red));
            return false;
        }
        if (problemTimes.indexOf(mBtnTimes.getText().toString()) < 0) {
            mBtnTimes.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.red));
            return false;
        }
        if (mEditProble.getText().length() <= 0) {
            mLayoutProblem.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.red));
            return false;
        }
        if (!RxRegUtils.isMobileExact(mEditPhone.getText().toString()) && !RxRegUtils.isEmail(mEditPhone.getText().toString())) {
            mLayoutPhone.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.red));
            return false;
        }
        return true;
    }

    private void listener() {
        mEditProble.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLayoutProblem.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.color_D9D8E1));
                mTvInputCount.setText((500 - s.length()) + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLayoutPhone.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.color_D9D8E1));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        RxLogUtils.d("requestCode：" + requestCode);
        RxLogUtils.d("resultCode：" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                imageLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<Object> images = new ArrayList<>();

                images.addAll(0, imageLists);
                images.add(R.mipmap.icon_add_white);
                if (images.size() == 5) {
                    images.remove(4);
                }
                adapter.setNewData(images);
            }
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            if (data != null) {
                imageLists = data.getParcelableArrayListExtra(Key.BUNDLE_DATA);

                ArrayList<Object> images = new ArrayList<>();
                images.addAll(0, imageLists);
                images.add(R.mipmap.icon_add_white);
                if (images.size() == 5) {
                    images.remove(4);
                }
                adapter.setNewData(images);
            }
        }
    }


    /*提交反馈，文字部分*/
    private void commitData(String imgUrl) {
        FeedBackBean mBackBean = new FeedBackBean();
        mBackBean.setAppType((problemTimes.indexOf(mBtnTimes.getText().toString()) + 1) + "");
        mBackBean.setContactInfo(mEditPhone.getText().toString());
        mBackBean.setFeedbackType((problemType.indexOf(mBtnType.getText().toString()) + 1));
        mBackBean.setFeedbackDesc(mEditProble.getText().toString());
        mBackBean.setFeedbackImg(imgUrl);

        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().feedback(mBackBean),
                lifecycleSubject
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束" + s);
                        onBackPressed();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    /*上传图片*/
    public void feedbackImg() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < imageLists.size(); i++) {
            File file = new File(RxImageUtils.compressImage(imageLists.get(i).path));
            files.add(file);
            Log.d("上传图片", "feedbackImg: " + file.getAbsolutePath());
            Log.d("上传图片", " file.length();: " + file.length());
        }

        List<MultipartBody.Part> body = filesToMultipartBodyParts(files);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().feedbackImg(body),
                lifecycleSubject)
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("上传多张图片成功" + s);
                        Log.d("上传图片", "_onNext: " + s);
                        commitData(s);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }


    @OnClick({R.id.btn_type, R.id.btn_times, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_type:
                BottomMenu.show((AppCompatActivity) mContext, problemType, (text, index) -> {
                    mBtnType.setText(text);
                    mBtnType.setTextColor(ContextCompat.getColor(mContext, R.color.color_978E9D));
                    mBtnType.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.color_D9D8E1));
                }, true);
                break;
            case R.id.btn_times:
                BottomMenu.show((AppCompatActivity) mContext, problemTimes, (text, index) -> {
                    mBtnTimes.setText(text);
                    mBtnTimes.setTextColor(ContextCompat.getColor(mContext, R.color.color_978E9D));
                    mBtnTimes.getHelper().setBorderColorNormal(ContextCompat.getColor(mContext, R.color.color_D9D8E1));
                }, true);
                break;
            case R.id.btn_submit:
                if (checkData()) {
                    if (imageLists.size() > 0) {
                        feedbackImg();
                    } else {
                        commitData("");
                    }
                } else {
                    RxToast.normal("有信息未填写");
                }
                break;
        }
    }
}
