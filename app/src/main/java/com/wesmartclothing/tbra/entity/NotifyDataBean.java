package com.wesmartclothing.tbra.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName NotifyDataBean
 * @Date 2018/12/25 17:08
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class NotifyDataBean {

    private String openTarget;
    private int operation;
    private String gid;

    public String getOpenTarget() {
        return openTarget;
    }

    public void setOpenTarget(String openTarget) {
        this.openTarget = openTarget;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getGId() {
        return gid;
    }

    public void setGId(String msgId) {
        this.gid = msgId;
    }
}
