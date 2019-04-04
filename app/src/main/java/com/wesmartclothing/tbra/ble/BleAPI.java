package com.wesmartclothing.tbra.ble;

import com.clj.fastble.utils.HexUtil;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.aboutByte.ByteUtil;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.entity.AddTempDataBean;
import com.wesmartclothing.tbra.entity.BleDeviceInfoBean;
import com.wesmartclothing.tbra.entity.JsonDataBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.ble
 * @FileName BleAPI
 * @Date 2019/1/4 18:24
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BleAPI {

    /**
     * 小端模式
     */
    static final String TGA = "【BleAPI】";


    private BleAPI() {
    }


    /**
     * 命令格式
     *
     * byte[0],头
     * byte[1-2],长度
     * byte[3],命令ID
     *
     */


    /**
     * 绑定用户ID
     *
     * @param userId
     * @param subscriber
     */
    public static void bindUserId(String userId, RxSubscriber<byte[]> subscriber) {
        byte[] bytes = new byte[20];

        byte[] bytes1 = userId.getBytes();

        bytes[1] = (byte) bytes1.length;
        bytes[3] = 0x01;


        System.arraycopy(bytes1, 0, bytes, 4, bytes1.length);

        RxLogUtils.d("【绑定设备ID】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, subscriber);
    }


    /**
     * 解绑用户ID
     *
     * @param subscriber
     */
    public static void removeUserId(RxSubscriber<byte[]> subscriber) {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x02;


        RxLogUtils.d("【解绑设备ID】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, subscriber);
    }


    /**
     * 获取设备信息
     * //01 1000 03 00 0001 0001 010101 000000 34303030
     *
     * @param subscriber
     */
    public static void getSettingInfo(RxSubscriber<BleDeviceInfoBean> subscriber) {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x03;

        RxLogUtils.d("【获取设备信息】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, new RxSubscriber<byte[]>() {
            @Override
            protected void _onNext(byte[] data) {
                BleDeviceInfoBean bleDeviceInfo = new BleDeviceInfoBean();
                bleDeviceInfo.setCategory(data[4] + "");
                bleDeviceInfo.setManufacture(String.valueOf(data[5]) + String.valueOf(data[6]));
                bleDeviceInfo.setHWVersion(String.valueOf(data[7]) + String.valueOf(data[8]));
                bleDeviceInfo.setFWAppVersion(String.valueOf(data[9]) + "." + String.valueOf(data[10]) + "." + String.valueOf(data[11]));
                bleDeviceInfo.setFWBootLoaderVersion(String.valueOf(data[12]) + "." + String.valueOf(data[13]) + "." + String.valueOf(data[14]));
                byte[] bytes1 = new byte[4];
                System.arraycopy(data, 15, bytes1, 0, 4);
                try {
                    bleDeviceInfo.setBLEVersion(new String(bytes1, "ascii"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                RxLogUtils.d("设备信息：" + bleDeviceInfo.toString());
                subscriber.onNext(bleDeviceInfo);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                subscriber.onError(e);
            }
        });
    }


    /**
     * 获取电池信息
     */
    public static void getBattery() {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x04;

        RxLogUtils.d("【获取电池信息】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, null);
    }

    /**
     * 获取温度信息
     * 412a0005 0000 0000 0000000000
     * 796a 796a 796a 796a 796a 796a 796a 796a
     * 796a 796a 796a 796a 796a 796a 796a 796a
     * <p>
     * temperature=-45.0+175*tmp_data/65536
     * <p>
     * 012a0009 0000 e307 0116101009
     * 796a796a796a796a796a796a796a796a796a796a796a796a796a796a796a796a
     * <p>
     * 包标识：
     * 0x01     //无
     * 0x41     //开始包
     * 0x81     //中间包
     * 0xc1     //结束包
     *
     * @param subscriber
     */
    public static void getTempData(int start, int end, RxSubscriber<AddTempDataBean> subscriber) {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x05;
        bytes[4] = ByteUtil.intToBytesD2(start)[0];
        bytes[5] = ByteUtil.intToBytesD2(start)[1];
        bytes[6] = ByteUtil.intToBytesD2(end)[0];
        bytes[7] = ByteUtil.intToBytesD2(end)[1];

        RxLogUtils.d("【获取温度信息】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, null);

        BleTools.getInstance().setBleCallBack(data -> {
            AddTempDataBean dataBean = new AddTempDataBean();
            dataBean.setPickageSign(data[0]);
            String dateStr = ByteUtil.bytesToIntD2(new byte[]{data[6], data[7]}) + "-" +
                    dateFormat(data[8]) + "-" + dateFormat(data[9]) + " " + dateFormat(data[10]) + ":" +
                    dateFormat(data[11]) + ":" + dateFormat(data[12]);
            dataBean.setCollectTime(dateStr);
            dataBean.setIndex(ByteUtil.bytesToIntD2(new byte[]{data[4], data[5]}));
            List<JsonDataBean> dataList = new ArrayList<>();
            dataBean.setDataList(dataList);
            for (int i = 13; i < data.length; i = i + 2) {
                JsonDataBean bean = new JsonDataBean();
                if (i < (13 + 16)) {
                    bean.setNodeName("L0" + ((i - 13) / 2 + 1));
                } else {
                    bean.setNodeName("R0" + ((i - 13 - 16) / 2 + 1));
                }
                bean.setNodeTemp(bytes2Temp(data[i], data[i + 1]));
                dataList.add(bean);
            }
            RxLogUtils.d("内衣数据：" + dataBean.toString());
            subscriber.onNext(dataBean);
        });

    }


    /**
     * 获取温度信息序号头
     *
     * @param subscriber
     */
    public static void getTempCount(RxSubscriber<Integer> subscriber) {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x06;

        RxLogUtils.d("【获取温度信息序号头】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, new RxSubscriber<byte[]>() {
            @Override
            protected void _onNext(byte[] bytes) {
                subscriber.onNext(ByteUtil.bytesToIntD2(new byte[]{bytes[4], bytes[5]}));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                subscriber.onError(e);
            }

        });
    }


    /**
     * 清除温度信息
     */
    public static void clearTempData() {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x07;

        RxLogUtils.d("【清除温度信息】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, new RxSubscriber<byte[]>() {
            @Override
            protected void _onNext(byte[] bytes) {

            }
        });
    }

    /**
     * 同步时间
     *
     * @param subscriber
     */
    public static void syncTime(RxSubscriber<byte[]> subscriber) {
        byte[] bytes = new byte[20];

        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;
        int day = instance.get(Calendar.DAY_OF_MONTH);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int min = instance.get(Calendar.MINUTE);
        int sec = instance.get(Calendar.SECOND);

        bytes[1] = 0x08;
        bytes[3] = 0x08;
        bytes[4] = ByteUtil.intToBytesD2(year)[0];
        bytes[5] = ByteUtil.intToBytesD2(year)[1];
        bytes[6] = (byte) month;
        bytes[7] = (byte) day;
        bytes[8] = (byte) hour;
        bytes[9] = (byte) min;
        bytes[10] = (byte) sec;

        RxLogUtils.d("【同步时间】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, subscriber);
    }


    /**
     * 获取智能内衣设置信息
     *
     * @param subscriber
     */
    public static void getTimingBraInfo(RxSubscriber<AddTempDataBean> subscriber) {
        byte[] bytes = new byte[20];

        bytes[1] = 0x01;
        bytes[3] = 0x09;

        RxLogUtils.d("【获取智能内衣设置信息】", HexUtil.encodeHexStr(bytes));
        BleTools.getInstance().write(bytes, new RxSubscriber<byte[]>() {
            @Override
            protected void _onNext(byte[] bytes) {
                AddTempDataBean dataBean = new AddTempDataBean();
                String dateStr = ByteUtil.bytesToIntD2(new byte[]{bytes[6], bytes[7]}) + "-" +
                        dateFormat(bytes[8]) + "-" + dateFormat(bytes[9]) + " " + dateFormat(bytes[10]) + ":" +
                        dateFormat(bytes[11]) + ":" + dateFormat(bytes[12]);
                dataBean.setCollectTime(dateStr);
                dataBean.setIndex(ByteUtil.bytesToIntD2(new byte[]{bytes[4], bytes[5]}));
                List<JsonDataBean> dataList = new ArrayList<>();
                dataBean.setDataList(dataList);
                for (int i = 13; i < bytes.length; i = i + 2) {
                    JsonDataBean bean = new JsonDataBean();
                    if (i < (13 + 16)) {
                        bean.setNodeName("R0" + ((i - 13) / 2 + 1));
                    } else {
                        bean.setNodeName("L0" + ((i - 13 - 16) / 2 + 1));
                    }
                    bean.setNodeTemp(bytes2Temp(bytes[i], bytes[i + 1]));

                    RxLogUtils.d("内衣数据：" + bean.toString());
                    dataList.add(bean);
                }
                subscriber.onNext(dataBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                subscriber.onError(e);
            }
        });
    }


    private static String dateFormat(byte b) {
        int date = (int) b;
        return date < 10 ? "0" + date : date + "";
    }


    private static double bytes2Temp(byte... bytes) {
        int i = ByteUtil.bytesToIntG2(bytes);
        double v = -45.0 + 175f * i / 65536;
        return RxFormatValue.format4S5R(v, 1);
    }

}
