package com.wesmartclothing.tbra.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.view.roundprogressbar.RxRoundProgressBar;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.entity.JsonDataBean;
import com.wesmartclothing.tbra.entity.PointDataBean;
import com.wesmartclothing.tbra.tools.MapSortUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Package com.wesmartclothing.tbra.view
 * @FileName HistoryTempView
 * @Date 2019/1/8 16:48
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class HistoryTempView extends LinearLayout {

    public static final int MODE_DYNAMIC = 0;
    public static final int MODE_STATIC = 1;


    @BindView(R.id.tv_L1)
    TextView mTvL1;
    @BindView(R.id.tv_L8)
    TextView mTvL8;
    @BindView(R.id.tv_L2)
    TextView mTvL2;
    @BindView(R.id.tv_L3)
    TextView mTvL3;
    @BindView(R.id.tv_L4)
    TextView mTvL4;
    @BindView(R.id.tv_L7)
    TextView mTvL7;
    @BindView(R.id.tv_L6)
    TextView mTvL6;
    @BindView(R.id.tv_L5)
    TextView mTvL5;
    @BindView(R.id.tv_R5)
    TextView mTvR5;
    @BindView(R.id.tv_R6)
    TextView mTvR6;
    @BindView(R.id.tv_R4)
    TextView mTvR4;
    @BindView(R.id.tv_R7)
    TextView mTvR7;
    @BindView(R.id.tv_R3)
    TextView mTvR3;
    @BindView(R.id.tv_R2)
    TextView mTvR2;
    @BindView(R.id.tv_R1)
    TextView mTvR1;
    @BindView(R.id.tv_R8)
    TextView mTvR8;
    //    @BindView(R.id.seekbar)
//    AppCompatSeekBar mSeekbar;
    @BindView(R.id.img_left)
    ImageView mImgLeft;
    @BindView(R.id.img_play_pause)
    ImageView mImgPlayPause;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.tv_playTime)
    TextView mTvPlayTime;
    @BindView(R.id.tv_playSpeed)
    TextView mTvPlaySpeed;
    @BindView(R.id.progress)
    RxRoundProgressBar mProgress;

    private int showMode = MODE_STATIC;
    private long speed = 1000;
    private boolean isPlay = false, isShow = false;
    private int currentTime = 0;
    private Gson gson = new Gson();
    private List<PointDataBean> mPointDataBeans;
    private Map<String, Integer> errorPointMap = new HashMap<>();
    private OnErrorPointListener mOnErrorPointListener;

    public interface OnErrorPointListener {

        void errorPoint(Map<String, Integer> errorPoints);
    }

    public void setOnErrorPointListener(OnErrorPointListener onErrorPointListener) {
        mOnErrorPointListener = onErrorPointListener;
    }

    public HistoryTempView(Context context) {
        this(context, null);
    }

    public HistoryTempView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryTempView(Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.layout_history_temp_data, this);
        ButterKnife.bind(this, view);
        initView();
    }


    private void initView() {
        //视频模式，动态播放
        setShow(false);
    }

    /**
     * 静态播放
     */
    public void setTimingData(List<JsonDataBean> jsonDataBeans) {
        showMode = MODE_STATIC;
        setShow(false);
        showPoint(jsonDataBeans);
    }


    /**
     * 动态播放
     *
     * @param list
     */
    public void setData(List<PointDataBean> list) {
        mPointDataBeans = list;
        showMode = MODE_DYNAMIC;
        //视频模式，动态播放
        setShow(true);
        currentTime = 0;
        mProgress.setMax(list.size());
        mProgress.setProgress(0);
        RxTextUtils.getBuilder(RxFormat.setSec2MS(currentTime))
                .append("/" + RxFormat.setSec2MS(list.size()))
                .setForegroundColor(Color.parseColor("#99FFFFFF"))
                .into(mTvPlayTime);
        mTvPlaySpeed.setText("X" + RxFormatValue.fromat4S5R(speed / 1000, 1) + "倍");

//        setPlay(true);
//
//        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                currentTime = Math.max(0, Math.min(i, list.size() - 1));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                setPlay(false);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                setPlay(true);
//            }
//        });

        scaleAnimation.setDuration(500L);
        scaleAnimation.setInterpolator(new CycleInterpolator(0.5F));
    }

    MyTimer mMyTimer = new MyTimer(speed, new MyTimerListener() {
        @Override
        public void enterTimer() {
            List<JsonDataBean> pointlist = gson.fromJson(mPointDataBeans.get(Math.min(currentTime, mPointDataBeans.size() - 1)).getJsonData(), new TypeToken<List<JsonDataBean>>() {
            }.getType());
            showPoint(pointlist);
            currentTime++;

//            mSeekbar.setProgress(currentTime, true);
            mProgress.setProgress(currentTime, false);
            RxTextUtils.getBuilder(RxFormat.setSec2MS(currentTime))
                    .append("/" + RxFormat.setSec2MS(mPointDataBeans.size()))
                    .setForegroundColor(Color.parseColor("#99FFFFFF"))
                    .into(mTvPlayTime);

            if (currentTime == mPointDataBeans.size()) {
                currentTime = 0;
                mMyTimer.stopTimer();
                isPlay = false;
                mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
        }
    });


    private void showPoint(List<JsonDataBean> pointlist) {
        for (JsonDataBean bean : pointlist) {
            if (bean.getWarningFlag() == 1) {
                errorPointMap.merge(bean.getNodeName(), 1, Integer::sum);
            }
            switch (bean.getNodeName()) {
                case "L01":
                    textTopDrawable(mTvL1, bean.getWarningFlag());
                    break;
                case "L02":
                    textBottomDrawable(mTvL2, bean.getWarningFlag());
                    break;
                case "L03":
                    textBottomDrawable(mTvL3, bean.getWarningFlag());
                    break;
                case "L04":
                    textBottomDrawable(mTvL4, bean.getWarningFlag());
                    break;
                case "L05":
                    textTopDrawable(mTvL5, bean.getWarningFlag());
                    break;
                case "L06":
                    textTopDrawable(mTvL6, bean.getWarningFlag());
                    break;
                case "L07":
                    textTopDrawable(mTvL7, bean.getWarningFlag());
                    break;
                case "L08":
                    textTopDrawable(mTvL8, bean.getWarningFlag());
                    break;
                case "R01":
                    textTopDrawable(mTvR1, bean.getWarningFlag());
                    break;
                case "R02":
                    textBottomDrawable(mTvR2, bean.getWarningFlag());
                    break;
                case "R03":
                    textBottomDrawable(mTvR3, bean.getWarningFlag());
                    break;
                case "R04":
                    textBottomDrawable(mTvR4, bean.getWarningFlag());
                    break;
                case "R05":
                    textTopDrawable(mTvR5, bean.getWarningFlag());
                    break;
                case "R06":
                    textTopDrawable(mTvR6, bean.getWarningFlag());
                    break;
                case "R07":
                    textTopDrawable(mTvR7, bean.getWarningFlag());
                    break;
                case "R08":
                    textTopDrawable(mTvR8, bean.getWarningFlag());
                    break;
            }
        }
        if (mOnErrorPointListener != null) {
            //排序
            errorPointMap = MapSortUtil.sortMapByValue(errorPointMap, true);
            mOnErrorPointListener.errorPoint(errorPointMap);
        }

    }

    private void textTopDrawable(TextView textView, int flag) {
        Drawable drawable = ContextCompat.getDrawable(mTvL1.getContext(),
                flag == 0 ? R.mipmap.ic_circle_green : flag == 1 ? R.mipmap.ic_cricle_red : R.mipmap.ic_cricle_yellow);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        if (flag == 1)
            doAnim(textView);
    }

    private void textBottomDrawable(TextView textView, int flag) {
        Drawable drawable = ContextCompat.getDrawable(mTvL1.getContext(),
                flag == 0 ? R.mipmap.ic_circle_green : flag == 1 ? R.mipmap.ic_cricle_red : R.mipmap.ic_cricle_yellow);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

        if (flag == 1) {
            doAnim(textView);
        }
    }

    ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.3f, 1f, 1.3f);

    private void doAnim(TextView view) {
//        if (view.getAnimation() == null) {
//            view.setAnimation(scaleAnimation);
//        }
//        view.getAnimation().start();

    }


    public void setShow(boolean show) {
        isShow = show;
//        mSeekbar.setVisibility(isShow ? VISIBLE : GONE);
        mProgress.setVisibility(isShow ? VISIBLE : GONE);
        mImgLeft.setVisibility(isShow ? VISIBLE : GONE);
        mImgPlayPause.setVisibility(isShow ? VISIBLE : GONE);
        mImgRight.setVisibility(isShow ? VISIBLE : GONE);
        mTvPlayTime.setVisibility(isShow ? VISIBLE : GONE);
        mTvPlaySpeed.setVisibility(isShow ? VISIBLE : GONE);
        if (isPlay)
            if (isShow) {
                showTimer.startTimer();
            } else {
                showTimer.stopTimer();
            }
    }

    MyTimer showTimer = new MyTimer(() -> setShow(false), 5000);

    private void setPlay(boolean play) {
        isPlay = play;
        if (isPlay) {
            mMyTimer.setPeriodAndDelay(speed, 0);
            mMyTimer.startTimer();
            mImgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
            showTimer.startTimer();
        } else {
            mMyTimer.stopTimer();
            mImgPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    public void pause() {
        setPlay(false);

    }

    public List<PointDataBean> getPointDataBeans() {
        return mPointDataBeans;
    }

    @OnClick({R.id.img_left, R.id.img_play_pause, R.id.img_right, R.id.parent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                speed += 200;
                mImgRight.setAlpha(1f);
                mImgRight.setEnabled(true);
                mMyTimer.stopTimer();
                mMyTimer.setPeriodAndDelay(speed, 0);
                mMyTimer.startTimer();
                mTvPlaySpeed.setText("X" + RxFormatValue.fromat4S5R(1000f / speed, 1) + "倍");
                if (speed > 2000) {
                    mImgLeft.setAlpha(0.6f);
                    mImgLeft.setEnabled(false);
                }
                break;
            case R.id.img_play_pause:
                setPlay(!isPlay);
                break;
            case R.id.img_right:
                speed -= 200;
                mImgLeft.setAlpha(1f);
                mImgLeft.setEnabled(true);
                mMyTimer.stopTimer();
                mMyTimer.setPeriodAndDelay(speed, 0);
                mMyTimer.startTimer();
                mTvPlaySpeed.setText("X" + RxFormatValue.fromat4S5R(1000f / speed, 1) + "倍");
                if (speed < 500) {
                    mImgRight.setAlpha(0.6f);
                    mImgRight.setEnabled(false);
                }
                break;
            case R.id.parent:
                if (showMode == MODE_DYNAMIC)
                    setShow(!isShow);
                break;
        }
    }
}
