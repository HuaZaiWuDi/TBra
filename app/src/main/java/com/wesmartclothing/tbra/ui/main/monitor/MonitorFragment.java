package com.wesmartclothing.tbra.ui.main.monitor;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.tmall.ultraviewpager.UltraViewPager;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.adapter.UltraPagerAdapter;
import com.wesmartclothing.tbra.base.BaseAcFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Package com.wesmartclothing.tbra.ui.main.testing
 * @FileName MonitorFragment
 * @Date 2019/1/8 11:02
 * @Author JACK
 * @Describe TODO监测界面
 * @Project tbra
 */
public class MonitorFragment extends BaseAcFragment {

    @BindView(R.id.ultraViewPager)
    UltraViewPager ultraViewPager;

    private List<String> imgs = new ArrayList<>();


    public static MonitorFragment getInstance() {
        return new MonitorFragment();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_monitor;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initViewPage();
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }


    private void initViewPage() {
        imgs.clear();
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=87077840f66f6c7607074ad9d7681e80&imgtype=0&src=http%3A%2F%2Fpic.kekenet.com%2F2017%2F0323%2F24621490236152.png");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=d7b02b4c46569e0197f5949164433c52&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F574ddb5egw1eqosahw1m6j20pa0g00w3.jpg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=53404ec533a940868fef1c0793cc8a5d&imgtype=0&src=http%3A%2F%2Fwerkstette.dk%2Fwp-content%2Fuploads%2F2015%2F09%2FEntertainment_Weekly_Photographer_Marc_Hom_British_Actor_Charlie_Hunnam_as_King_Arthur_Retouch_Werkstette10-770x841.jpg");

        //UltraPagerAdapter 绑定子view到UltraViewPager
        PagerAdapter adapter = new UltraPagerAdapter(imgs, ultraViewPager);
        ultraViewPager.setAdapter(adapter);

    }


}
