package com.wesmartclothing.tbra.ui.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxOpenFileBySystem;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.tools.LogTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LogActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerLog)
    RecyclerView mRecyclerLog;

    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_log;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initRecyclerView();
        initData();
    }

    private void initData() {
        List<String> logLists = new ArrayList<>();
        List<File> files = RxFileUtils.listFilesInDir(LogTools.LogDir);//获取目录下所有的文件和子目录
        if (files != null)
            for (File file : files) {
                if (!file.isDirectory()) {
                    RxLogUtils.d("文件夹：" + file.getPath());
                    logLists.add(file.getAbsolutePath());
                }
            }
        adapter.setNewData(logLists);
    }

    private void initRecyclerView() {
        mRecyclerLog.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_log_item) {
            @Override
            protected void convert(BaseViewHolder helper, final String path) {
                helper.setText(R.id.tv_logDate, path)
                        .setOnClickListener(R.id.tv_details, view -> {
                            //跳转详情
                            RxLogUtils.d("文件路径：" + path);
                            RxOpenFileBySystem.openFile(mContext, new File(path));
                        });
            }
        };

        adapter.bindToRecyclerView(mRecyclerLog);
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }
}
