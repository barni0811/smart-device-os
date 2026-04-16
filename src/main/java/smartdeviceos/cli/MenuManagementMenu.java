package smartdeviceos.cli;

import smartdeviceos.entity.Menu;
import smartdeviceos.entity.User;
import smartdeviceos.service.MenuService;
import smartdeviceos.service.DeviceService;
import smartdeviceos.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuManagementMenu {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private AppService appService;
    
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
            System.out.println("\n=== Menu Management ===");
            System.out.println("1. Select Menu to Manage");
            System.out.println("2. Create Menu");
            System.out.println("3. Modify Menu");
            System.out.println("4. List Menus");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> selectMenuToManage();
                    case 2 -> createMenu();
                    case 3 -> modifyMenu();
                    case 4 -> listMenus();
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
    
    private void selectMenuToManage() {
        if (currentDeviceId == null) {
            System.out.println("No device currently selected!");
            return;
        }
        
        try {
            // Get only main menus (not submenus)
            var menus = menuService.getMainMenusByDeviceId(currentDeviceId);
            
            if (menus.isEmpty()) {
                System.out.println("No menus found. Please create a menu first.");
                return;
            }
            
            System.out.println("\n=== Select Menu ===");
            for (int i = 0; i < menus.size(); i++) {
                var menu = menus.get(i);
                System.out.println((i + 1) + ". " + menu.getName());
            }
            
            System.out.print("Select menu (1-" + menus.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= menus.size()) {
                    var selectedMenu = menus.get(choice - 1);
                    showMenuManagement(selectedMenu);
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error selecting menu: " + e.getMessage());
        }
    }
    
    private void createMenu() {
        if (currentDeviceId == null) {
            System.out.println("No device currently selected!");
            return;
        }
        
        System.out.print("Enter menu name: ");
        String menuName = scanner.nextLine().trim();
        
        if (menuName.isEmpty()) {
            System.out.println("Menu name cannot be empty!");
            return;
        }
        
        try {
            var menu = menuService.createMainMenu(currentDeviceId, menuName);
            System.out.println("Menu created successfully: " + menu.getName());
        } catch (Exception e) {
            System.out.println("Error creating menu: " + e.getMessage());
        }
    }
    
    private void createDefaultMenuWithApps() {
        if (currentDeviceId == null) {
            System.out.println("No device currently selected!");
            return;
        }
        
        try {
            System.out.println("Creating Default Family Menu with 20 family-friendly applications...");
            menuService.createDefaultMenuWithApps(currentDeviceId);
            System.out.println("Default Family Menu created successfully with 20 applications:");
            System.out.println("1. GPS Navigator - Real-time GPS navigation and maps");
            System.out.println("2. Family Cookbook - Store and share family recipes");
            System.out.println("3. Chess Master - Play chess with family members");
            System.out.println("4. Photo Gallery - Store and organize family photos");
            System.out.println("5. Weather Station - Check weather forecasts");
            System.out.println("6. Shopping List - Shared family shopping lists");
            System.out.println("7. Calendar - Family events and appointments");
            System.out.println("8. Music Player - Play family music collection");
            System.out.println("9. Video Player - Watch family videos");
            System.out.println("10. Notes - Family notes and reminders");
            System.out.println("11. Calculator - Basic calculator for homework");
            System.out.println("12. Camera - Take family photos");
            System.out.println("13. Messages - Family chat and messaging");
            System.out.println("14. Emergency Contacts - Important family contacts");
            System.out.println("15. Fitness Tracker - Track family fitness activities");
            System.out.println("16. Recipe Timer - Timer for cooking recipes");
            System.out.println("17. Drawing Pad - Digital drawing for kids");
            System.out.println("18. Story Books - Digital story books for children");
            System.out.println("19. Budget Tracker - Family budget management");
            System.out.println("20. Games Center - Collection of family games");
        } catch (Exception e) {
            System.out.println("Error creating default menu with apps: " + e.getMessage());
        }
    }
    
    private void modifyMenu() {
        if (currentDeviceId == null) {
            System.out.println("No device currently selected!");
            return;
        }
        
        try {
            var menus = menuService.getMenusByDeviceId(currentDeviceId);
            if (menus.isEmpty()) {
                System.out.println("No menus found. Please create a menu first.");
                return;
            }
            
            System.out.println("\n=== Select Menu to Modify ===");
            for (int i = 0; i < menus.size(); i++) {
                var menu = menus.get(i);
                System.out.println((i + 1) + ". " + menu.getName());
            }
            
            System.out.print("Select menu (1-" + menus.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= menus.size()) {
                    var selectedMenu = menus.get(choice - 1);
                    
                    System.out.print("Enter new name for '" + selectedMenu.getName() + "': ");
                    String newName = scanner.nextLine().trim();
                    
                    if (newName.isEmpty()) {
                        System.out.println("New name cannot be empty!");
                        return;
                    }
                    
                    selectedMenu.setName(newName);
                    menuService.saveMenu(selectedMenu);
                    System.out.println("Menu updated successfully: " + newName);
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error modifying menu: " + e.getMessage());
        }
    }
    
    private void showMenuManagement(Menu selectedMenu) {
        showMenuManagement(selectedMenu, false);
    }
    
    private void showMenuManagement(Menu selectedMenu, boolean isSubmenu) {
        while (true) {
            String title = isSubmenu ? "Submenu Management" : "Menu Management";
            System.out.println("\n=== " + title + " ===");
            if (isSubmenu) {
                // Submenu view - sequential numbering
                System.out.println("1. List Submenu Items");
                System.out.println("2. Add Application");
                System.out.println("3. Add File");
                System.out.println("4. Rename File");
                System.out.println("5. Remove File");
                System.out.println("6. Remove Application");
                System.out.println("7. Back to Menu Management");
                System.out.print("Select an option (1-7): ");
            } else {
                // Main menu view
                System.out.println("1. List Menu Items");
                System.out.println("2. Open Submenu");
                System.out.println("3. Add Application");
                System.out.println("4. Add File");
                System.out.println("5. Add Submenu");
                System.out.println("6. Rename File");
                System.out.println("7. Rename Submenu");
                System.out.println("8. Remove Submenu");
                System.out.println("9. Remove File");
                System.out.println("10. Remove Application");
                System.out.println("11. Back to Menu Management");
                System.out.print("Select an option (1-11): ");
            }
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                if (isSubmenu) {
                    // Submenu mapping
                    switch (choice) {
                        case 1 -> listMenuItems(selectedMenu);
                        case 2 -> addApplication(selectedMenu);
                        case 3 -> addFileToMenu(selectedMenu);
                        case 4 -> renameFile(selectedMenu);
                        case 5 -> removeFile(selectedMenu);
                        case 6 -> removeApplication(selectedMenu);
                        case 7 -> { return; }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } else {
                    // Main menu mapping
                    switch (choice) {
                        case 1 -> listMenuItems(selectedMenu);
                        case 2 -> openSubmenu(selectedMenu);
                        case 3 -> addApplication(selectedMenu);
                        case 4 -> addFileToMenu(selectedMenu);
                        case 5 -> addSubmenu(selectedMenu);
                        case 6 -> renameFile(selectedMenu);
                        case 7 -> renameSubmenu(selectedMenu);
                        case 8 -> removeSubmenu(selectedMenu);
                        case 9 -> removeFile(selectedMenu);
                        case 10 -> removeApplication(selectedMenu);
                        case 11 -> { return; }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addApplication(Menu selectedMenu) {
        try {
            // Get all applications
            var apps = appService.getAllActiveApps();
            if (apps.isEmpty()) {
                System.out.println("No applications found. Please create applications first.");
                return;
            }
            
            System.out.println("\n=== Select Application ===");
            for (int i = 0; i < apps.size(); i++) {
                var app = apps.get(i);
                System.out.println((i + 1) + ". " + app.getName());
            }
            
            System.out.print("Select application (1-" + apps.size() + "): ");
            try {
                int appChoice = Integer.parseInt(scanner.nextLine());
                if (appChoice >= 1 && appChoice <= apps.size()) {
                    var selectedApp = apps.get(appChoice - 1);
                    
                    try {
                        // Use app name as item name automatically
                        var menuItem = menuService.addApplicationToMenu(selectedMenu.getId(), selectedApp.getId(), selectedApp.getName(), 1);
                        System.out.println("Application added to menu successfully!");
                    } catch (Exception e) {
                        String errorMsg = e.getMessage();
                        if (errorMsg != null && (errorMsg.contains("Unique index") || errorMsg.contains("constraint") || errorMsg.contains("23505"))) {
                            System.out.println("This application is already in the menu!");
                        } else if (e.getCause() != null && e.getCause().getMessage() != null) {
                            String causeMsg = e.getCause().getMessage();
                            if (causeMsg.contains("Unique index") || causeMsg.contains("constraint") || causeMsg.contains("23505")) {
                                System.out.println("This application is already in the menu!");
                            } else {
                                System.out.println("Error adding application to menu: " + causeMsg);
                            }
                        } else {
                            System.out.println("Error adding application to menu: " + errorMsg);
                        }
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unique index") || errorMsg.contains("constraint") || errorMsg.contains("23505"))) {
                System.out.println("This application is already in the menu!");
            } else if (e.getCause() != null && e.getCause().getMessage() != null) {
                String causeMsg = e.getCause().getMessage();
                if (causeMsg.contains("Unique index") || causeMsg.contains("constraint") || causeMsg.contains("23505")) {
                    System.out.println("This application is already in the menu!");
                } else {
                    System.out.println("Error adding application to menu: " + causeMsg);
                }
            } else {
                System.out.println("Error adding application to menu: " + errorMsg);
            }
        }
    }
    
    private void listMenuItems(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            if (menuItems.isEmpty()) {
                System.out.println("No items found in this menu.");
                return;
            }
            
            System.out.println("\n=== Menu Items ===");
            for (int i = 0; i < menuItems.size(); i++) {
                var item = menuItems.get(i);
                String type;
                if (item.getSubmenu() != null) {
                    type = "[Submenu]";
                } else if (item.getApp() != null) {
                    type = "[App]";
                } else {
                    type = "[File]";
                }
                System.out.println((i + 1) + ". " + item.getName() + " " + type);
            }
        } catch (Exception e) {
            System.out.println("Error listing menu items: " + e.getMessage());
        }
    }
    
    private void addSubmenu(Menu selectedMenu) {
        try {
            System.out.print("Enter submenu name: ");
            String submenuName = scanner.nextLine().trim();
            
            if (submenuName.isEmpty()) {
                System.out.println("Submenu name cannot be empty!");
                return;
            }
            
            // Get current menu items to determine next position
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            int nextPosition = menuItems.size() + 1;
            
            // Create the submenu menu entity
            var submenu = menuService.createSubmenu(selectedMenu.getId(), submenuName);
            // Add as menu item to link it to parent menu
            try {
                menuService.addSubmenuToMenu(selectedMenu.getId(), submenu.getId(), submenuName, nextPosition);
                System.out.println("Submenu created successfully: " + submenu.getName());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error creating submenu: " + e.getMessage());
        }
    }
    
    private void addFileToMenu(Menu selectedMenu) {
        try {
            System.out.print("Enter file name: ");
            String fileName = scanner.nextLine().trim();
            
            if (fileName.isEmpty()) {
                System.out.println("File name cannot be empty!");
                return;
            }
            
            // Get current menu items to determine next position and check for duplicates
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            
            // Check if file with same name already exists
            boolean fileExists = menuItems.stream()
                .anyMatch(item -> item.getApp() == null && item.getSubmenu() == null && item.getName().equals(fileName));
            
            if (fileExists) {
                System.out.println("A file with this name already exists in the menu!");
                return;
            }
            
            int nextPosition = menuItems.size() + 1;
            
            menuService.addFileToMenu(selectedMenu.getId(), fileName, nextPosition);
            System.out.println("File added to menu successfully: " + fileName);
        } catch (Exception e) {
            System.out.println("Error adding file to menu: " + e.getMessage());
        }
    }
    
    private void renameSubmenu(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            var submenus = menuItems.stream()
                .filter(item -> item.getSubmenu() != null)
                .toList();
            
            if (submenus.isEmpty()) {
                System.out.println("No submenus found to rename.");
                return;
            }
            
            System.out.println("\n=== Select Submenu to Rename ===");
            for (int i = 0; i < submenus.size(); i++) {
                var item = submenus.get(i);
                System.out.println((i + 1) + ". " + item.getName());
            }
            
            System.out.print("Select submenu to rename (1-" + submenus.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= submenus.size()) {
                    var itemToRename = submenus.get(choice - 1);
                    // Get submenu ID to avoid lazy loading issue
                    String submenuId = itemToRename.getSubmenu().getId();
                    
                    System.out.print("Enter new name for submenu: ");
                    String newName = scanner.nextLine().trim();
                    
                    if (newName.isEmpty()) {
                        System.out.println("Submenu name cannot be empty!");
                        return;
                    }
                    
                    // Fetch submenu fresh from service to avoid lazy loading
                    var submenuOpt = menuService.findMenuById(submenuId);
                    if (submenuOpt.isPresent()) {
                        var submenu = submenuOpt.get();
                        submenu.setName(newName);
                        menuService.saveMenu(submenu);
                        // Also update the menu item name
                        menuService.renameMenuItem(itemToRename.getId(), newName);
                        System.out.println("Submenu renamed successfully to: " + newName);
                    } else {
                        System.out.println("Error: Could not find submenu to rename.");
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error renaming submenu: " + e.getMessage());
        }
    }
    
    private void openSubmenu(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            var submenus = menuItems.stream()
                .filter(item -> item.getSubmenu() != null)
                .toList();
            
            if (submenus.isEmpty()) {
                System.out.println("No submenus found in this menu.");
                return;
            }
            
            System.out.println("\n=== Select Submenu to Open ===");
            for (int i = 0; i < submenus.size(); i++) {
                var item = submenus.get(i);
                System.out.println((i + 1) + ". " + item.getName());
            }
            
            System.out.print("Select submenu (1-" + submenus.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= submenus.size()) {
                    var selectedSubmenu = submenus.get(choice - 1).getSubmenu();
                    showMenuManagement(selectedSubmenu, true); // true = isSubmenu
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error opening submenu: " + e.getMessage());
        }
    }
    
    private void renameFile(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            var files = menuItems.stream()
                .filter(item -> item.getApp() == null && item.getSubmenu() == null)
                .toList();
            
            if (files.isEmpty()) {
                System.out.println("No files found to rename.");
                return;
            }
            
            System.out.println("\n=== Select File to Rename ===");
            for (int i = 0; i < files.size(); i++) {
                System.out.println((i + 1) + ". " + files.get(i).getName());
            }
            System.out.print("Select file (1-" + files.size() + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice >= 1 && choice <= files.size()) {
                var selectedItem = files.get(choice - 1);
                System.out.print("Enter new file name (leave blank to keep '" + selectedItem.getName() + "'): ");
                String newName = scanner.nextLine().trim();
                
                if (newName.isEmpty()) {
                    System.out.println("No changes made. File name remains: " + selectedItem.getName());
                } else {
                    menuService.renameMenuItem(selectedItem.getId(), newName);
                    System.out.println("File renamed successfully: " + newName);
                }
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error renaming file: " + e.getMessage());
        }
    }
    
    private void removeSubmenu(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            var submenus = menuItems.stream()
                .filter(item -> item.getSubmenu() != null)
                .toList();
            
            if (submenus.isEmpty()) {
                System.out.println("No submenus found to remove.");
                return;
            }
            
            System.out.println("\n=== Select Submenu to Remove ===");
            for (int i = 0; i < submenus.size(); i++) {
                var item = submenus.get(i);
                System.out.println((i + 1) + ". " + item.getName());
            }
            
            System.out.print("Select submenu to remove (1-" + submenus.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= submenus.size()) {
                    var itemToRemove = submenus.get(choice - 1);
                    menuService.removeMenuItem(itemToRemove.getId());
                    System.out.println("Submenu removed successfully!");
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error removing submenu: " + e.getMessage());
        }
    }
    
    private void removeApplication(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            var apps = menuItems.stream()
                .filter(item -> item.getApp() != null)
                .toList();
            
            if (apps.isEmpty()) {
                System.out.println("No applications found to remove.");
                return;
            }
            
            System.out.println("\n=== Select Application to Remove ===");
            for (int i = 0; i < apps.size(); i++) {
                var item = apps.get(i);
                System.out.println((i + 1) + ". " + item.getName());
            }
            
            System.out.print("Select application to remove (1-" + apps.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= apps.size()) {
                    var itemToRemove = apps.get(choice - 1);
                    menuService.removeMenuItem(itemToRemove.getId());
                    System.out.println("Application removed from menu successfully!");
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error removing application: " + e.getMessage());
        }
    }
    
    private void removeFile(Menu selectedMenu) {
        try {
            var menuItems = menuService.getMenuItems(selectedMenu.getId());
            var files = menuItems.stream()
                .filter(item -> item.getApp() == null && item.getSubmenu() == null)
                .toList();
            
            if (files.isEmpty()) {
                System.out.println("No files found to remove.");
                return;
            }
            
            System.out.println("\n=== Select File to Remove ===");
            for (int i = 0; i < files.size(); i++) {
                var item = files.get(i);
                System.out.println((i + 1) + ". " + item.getName());
            }
            
            System.out.print("Select file to remove (1-" + files.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= files.size()) {
                    var itemToRemove = files.get(choice - 1);
                    menuService.removeMenuItem(itemToRemove.getId());
                    System.out.println("File removed from menu successfully!");
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error removing file: " + e.getMessage());
        }
    }
    
    private void addSubmenuToMenu() {
        System.out.println("Submenu functionality would be implemented here.");
    }
    
    private void listMenus() {
        if (currentDeviceId == null) {
            System.out.println("No device currently selected!");
            return;
        }
        
        try {
            // Get only main menus (not submenus)
            var menus = menuService.getMainMenusByDeviceId(currentDeviceId);
            
            System.out.println("\n=== All Menus ===");
            if (menus.isEmpty()) {
                System.out.println("No menus found.");
            } else {
                for (int i = 0; i < menus.size(); i++) {
                    var menu = menus.get(i);
                    System.out.println((i + 1) + ". " + menu.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing menus: " + e.getMessage());
        }
    }
}
