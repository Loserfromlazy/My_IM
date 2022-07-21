package com.myim.common.entity;



import java.util.UUID;


public class User {
    private String uid = UUID.randomUUID().toString();
    private String deviceId=UUID.randomUUID().toString();
    private DeviceType deviceType = DeviceType.ANDROID;
    private String token =UUID.randomUUID().toString();
    private String name = "zhangsan";

    public enum DeviceType{
        ANDROID,IOS,WINDOWS,LINUX,OTHER;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType=" + deviceType +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
