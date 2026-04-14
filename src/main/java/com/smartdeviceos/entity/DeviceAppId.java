package com.smartdeviceos.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class DeviceAppId {

    private String deviceId;
    private String appId;

    // Constructors
    public DeviceAppId() {}

    public DeviceAppId(String deviceId, String appId) {
        this.deviceId = deviceId;
        this.appId = appId;
    }

    // Getters and Setters
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceAppId that = (DeviceAppId) o;
        return Objects.equals(deviceId, that.deviceId) &&
               Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, appId);
    }

    @Override
    public String toString() {
        return "DeviceAppId{" +
                "deviceId='" + deviceId + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
