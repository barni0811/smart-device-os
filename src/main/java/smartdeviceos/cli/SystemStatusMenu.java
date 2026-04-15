package smartdeviceos.cli;

import smartdeviceos.service.UserService;
import smartdeviceos.service.AppService;
import smartdeviceos.service.IconService;
import smartdeviceos.service.CustomizationService;
import smartdeviceos.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SystemStatusMenu {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private IconService iconService;
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private DeviceService deviceService;
    
    private final Scanner scanner = new Scanner(System.in);
    private String currentUserId;
    
    public void setUserId(String userId) {
        this.currentUserId = userId;
    }
    
    public void showStatus() {
        System.out.println("\n=== System Status ===");
        
        try {
            var users = userService.getAllUsers();
            System.out.println("\n=== Users (" + users.size() + ") ===");
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                users.forEach(user -> System.out.println("- " + user.getName()));
            }
            
            var apps = appService.getAllActiveApps();
            System.out.println("\n=== Applications (" + apps.size() + ") ===");
            if (apps.isEmpty()) {
                System.out.println("No applications found.");
            } else {
                apps.forEach(app -> System.out.println("- " + app.getName()));
            }
            
            var icons = iconService.getAllIcons();
            System.out.println("\n=== Icons (" + icons.size() + ") ===");
            if (icons.isEmpty()) {
                System.out.println("No icons found.");
            } else {
                icons.forEach(icon -> System.out.println("- " + icon.getName()));
            }
            
            var wallpapers = customizationService.getAllWallpapers();
            System.out.println("\n=== Wallpapers (" + wallpapers.size() + ") ===");
            if (wallpapers.isEmpty()) {
                System.out.println("No wallpapers found.");
            } else {
                wallpapers.forEach(wallpaper -> System.out.println("- " + wallpaper.getName()));
            }
            
            var themes = customizationService.getAllThemes();
            System.out.println("\n=== Themes (" + themes.size() + ") ===");
            if (themes.isEmpty()) {
                System.out.println("No themes found.");
            } else {
                themes.forEach(theme -> System.out.println("- " + theme.getName()));
            }
            
            var devices = deviceService.getDevicesByUserId(currentUserId);
            System.out.println("\n=== Devices (" + devices.size() + ") ===");
            if (devices.isEmpty()) {
                System.out.println("No devices found.");
            } else {
                devices.forEach(device -> System.out.println("- " + device.getName()));
            }
            
        } catch (Exception e) {
            System.out.println("Error retrieving system status: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
