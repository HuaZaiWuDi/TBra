package com.wesmartclothing.tbra.entity;


import com.vondear.rxtools.model.JavaBean;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName GidBean
 * @Date 2019/1/3 16:12
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class GidBean extends JavaBean {


    /**
     * gid : string
     */

    private String gid;


    public GidBean(String gid) {
        this.gid = gid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
