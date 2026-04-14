package smartdeviceos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank(message = "Menu name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false, unique = true)
    private Device device;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "submenu")
    private List<MenuItem> parentMenuItems;

    public Menu() {}

    public Menu(String id, String name, Device device) {
        this.id = id;
        this.name = name;
        this.device = device;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<MenuItem> getParentMenuItems() {
        return parentMenuItems;
    }

    public void setParentMenuItems(List<MenuItem> parentMenuItems) {
        this.parentMenuItems = parentMenuItems;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", device=" + (device != null ? device.getName() : "null") +
                ", isDefault=" + isDefault +
                '}';
    }
}
