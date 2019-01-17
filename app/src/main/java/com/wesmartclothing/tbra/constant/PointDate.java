package com.wesmartclothing.tbra.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Package com.wesmartclothing.tbra.constant
 * @FileName PointDate
 * @Date 2019/1/15 11:41
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
//自定义注解
@Retention(RetentionPolicy.SOURCE)
@StringDef({PointDate.week, PointDate.oneMonth, PointDate.quarter, PointDate.halfYear, PointDate.year})
public @interface PointDate {

    String week = "1_week";

    String oneMonth = "1_month";

    String quarter = "3_month";

    String halfYear = "6_month";

    String year = "1_year";
}