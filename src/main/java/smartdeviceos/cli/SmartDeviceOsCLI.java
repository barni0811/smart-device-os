package smartdeviceos.cli;

import smartdeviceos.entity.User;
import smartdeviceos.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

@Component
public class SmartDeviceOsCLI implements CommandLineRunner {
    
    private final UserService userService;
    private final MenuService menuService;
    private final AppService appService;
    private final IconService iconService;
    private final CustomizationService customizationService;
    private final DeviceService deviceService;
    private final FamilyService familyService;
    
    private String currentUserId;
    private Scanner scanner;
    
    public SmartDeviceOsCLI(UserService userService, MenuService menuService, 
                           AppService appService, IconService iconService,
                           CustomizationService customizationService, DeviceService deviceService,
                           FamilyService familyService) {
        this.userService = userService;
        this.menuService = menuService;
        this.appService = appService;
        this.iconService = iconService;
        this.customizationService = customizationService;
        this.deviceService = deviceService;
        this.familyService = familyService;
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void run(String... args) {
        setupCurrentUser();
        
        showMainMenu();
    }
    
    private void setupCurrentUser() {
        try {
            var currentUser = userService.createUser("current_user");
            currentUserId = currentUser.getId();
            
            userService.addFavoriteApps(currentUser, java.util.List.of("recipes", "gps", "games"));
            
            var defaultDevice = deviceService.createDevice(currentUserId, "default_device");
            
            var mainMenu = menuService.createMainMenu(defaultDevice.getId(), "Main Menu");
            
            menuService.addApplicationToMenu(mainMenu.getId(), "recipes", "Recipe App", 1);
            menuService.addApplicationToMenu(mainMenu.getId(), "gps", "GPS Navigation", 2);
            menuService.addApplicationToMenu(mainMenu.getId(), "games", "Games", 3);
            
        } catch (Exception e) {
            var existingUser = userService.findUserByName("current_user");
            if (existingUser.isPresent()) {
                currentUserId = existingUser.get().getId();
            } else {
                System.err.println("Error setting up current user: " + e.getMessage());
                return;
            }
        }
    }
    
    private void showMainMenu() {
        while (true) {
            System.out.println("\n=== Smart Device OS Main Menu ===");
            System.out.println("1. User Management");
            System.out.println("2. Menu Management");
            System.out.println("3. Icon Management");
            System.out.println("4. Application Management");
            System.out.println("5. Customization (Wallpaper & Theme)");
            System.out.println("6. Family Management");
            System.out.println("7. Device Management");
            System.out.println("8. View System Status");
            System.out.println("9. Exit");
            System.out.print("Select an option (1-9): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> userManagementMenu();
                    case 2 -> menuManagementMenu();
                    case 3 -> iconManagementMenu();
                    case 4 -> applicationManagementMenu();
                    case 5 -> customizationMenu();
                    case 6 -> familyManagementMenu();
                    case 7 -> deviceManagementMenu();
                    case 8 -> viewSystemStatus();
                    case 9 -> {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void userManagementMenu() {
        while (true) {
            System.out.println("\n=== User Management ===");
            System.out.println("1. Create User");
            System.out.println("2. Modify User");
            System.out.println("3. Delete User");
            System.out.println("4. List All Users");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> modifyUser();
                    case 3 -> deleteUser();
                    case 4 -> listUsers();
                    case 5 -> { return; }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void createUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        try {
            var user = userService.createUser(name);
            System.out.println("User created successfully: " + user.getName());
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
    
    private void modifyUser() {
        User selectedUser = searchAndSelectUser();
        if (selectedUser == null) {
            return;
        }
        
        System.out.println("Current name: " + selectedUser.getName());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine().trim();
        
        if (newName.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        try {
            var updatedUser = userService.updateUser(selectedUser.getId(), newName);
            System.out.println("User updated successfully: " + updatedUser.getName());
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
    
    private void deleteUser() {
        User selectedUser = searchAndSelectUser();
        if (selectedUser == null) {
            return;
        }
        
        System.out.println("Are you sure you want to delete user '" + selectedUser.getName() + "'? (y/N): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("y")) {
            try {
                userService.deleteUser(selectedUser.getId());
                System.out.println("User deleted successfully!");
            } catch (Exception e) {
                System.out.println("Error deleting user: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private User searchAndSelectUser() {
        System.out.print("Enter username (or part of name): ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty!");
            return null;
        }
        
        var users = userService.getAllUsers().stream()
                .filter(user -> user.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
        
        if (users.isEmpty()) {
            System.out.println("No users found matching: " + searchTerm);
            return null;
        }
        
        if (users.size() == 1) {
            return users.get(0);
        }
        
        System.out.println("Found users:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getName() + " (ID: " + users.get(i).getId() + ")");
        }
        
        System.out.print("Select user (1-" + users.size() + "): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= users.size()) {
                return users.get(choice - 1);
            } else {
                System.out.println("Invalid selection!");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return null;
        }
    }
    
    private void listUsers() {
        var users = userService.getAllUsers();
        System.out.println("\n=== All Users ===");
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(user -> System.out.println("Name: " + user.getName()));
        }
    }
    
    private void menuManagementMenu() {
        System.out.println("\n=== Menu Management ===");
        System.out.println("Menu management functionality would be implemented here.");
        System.out.println("Features include:");
        System.out.println("- Create main menu");
        System.out.println("- Add submenu");
        System.out.println("- Modify menu items");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void iconManagementMenu() {
        while (true) {
            System.out.println("\n=== Icon Management ===");
            System.out.println("1. Add Icon");
            System.out.println("2. Modify Icon");
            System.out.println("3. Delete Icon");
            System.out.println("4. List All Icons");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> addIcon();
                    case 2 -> modifyIcon();
                    case 3 -> deleteIcon();
                    case 4 -> listIcons();
                    case 5 -> { return; }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addIcon() {
        System.out.print("Enter icon name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter image path: ");
        String imagePath = scanner.nextLine().trim();
        
        if (name.isEmpty() || imagePath.isEmpty()) {
            System.out.println("Name and image path cannot be empty!");
            return;
        }
        
        try {
            var icon = iconService.createIcon(name, imagePath);
            System.out.println("Icon created successfully: " + icon.getName());
        } catch (Exception e) {
            System.out.println("Error creating icon: " + e.getMessage());
        }
    }
    
    private void modifyIcon() {
        System.out.print("Enter icon name to modify: ");
        String iconName = scanner.nextLine().trim();
        
        var iconOpt = iconService.findIconByName(iconName);
        if (iconOpt.isEmpty()) {
            System.out.println("Icon not found!");
            return;
        }
        
        var icon = iconOpt.get();
        System.out.println("Current name: " + icon.getName());
        System.out.println("Current image path: " + icon.getImagePath());
        
        System.out.print("Enter new name (or press Enter to keep current): ");
        String newName = scanner.nextLine().trim();
        
        System.out.print("Enter new image path (or press Enter to keep current): ");
        String newImagePath = scanner.nextLine().trim();
        
        if (newName.isEmpty()) newName = icon.getName();
        if (newImagePath.isEmpty()) newImagePath = icon.getImagePath();
        
        try {
            var updatedIcon = iconService.updateIcon(icon.getId(), newName, newImagePath);
            System.out.println("Icon updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating icon: " + e.getMessage());
        }
    }
    
    private void deleteIcon() {
        System.out.print("Enter icon name to delete: ");
        String iconName = scanner.nextLine().trim();
        
        var iconOpt = iconService.findIconByName(iconName);
        if (iconOpt.isEmpty()) {
            System.out.println("Icon not found!");
            return;
        }
        
        System.out.println("Are you sure you want to delete icon '" + iconOpt.get().getName() + "'? (y/N): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("y")) {
            try {
                iconService.deleteIcon(iconOpt.get().getId());
                System.out.println("Icon deleted successfully!");
            } catch (Exception e) {
                System.out.println("Error deleting icon: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void listIcons() {
        var icons = iconService.getAllIcons();
        System.out.println("\n=== All Icons ===");
        if (icons.isEmpty()) {
            System.out.println("No icons found.");
        } else {
            icons.forEach(icon -> System.out.println("ID: " + icon.getId() + ", Name: " + icon.getName() + ", Path: " + icon.getImagePath()));
        }
    }
    
    private void applicationManagementMenu() {
        System.out.println("\n=== Application Management ===");
        System.out.println("Application management functionality would be implemented here.");
        System.out.println("Features include:");
        System.out.println("- Install application");
        System.out.println("- Launch application");
        System.out.println("- View installed applications");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void customizationMenu() {
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
            System.out.println("Name and image path cannot be empty!");
            return;
        }
        
        try {
            var wallpaper = customizationService.addWallpaper(name, imagePath);
            System.out.println("Wallpaper added successfully: " + wallpaper.getName() + " (ID: " + wallpaper.getId() + ")");
        } catch (Exception e) {
            System.out.println("Error adding wallpaper: " + e.getMessage());
        }
    }
    
    private void selectWallpaper() {
        System.out.print("Enter device ID: ");
        String deviceId = scanner.nextLine().trim();
        
        System.out.print("Enter wallpaper ID: ");
        String wallpaperId = scanner.nextLine().trim();
        
        try {
            customizationService.selectWallpaper(deviceId, wallpaperId);
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
        
        if (name.isEmpty()) {
            System.out.println("Theme name cannot be empty!");
            return;
        }
        
        try {
            var theme = customizationService.addTheme(name, primaryColor, secondaryColor, fontFamily);
            System.out.println("Theme added successfully: " + theme.getName() + " (ID: " + theme.getId() + ")");
        } catch (Exception e) {
            System.out.println("Error adding theme: " + e.getMessage());
        }
    }
    
    private void changeTheme() {
        System.out.print("Enter device ID: ");
        String deviceId = scanner.nextLine().trim();
        
        System.out.print("Enter theme ID: ");
        String themeId = scanner.nextLine().trim();
        
        try {
            customizationService.changeTheme(deviceId, themeId);
            System.out.println("Theme changed successfully!");
        } catch (Exception e) {
            System.out.println("Error changing theme: " + e.getMessage());
        }
    }
    
    private void listWallpapers() {
        var wallpapers = customizationService.getAllWallpapers();
        System.out.println("\n=== All Wallpapers ===");
        if (wallpapers.isEmpty()) {
            System.out.println("No wallpapers found.");
        } else {
            wallpapers.forEach(wallpaper -> System.out.println("ID: " + wallpaper.getId() + ", Name: " + wallpaper.getName() + ", Path: " + wallpaper.getImagePath()));
        }
    }
    
    private void listThemes() {
        var themes = customizationService.getAllThemes();
        System.out.println("\n=== All Themes ===");
        if (themes.isEmpty()) {
            System.out.println("No themes found.");
        } else {
            themes.forEach(theme -> System.out.println("ID: " + theme.getId() + ", Name: " + theme.getName() + 
                ", Primary: " + theme.getPrimaryColor() + ", Secondary: " + theme.getSecondaryColor()));
        }
    }
    
    private void familyManagementMenu() {
        System.out.println("\n=== Family Management ===");
        System.out.println("Family management functionality would be implemented here.");
        System.out.println("Features include:");
        System.out.println("- Create family");
        System.out.println("- Add family members");
        System.out.println("- Manage member roles");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void deviceManagementMenu() {
        System.out.println("\n=== Device Management ===");
        System.out.println("Device management functionality would be implemented here.");
        System.out.println("Features include:");
        System.out.println("- Create device");
        System.out.println("- Modify device");
        System.out.println("- Set default menu");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewSystemStatus() {
        System.out.println("\n=== System Status ===");
        System.out.println("Current User ID: " + currentUserId);
        
        try {
            var user = userService.findUserById(currentUserId);
            user.ifPresent(u -> System.out.println("Current User Name: " + u.getName()));
            
            var users = userService.getAllUsers();
            System.out.println("Total Users: " + users.size());
            
            var apps = appService.getAllActiveApps();
            System.out.println("Active Applications: " + apps.size());
            
            var wallpapers = customizationService.getAllWallpapers();
            System.out.println("Available Wallpapers: " + wallpapers.size());
            
            var themes = customizationService.getAllThemes();
            System.out.println("Available Themes: " + themes.size());
            
        } catch (Exception e) {
            System.out.println("Error retrieving system status: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
