package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName SinglePointBean
 * @Date 2019/1/15 15:52
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class SinglePointBean {


    private long time;
    private String pointName;
    private double temp;

    public SinglePointBean(long time, String pointName, double temp) {
        this.time = time;
        this.pointName = pointName;
        this.temp = temp;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
