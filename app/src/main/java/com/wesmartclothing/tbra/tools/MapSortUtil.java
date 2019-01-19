package com.wesmartclothing.tbra.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName MapSortUtil
 * @Date 2019/1/15 16:57
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class MapSortUtil {


    ///////////////////////////////////////////////////////////////////////////
    // Map按值排序
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static <T, V> Map<T, V> sortMapByKey(Map<T, V> map, boolean isDown) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<T, V> sortMap = new TreeMap<>(
                (s, t1) -> {
                    if (!isDown) {
                        return s.hashCode() - t1.hashCode();
                    } else {
                        return t1.hashCode() - s.hashCode();
                    }
                });
        sortMap.putAll(map);

        return sortMap;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static <T, V> Map<T, V> sortMapByKey(Map<T, V> map) {
        return sortMapByKey(map, false);
    }


    public static Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        return sortMapByValue(oriMap, false);
    }

    /**
     * 使用 Map按value进行排序
     * <p>
     * * int compare(Object o1, Object o2) 返回一个基本类型的整型，
     * * 返回负数表示：o1 小于o2，
     * * 返回0 表示：o1和o2相等，
     * * 返回正数表示：o1大于o2。
     *
     * @param oriMap
     * @param isDown 是否是降序
     * @return
     */
    public static <T, V> Map<T, V> sortMapByValue(Map<T, V> oriMap, boolean isDown) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<T, V> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<T, V>> entryList = new ArrayList<>(oriMap.entrySet());
        Collections.sort(entryList, (t1, t2) -> {
            if (isDown) {
                return t2.hashCode() - t1.hashCode();
            } else {
                return t1.hashCode() - t2.hashCode();
            }
        });

        Iterator<Map.Entry<T, V>> iter = entryList.iterator();
        Map.Entry<T, V> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

}
