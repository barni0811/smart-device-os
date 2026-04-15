package smartdeviceos.cli;

import smartdeviceos.entity.User;
import smartdeviceos.service.UserService;
import smartdeviceos.service.AppService;
import smartdeviceos.service.MenuService;
import smartdeviceos.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserManagementMenu {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private DeviceService deviceService;
    
    private final Scanner scanner = new Scanner(System.in);
    
    public void showMenu() {
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
            System.out.println("User name cannot be empty!");
            return;
        }
        
        try {
            var user = userService.createUser(name);
            System.out.println("User created successfully: " + user.getName());
            
            // Automatically create default iPhone apps for the new user
            System.out.println("Creating default iPhone applications for new user...");
            appService.createDefaultIPhoneApps();
            System.out.println("Default iPhone apps created successfully for new user!");
            
            // Create default device, menu and submenu for the new user
            System.out.println("Creating default device and menu for new user...");
            var device = deviceService.createDevice(user.getId(), "default_device");
            var menu = menuService.createMainMenu(device.getId(), "default_menu");
            var submenu = menuService.createSubmenu(menu.getId(), "default_submenu");
            // Add submenu as a menu item to the parent menu
            menuService.addSubmenuToMenu(menu.getId(), submenu.getId(), "default_submenu", 1);
            System.out.println("Default device, menu and submenu created successfully for new user!");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
    
    private void modifyUser() {
        User selectedUser = searchAndSelectUser();
        if (selectedUser == null) {
            return;
        }
        
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine().trim();
        
        if (newName.isEmpty()) {
            System.out.println("New name cannot be empty!");
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
        
        System.out.print("Are you sure you want to delete user '" + selectedUser.getName() + "'? (y/N): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            try {
                userService.deleteUser(selectedUser.getId());
                System.out.println("User deleted successfully!");
            } catch (Exception e) {
                System.out.println("Error deleting user: " + e.getMessage());
            }
        } else {
            System.out.println("User deletion cancelled.");
        }
    }
    
    private void listUsers() {
        try {
            var users = userService.getAllUsers();
            System.out.println("\n=== All Users ===");
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                users.forEach(user -> System.out.println("Name: " + user.getName()));
            }
        } catch (Exception e) {
            System.out.println("Error listing users: " + e.getMessage());
        }
    }
    
    private User searchAndSelectUser() {
        try {
            var users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found. Please create a user first.");
                return null;
            }
            
            // Always show selection menu, never auto-select even for single user
            
            System.out.println("\n=== Select User ===");
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i).getName());
            }
            
            System.out.print("Select user (1-" + users.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= users.size()) {
                    return users.get(choice - 1);
                } else {
                    System.out.println("Invalid selection.");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error getting users: " + e.getMessage());
            return null;
        }
    }
}
