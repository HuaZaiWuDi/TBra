package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName SingleSelectBean
 * @Date 2019/1/18 18:42
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class SingleSelectBean {

    private String text;
    private boolean select;


    public SingleSelectBean(String text) {
        this.text = text;
        this.select = false;
    }

    public SingleSelectBean(String text, boolean select) {
        this.text = text;
        this.select = select;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
