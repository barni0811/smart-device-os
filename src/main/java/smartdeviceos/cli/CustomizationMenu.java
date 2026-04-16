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
            System.out.println("4. Select Theme");
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
        try {
            var wallpapers = customizationService.getAllWallpapers();
            if (wallpapers.isEmpty()) {
                System.out.println("No wallpapers found. Please create a wallpaper first.");
                return;
            }
            
            if (currentDeviceId == null) {
                System.out.println("No device currently selected!");
                return;
            }
            
            var deviceOpt = deviceService.findDeviceById(currentDeviceId);
            
            System.out.println("\n=== Select Wallpaper ===");
            for (int i = 0; i < wallpapers.size(); i++) {
                String activeTag = (deviceOpt.isPresent() && 
                    deviceOpt.get().getWallpaper() != null && 
                    deviceOpt.get().getWallpaper().getId().equals(wallpapers.get(i).getId())) ? " [active]" : "";
                System.out.println((i + 1) + ". " + wallpapers.get(i).getName() + activeTag);
            }
            System.out.print("Select wallpaper (1-" + wallpapers.size() + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice >= 1 && choice <= wallpapers.size()) {
                var wallpaper = wallpapers.get(choice - 1);
                customizationService.selectWallpaper(currentDeviceId, wallpaper.getId());
                System.out.println("Wallpaper selected successfully!");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
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
        
        if (name.isEmpty() || primaryColor.isEmpty() || secondaryColor.isEmpty()) {
            System.out.println("Theme name and colors are required!");
            return;
        }
        
        try {
            var theme = customizationService.addTheme(name, primaryColor, secondaryColor, "Arial");
            System.out.println("Theme added successfully: " + theme.getName());
        } catch (Exception e) {
            System.out.println("Error adding theme: " + e.getMessage());
        }
    }
    
    private void changeTheme() {
        try {
            var themes = customizationService.getAllThemes();
            if (themes.isEmpty()) {
                System.out.println("No themes found. Please create a theme first.");
                return;
            }
            
            if (currentDeviceId == null) {
                System.out.println("No device currently selected!");
                return;
            }
            
            var deviceOpt = deviceService.findDeviceById(currentDeviceId);
            
            System.out.println("\n=== Select Theme ===");
            for (int i = 0; i < themes.size(); i++) {
                String activeTag = (deviceOpt.isPresent() && 
                    deviceOpt.get().getTheme() != null && 
                    deviceOpt.get().getTheme().getId().equals(themes.get(i).getId())) ? " [active]" : "";
                System.out.println((i + 1) + ". " + themes.get(i).getName() + activeTag);
            }
            System.out.print("Select theme (1-" + themes.size() + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice >= 1 && choice <= themes.size()) {
                var theme = themes.get(choice - 1);
                customizationService.changeTheme(currentDeviceId, theme.getId());
                System.out.println("Theme changed successfully!");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
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
                // Get current device's wallpaper to show [active] tag
                String activeWallpaperId = null;
                if (currentDeviceId != null) {
                    var deviceOpt = deviceService.findDeviceById(currentDeviceId);
                    if (deviceOpt.isPresent() && deviceOpt.get().getWallpaper() != null) {
                        activeWallpaperId = deviceOpt.get().getWallpaper().getId();
                    }
                }
                
                for (var wallpaper : wallpapers) {
                    String activeTag = (activeWallpaperId != null && activeWallpaperId.equals(wallpaper.getId())) ? " [active]" : "";
                    System.out.println("Name: " + wallpaper.getName() + activeTag + ", Path: " + wallpaper.getImagePath());
                }
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
                // Get current device's theme to show [active] tag
                String activeThemeId = null;
                if (currentDeviceId != null) {
                    var deviceOpt = deviceService.findDeviceById(currentDeviceId);
                    if (deviceOpt.isPresent() && deviceOpt.get().getTheme() != null) {
                        activeThemeId = deviceOpt.get().getTheme().getId();
                    }
                }
                
                for (var theme : themes) {
                    String activeTag = (activeThemeId != null && activeThemeId.equals(theme.getId())) ? " [active]" : "";
                    System.out.println("Name: " + theme.getName() + activeTag + 
                        ", Primary: " + theme.getPrimaryColor() + ", Secondary: " + theme.getSecondaryColor());
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing themes: " + e.getMessage());
        }
    }
}
