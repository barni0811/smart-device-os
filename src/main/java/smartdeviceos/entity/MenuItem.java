package smartdeviceos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu_items", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_app", columnNames = {"menu_id", "app_id"}),
        @UniqueConstraint(name = "uk_menu_submenu", columnNames = {"menu_id", "submenu_id"})
    })
public class MenuItem {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank(message = "Menu item name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submenu_id")
    private Menu submenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "position")
    private Integer position = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public MenuItem() {}

    public MenuItem(String id, String name, Menu menu) {
        this.id = id;
        this.name = name;
        this.menu = menu;
    }

    public MenuItem(String id, String name, Menu menu, App app, Icon icon) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.app = app;
        this.icon = icon;
    }

    public MenuItem(String id, String name, Menu menu, Menu submenu) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.submenu = submenu;
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

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Menu getSubmenu() {
        return submenu;
    }

    public void setSubmenu(Menu submenu) {
        this.submenu = submenu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isApplication() {
        return app != null;
    }

    public boolean isSubmenu() {
        return submenu != null;
    }

    public boolean isLeaf() {
        return app == null && submenu == null;
    }

    @Override
    public String toString() {
        String type = "Unknown";
        if (isApplication()) {
            type = "App: " + app.getName();
        } else if (isSubmenu()) {
            type = "Submenu: " + submenu.getName();
        } else if (isLeaf()) {
            type = "Leaf";
        }
        
        return "MenuItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", position=" + position +
                '}';
    }
}
