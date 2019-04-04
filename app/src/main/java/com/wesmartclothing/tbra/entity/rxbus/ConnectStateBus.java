package com.wesmartclothing.tbra.entity.rxbus;

import com.vondear.rxtools.model.JavaBean;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName ConnectStateBus
 * @Date 2019/1/23 11:00
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class ConnectStateBus extends JavaBean {


    private boolean connect;


    public ConnectStateBus(boolean connect) {
        this.connect = connect;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }
}
