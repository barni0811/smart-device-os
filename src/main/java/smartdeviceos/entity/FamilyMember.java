package smartdeviceos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "family_members")
public class FamilyMember {

    @EmbeddedId
    private FamilyMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("familyId")
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Role is required")
    @Column(name = "role", nullable = false)
    private String role;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;

    public FamilyMember() {}

    public FamilyMember(Family family, User user, String role) {
        this.family = family;
        this.user = user;
        this.role = role;
        this.id = new FamilyMemberId(family.getId(), user.getId());
    }

    public FamilyMemberId getId() {
        return id;
    }

    public void setId(FamilyMemberId id) {
        this.id = id;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "family=" + (family != null ? family.getName() : "null") +
                ", user=" + (user != null ? user.getName() : "null") +
                ", role='" + role + '\'' +
                '}';
    }
}
