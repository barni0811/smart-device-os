package com.smartdeviceos.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class UserAppId {

    private String userId;
    private String appId;

    // Constructors
    public UserAppId() {}

    public UserAppId(String userId, String appId) {
        this.userId = userId;
        this.appId = appId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        UserAppId that = (UserAppId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, appId);
    }

    @Override
    public String toString() {
        return "UserAppId{" +
                "userId='" + userId + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
