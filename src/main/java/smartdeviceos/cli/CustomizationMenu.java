package smartdeviceos.cli;

import smartdeviceos.service.CustomizationService;
import smartdeviceos.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CustomizationMenu {
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private DeviceService deviceService;
    
    private final Scanner scanner = new Scanner(System.in);
    private String currentUserId;
    private String currentDeviceId;
    
    public void setUserId(String userId) {
        this.currentUserId = userId;
    }
    
    public void setDeviceId(String deviceId) {
        this.currentDeviceId = deviceId;
    }
    
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Customization ===");
            System.out.println("1. Add Wallpaper");
            System.out.println("2. Select Wallpaper");
            System.out.println("3. Add Theme");
            System.out.println("4. Change Theme");
            System.out.println("5. List Wallpapers");
            System.out.println("6. List Themes");
            System.out.println("7. Back to Main Menu");
            System.out.print("Select an option (1-7): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> addWallpaper();
                    case 2 -> selectWallpaper();
                    case 3 -> addTheme();
                    case 4 -> changeTheme();
                    case 5 -> listWallpapers();
                    case 6 -> listThemes();
                    case 7 -> { return; }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addWallpaper() {
        System.out.print("Enter wallpaper name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter image path: ");
        String imagePath = scanner.nextLine().trim();
        
        if (name.isEmpty() || imagePath.isEmpty()) {
            System.out.println("Wallpaper name and image path cannot be empty!");
            return;
        }
        
        try {
            var wallpaper = customizationService.addWallpaper(name, imagePath);
            System.out.println("Wallpaper added successfully: " + wallpaper.getName());
        } catch (Exception e) {
            System.out.println("Error adding wallpaper: " + e.getMessage());
        }
    }
    
    private void selectWallpaper() {
        System.out.print("Enter device name: ");
        String deviceName = scanner.nextLine().trim();
        
        System.out.print("Enter wallpaper name: ");
        String wallpaperName = scanner.nextLine().trim();
        
        if (deviceName.isEmpty() || wallpaperName.isEmpty()) {
            System.out.println("Device name and wallpaper name cannot be empty!");
            return;
        }
        
        try {
            var deviceOpt = deviceService.findDeviceByNameAndUserId(deviceName, currentUserId);
            if (deviceOpt.isEmpty()) {
                System.out.println("Device not found!");
                return;
            }
            
            var wallpaperOpt = customizationService.findWallpaperByName(wallpaperName);
            if (wallpaperOpt.isEmpty()) {
                System.out.println("Wallpaper not found!");
                return;
            }
            
            customizationService.selectWallpaper(deviceOpt.get().getId(), wallpaperOpt.get().getId());
            System.out.println("Wallpaper selected successfully!");
        } catch (Exception e) {
            System.out.println("Error selecting wallpaper: " + e.getMessage());
        }
    }
    
    private void addTheme() {
        System.out.print("Enter theme name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter primary color: ");
        String primaryColor = scanner.nextLine().trim();
        
        System.out.print("Enter secondary color: ");
        String secondaryColor = scanner.nextLine().trim();
        
        System.out.print("Enter font family: ");
        String fontFamily = scanner.nextLine().trim();
        
        if (name.isEmpty() || primaryColor.isEmpty() || secondaryColor.isEmpty() || fontFamily.isEmpty()) {
            System.out.println("All theme fields are required!");
            return;
        }
        
        try {
            var theme = customizationService.addTheme(name, primaryColor, secondaryColor, fontFamily);
            System.out.println("Theme added successfully: " + theme.getName());
        } catch (Exception e) {
            System.out.println("Error adding theme: " + e.getMessage());
        }
    }
    
    private void changeTheme() {
        System.out.print("Enter device name: ");
        String deviceName = scanner.nextLine().trim();
        
        System.out.print("Enter theme name: ");
        String themeName = scanner.nextLine().trim();
        
        if (deviceName.isEmpty() || themeName.isEmpty()) {
            System.out.println("Device name and theme name cannot be empty!");
            return;
        }
        
        try {
            var deviceOpt = deviceService.findDeviceByNameAndUserId(deviceName, currentUserId);
            if (deviceOpt.isEmpty()) {
                System.out.println("Device not found!");
                return;
            }
            
            var themeOpt = customizationService.findThemeByName(themeName);
            if (themeOpt.isEmpty()) {
                System.out.println("Theme not found!");
                return;
            }
            
            customizationService.changeTheme(deviceOpt.get().getId(), themeOpt.get().getId());
            System.out.println("Theme changed successfully!");
        } catch (Exception e) {
            System.out.println("Error changing theme: " + e.getMessage());
        }
    }
    
    private void listWallpapers() {
        try {
            var wallpapers = customizationService.getAllWallpapers();
            System.out.println("\n=== All Wallpapers ===");
            if (wallpapers.isEmpty()) {
                System.out.println("No wallpapers found.");
            } else {
                wallpapers.forEach(wallpaper -> System.out.println("Name: " + wallpaper.getName() + ", Path: " + wallpaper.getImagePath()));
            }
        } catch (Exception e) {
            System.out.println("Error listing wallpapers: " + e.getMessage());
        }
    }
    
    private void listThemes() {
        try {
            var themes = customizationService.getAllThemes();
            System.out.println("\n=== All Themes ===");
            if (themes.isEmpty()) {
                System.out.println("No themes found.");
            } else {
                themes.forEach(theme -> System.out.println("Name: " + theme.getName() + 
                    ", Primary: " + theme.getPrimaryColor() + ", Secondary: " + theme.getSecondaryColor()));
            }
        } catch (Exception e) {
            System.out.println("Error listing themes: " + e.getMessage());
        }
    }
}
