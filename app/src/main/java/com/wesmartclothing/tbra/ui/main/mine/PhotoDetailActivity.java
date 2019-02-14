package com.wesmartclothing.tbra.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.imagepicker.bean.ImageItem;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.cardview.CardView;
import com.vondear.rxtools.view.recyclerview.banner.BannerLayoutManager;
import com.vondear.rxtools.view.scaleimage.ImageSource;
import com.vondear.rxtools.view.scaleimage.RxScaleImageView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;

import java.util.ArrayList;

import butterknife.BindView;

public class PhotoDetailActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.banner)
    RecyclerView banner;
    @BindView(R.id.layoutTitle)
    CardView mLayoutTitle;

    private BaseQuickAdapter adapter;
    private ArrayList<ImageItem> list;
    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initRxTitle();
        initRecycler();
    }

    private void initRecycler() {
        Bundle bundle = getIntent().getExtras();
        list = bundle.getParcelableArrayList(Key.BUNDLE_DATA);

        mRxTitle.setTitle(1 + "/" + list.size());
        BannerLayoutManager bannerLayoutManager = new BannerLayoutManager(mContext, banner, false, list.size());
        banner.setLayoutManager(bannerLayoutManager);
        adapter = new BaseQuickAdapter<ImageItem, BaseViewHolder>(R.layout.item_photo, list) {
            @Override
            protected void convert(BaseViewHolder helper, ImageItem item) {
                ((RxScaleImageView) helper.getView(R.id.iv_scale)).setImage(ImageSource.uri(item.path));
            }
        };
        banner.setAdapter(adapter);
        bannerLayoutManager.setOnSelectedViewListener((view, position) -> {
            mRxTitle.setTitle((position + 1) + "/" + list.size());
            current = position;
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (mLayoutTitle.getHeight() != 0) {
                RxAnimationUtils.animateHeight(RxUtils.dp2px(84), RxUtils.dp2px(0), mLayoutTitle);
            } else {
                RxAnimationUtils.animateHeight(RxUtils.dp2px(0), RxUtils.dp2px(84), mLayoutTitle);
            }
        });
    }

    /**
     * 标题
     */
    private void initRxTitle() {
        mRxTitle.setLeftOnClickListener(view -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent();
            bundle.putParcelableArrayList(Key.BUNDLE_DATA, list);
            intent.putExtras(bundle);
            setResult(UserInfoActivity.RESULT_CODE, intent);
            finish();
        });
        mRxTitle.setRightIconOnClickListener(view -> {
            adapter.remove(current);
            mRxTitle.setTitle((current == 0 ? current + 1 : current) + "/" + list.size());
            if (adapter.getData().size() == 0) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                bundle.putParcelableArrayList(Key.BUNDLE_DATA, list);
                intent.putExtras(bundle);
                setResult(UserInfoActivity.RESULT_CODE, intent);
                finish();
            }
        });
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
