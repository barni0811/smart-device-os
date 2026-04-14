package com.smartdeviceos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "families")
public class Family {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank(message = "Family name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // One-to-many relationships
    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FamilyMember> members;

    // Constructors
    public Family() {}

    public Family(String id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<FamilyMember> getMembers() {
        return members;
    }

    public void setMembers(List<FamilyMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Family{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner=" + (owner != null ? owner.getName() : "null") +
                '}';
    }
}
