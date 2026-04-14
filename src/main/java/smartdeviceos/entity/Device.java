package smartdeviceos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank(message = "Device name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallpaper_id")
    private Wallpaper wallpaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name = "is_default_menu")
    private Boolean isDefaultMenu = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceApp> deviceApps;

    public Device() {}

    public Device(String id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wallpaper getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(Wallpaper wallpaper) {
        this.wallpaper = wallpaper;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Boolean getIsDefaultMenu() {
        return isDefaultMenu;
    }

    public void setIsDefaultMenu(Boolean isDefaultMenu) {
        this.isDefaultMenu = isDefaultMenu;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<DeviceApp> getDeviceApps() {
        return deviceApps;
    }

    public void setDeviceApps(List<DeviceApp> deviceApps) {
        this.deviceApps = deviceApps;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user=" + (user != null ? user.getName() : "null") +
                ", isDefaultMenu=" + isDefaultMenu +
                '}';
    }
}
