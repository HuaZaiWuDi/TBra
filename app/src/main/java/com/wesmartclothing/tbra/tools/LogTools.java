package com.wesmartclothing.tbra.tools;

import android.os.Environment;

import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.wesmartclothing.tbra.ble.BleTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName LogTools
 * @Date 2019/3/22 12:04
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class LogTools {
    //保存Device的心率数据到文件夹
    public static final String LogDir = Environment.getExternalStorageDirectory() + "/TbraBleLog/";

    private LogTools() {
    }


    public static void saveDeviceLog(String logDir) {
        String date = RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date);
        String time = RxFormat.setFormatDate(System.currentTimeMillis(), RxFormat.Date_Time);

        String MACAddr = BleTools.getInstance().getBleDevice().getMac();
        String dir = LogDir;

        File destDir = new File(dir);

        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(dir, date + MACAddr + ".txt");

        String dateLogContent = time + "：" + logDir;
        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, true);
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();

            filerWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufWriter != null)
                    bufWriter.close();
                if (filerWriter != null)
                    filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
