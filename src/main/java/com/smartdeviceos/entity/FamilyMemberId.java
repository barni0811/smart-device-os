package com.smartdeviceos.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class FamilyMemberId {

    private String familyId;
    private String userId;

    // Constructors
    public FamilyMemberId() {}

    public FamilyMemberId(String familyId, String userId) {
        this.familyId = familyId;
        this.userId = userId;
    }

    // Getters and Setters
    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMemberId that = (FamilyMemberId) o;
        return Objects.equals(familyId, that.familyId) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(familyId, userId);
    }

    @Override
    public String toString() {
        return "FamilyMemberId{" +
                "familyId='" + familyId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
