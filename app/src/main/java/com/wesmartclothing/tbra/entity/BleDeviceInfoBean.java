package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName BleDeviceInfoBean
 * @Date 2019/1/22 10:24
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BleDeviceInfoBean {

    //智能内衣的类别
    String category;

    //智能内衣制造厂商 (2byte)
    String manufacture;

    //智能内衣硬件的版本。(2byte)
    String HWVersion;

    //智能内衣的固件(应用部分)版本 （3byte）
    String FWAppVersion;

    //智能内衣的固件(Bootloader)版本。 (3byte)
    String FWBootLoaderVersion;

    //智能内衣BLE协议栈版本。(以ASCII码表示) (4byte)
    String BLEVersion;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getHWVersion() {
        return HWVersion;
    }

    public void setHWVersion(String HWVersion) {
        this.HWVersion = HWVersion;
    }

    public String getFWAppVersion() {
        return FWAppVersion;
    }

    public void setFWAppVersion(String FWAppVersion) {
        this.FWAppVersion = FWAppVersion;
    }

    public String getFWBootLoaderVersion() {
        return FWBootLoaderVersion;
    }

    public void setFWBootLoaderVersion(String FWBootLoaderVersion) {
        this.FWBootLoaderVersion = FWBootLoaderVersion;
    }

    public String getBLEVersion() {
        return BLEVersion;
    }

    public void setBLEVersion(String BLEVersion) {
        this.BLEVersion = BLEVersion;
    }


    @Override
    public String toString() {
        return "BleDeviceInfoBean{" +
                "category='" + category + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", HWVersion='" + HWVersion + '\'' +
                ", FWAppVersion='" + FWAppVersion + '\'' +
                ", FWBootLoaderVersion='" + FWBootLoaderVersion + '\'' +
                ", BLEVersion='" + BLEVersion + '\'' +
                '}';
    }
}
