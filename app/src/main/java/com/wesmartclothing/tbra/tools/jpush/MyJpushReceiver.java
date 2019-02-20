package com.wesmartclothing.tbra.tools.jpush;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.utils.RxLogUtils;
import com.wesmartclothing.tbra.entity.NotifyDataBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public static final int TYPE_OPEN_APP = 1;
    public static final int TYPE_OPEN_ACTIVITY = 2;
    public static final int TYPE_OPEN_URL = 3;


    public static final String ACTIVITY_SLIM = "slim";
    public static final String ACTIVITY_FIND = "find";
    public static final String ACTIVITY_SHOP = "shop";
    public static final String ACTIVITY_USER = "user";
    public static final String ACTIVITY_MESSAGE = "message";


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Intent pushintent = new Intent(context, PushService.class);//启动极光推送的服务
            context.startService(pushintent);

            Bundle bundle = intent.getExtras();
            RxLogUtils.d(TAG, "[MyJpushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                RxLogUtils.d(TAG, "[MyJpushReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                RxLogUtils.d(TAG, "[MyJpushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                RxLogUtils.d(TAG, "[MyJpushReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                RxLogUtils.d(TAG, "[MyJpushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

                String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
                NotifyDataBean notifyDataBean = JSON.parseObject(extra, NotifyDataBean.class);
                if (notifyDataBean == null) return;
                String openTarget = notifyDataBean.getOpenTarget();

                if ("planIndex".equals(openTarget)) {
//                    RxBus.getInstance().post(new RefreshSlimming());
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                RxLogUtils.d(TAG, "[MyJpushReceiver] 用户点击打开了通知");

                RxLogUtils.d("跳转首页:" + context.getPackageName());

                //打开自定义的Activity
//                RxActivityUtils.skipActivity(context, MainActivity.class, bundle);


                //9.0不同包名启动四大组件
                //https://blog.csdn.net/weixin_39178354/article/details/85099739
                Intent intent1 = new Intent("android.intent.action.MAINAC");
                intent1.setComponent(new ComponentName("com.wesmartclothing.tbra", "com.wesmartclothing.tbra.ui.main.MainActivity"));
                context.startActivity(intent1);

//                PackageManager manager = context.getPackageManager();
//                Intent intent1 = manager.getLaunchIntentForPackage("com.wesmartclothing.tbra");
//                intent1.putExtras(bundle);
//                if (intent1 != null) {
//                    context.startActivity(intent1);
//                } else {
//                    RxLogUtils.d("intent1 == null");
//                }


            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                RxLogUtils.d(TAG, "[MyJpushReceiver] 用户收到到自定义图标的回调: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                RxLogUtils.w(TAG, "[MyJpushReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                RxLogUtils.d(TAG, "[MyJpushReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    RxLogUtils.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    RxLogUtils.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //自定义信息
    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        RxLogUtils.e("接收到自定义广播：" + bundle.toString());

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Intent msgIntent = new Intent(MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(KEY_MESSAGE, message);
        if (!ExampleUtil.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.length() > 0) {
                    msgIntent.putExtra(KEY_EXTRAS, extras);
                }
            } catch (JSONException e) {

            }
        }

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }
}
