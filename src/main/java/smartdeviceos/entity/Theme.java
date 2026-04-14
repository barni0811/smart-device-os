package smartdeviceos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "themes")
public class Theme {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank(message = "Theme name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "font_family")
    private String fontFamily;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // One-to-many relationships
    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    // Constructors
    public Theme() {}

    public Theme(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Theme(String id, String name, String primaryColor, String secondaryColor, String fontFamily) {
        this.id = id;
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.fontFamily = fontFamily;
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

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
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

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", primaryColor='" + primaryColor + '\'' +
                ", secondaryColor='" + secondaryColor + '\'' +
                ", fontFamily='" + fontFamily + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
