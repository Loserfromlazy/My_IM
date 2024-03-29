package com.myim.common.entity;



import com.myim.common.pojo.ProtoMsgOuterClass;

import java.util.UUID;


public class User {
    private String uid = UUID.randomUUID().toString();
    private String deviceId=UUID.randomUUID().toString();
    private DeviceType deviceType = DeviceType.ANDROID;
    private String token =UUID.randomUUID().toString();
    private String name;

    private String sessionId;

    public enum DeviceType{
        ANDROID,IOS,WINDOWS,LINUX,OTHER;
    }

    public static User fromLoginRequets(ProtoMsgOuterClass.ProtoMsg.LoginRequest loginRequest){
        User user = new User();
        user.setUid(loginRequest.getUid());
        user.setDeviceId(loginRequest.getDeviceId());
        user.setToken(loginRequest.getToken());
        user.setDeviceType(loginRequest.getDeviceType());
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType=" + deviceType +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", sessionId='" + sessionId + '\'' +
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

    public void setDeviceType(int deviceType) {
        for (DeviceType value : DeviceType.values()) {
            if (value.ordinal() == deviceType){
                this.deviceType = value;
            }
        }
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
