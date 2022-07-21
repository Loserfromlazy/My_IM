package com.myim.client.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private String uid = UUID.randomUUID().toString();
    private String deviceId=UUID.randomUUID().toString();
    private DeviceType deviceType = DeviceType.ANDROID;
    private String token =UUID.randomUUID().toString();
    private String name = "zhangsan";

    public enum DeviceType{
        ANDROID,IOS,WINDOWS,LINUX,OTHER;
    }
}
