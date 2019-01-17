package com.wesmartclothing.tbra.tools;

import com.vondear.rxtools.utils.RxDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName CheckTempErrorUtil
 * @Date 2019/1/16 16:04
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class CheckTempErrorUtil {

    /**
     * 计算方差后的平均温度（标准温度）
     * 标准温度=temp和 / temp个数 temp为在r加减平均方差温度范围内的温度
     * 平均方差温度=Math.sqrt(1/N * (Math.pow((X1 - r),2) + ...+ Math.pow((Xn - r),2)))
     * r = totalTemp / N    totalTemp为有效温度和，N有效温度个数，r为有效温度和的平均温度
     *
     * @param tempList
     * @return
     */
    public static double calculationNormTemp(List<Double> tempList) {

        double sum = 0.0;
        if (RxDataUtils.isEmpty(tempList)) {
            return sum;
        }
        double avgTemp = tempList.stream().mapToDouble(Double::doubleValue).average().getAsDouble();

        for (Double temp : tempList) {
            sum += Math.pow((temp - avgTemp), 2);
        }
        double variance = Math.sqrt(sum / tempList.size());
        List<Double> resultList = new ArrayList();
        for (Double temp : tempList) {
            if (avgTemp - variance < temp && temp < avgTemp + variance) {
                resultList.add(temp);
            }
        }
        if (resultList.size() == 0) {
            return avgTemp;
        }
        return resultList.stream().reduce(0.0, (x, y) -> x + y) / resultList.size();
    }


    /**
     * 判断是否是有效温度。温度在32到42之间为有效温度
     *
     * @param temp
     * @return
     */
    public static boolean isValidTemperature(double temp) {
        return 32 <= temp && temp <= 42;
    }

}
