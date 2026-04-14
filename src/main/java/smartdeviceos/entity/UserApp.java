package smartdeviceos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_apps")
public class UserApp {

    @EmbeddedId
    private UserAppId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("appId")
    @JoinColumn(name = "app_id", nullable = false)
    private App app;

    @CreationTimestamp
    @Column(name = "installed_at", updatable = false)
    private LocalDateTime installedAt;

    @Column(name = "is_favorite")
    private Boolean isFavorite = false;

    // Constructors
    public UserApp() {}

    public UserApp(User user, App app) {
        this.user = user;
        this.app = app;
        this.id = new UserAppId(user.getId(), app.getId());
    }

    public UserApp(User user, App app, Boolean isFavorite) {
        this.user = user;
        this.app = app;
        this.isFavorite = isFavorite;
        this.id = new UserAppId(user.getId(), app.getId());
    }

    // Getters and Setters
    public UserAppId getId() {
        return id;
    }

    public void setId(UserAppId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "UserApp{" +
                "user=" + (user != null ? user.getName() : "null") +
                ", app=" + (app != null ? app.getName() : "null") +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
