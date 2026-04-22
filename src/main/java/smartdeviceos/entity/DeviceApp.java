package smartdeviceos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_apps")
public class DeviceApp {

    @EmbeddedId
    private DeviceAppId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deviceId")
    @JoinColumn(name = "device_id", nullable = false)
    @JsonIgnore
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("appId")
    @JoinColumn(name = "app_id", nullable = false)
    @JsonIgnore
    private App app;

    @CreationTimestamp
    @Column(name = "installed_at", updatable = false)
    private LocalDateTime installedAt;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    public DeviceApp() {}

    public DeviceApp(Device device, App app) {
        this.device = device;
        this.app = app;
        this.id = new DeviceAppId(device.getId(), app.getId());
    }

    public DeviceAppId getId() {
        return id;
    }

    public void setId(DeviceAppId id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
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

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public void incrementUsageCount() {
        this.usageCount++;
        this.lastUsed = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "DeviceApp{" +
                "device=" + (device != null ? device.getName() : "null") +
                ", app=" + (app != null ? app.getName() : "null") +
                ", usageCount=" + usageCount +
                '}';
    }
}
