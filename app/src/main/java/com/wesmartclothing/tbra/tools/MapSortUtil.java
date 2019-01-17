package com.wesmartclothing.tbra.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                String::compareTo);

        sortMap.putAll(map);

        return sortMap;
    }


    /**
     * 使用 Map按value进行排序
     *
     * @param oriMap
     * @return
     */
    public static Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(
                oriMap.entrySet());
        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

}
