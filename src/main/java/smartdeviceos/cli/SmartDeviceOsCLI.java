package smartdeviceos.cli;

import smartdeviceos.service.UserService;
import smartdeviceos.service.DeviceService;
import smartdeviceos.service.AppService;
import smartdeviceos.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SmartDeviceOsCLI implements CommandLineRunner {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private UserManagementMenu userManagementMenu;
    
    @Autowired
    private FamilyManagementMenu familyManagementMenu;
    
    @Autowired
    private SystemStatusMenu systemStatusMenu;
    
    @Autowired
    private DeviceMenu deviceMenu;

    private final Scanner scanner = new Scanner(System.in);
    private String currentUserId;

    @Override
    public void run(String... args) throws Exception {
        setupCurrentUser();
        showMainMenu();
    }

    private void setupCurrentUser() {
        try {
            var currentUser = userService.createCurrentUser();
            currentUserId = currentUser.getId();
            
            deviceService.createDevice(currentUserId, "default_device");
            
            // Automatically create default iPhone apps for the default user and device
            System.out.println("Creating default iPhone applications...");
            appService.createDefaultIPhoneApps();
            System.out.println("Default iPhone apps created successfully!");
            
            // Create default menu for the device
            System.out.println("Creating default menu...");
            var deviceOpt = deviceService.findDeviceByNameAndUserId("default_device", currentUserId);
            if (deviceOpt.isPresent()) {
                var menu = menuService.createMainMenu(deviceOpt.get().getId(), "default_menu");
                System.out.println("Default menu created successfully!");
                
                // Create default submenu inside the menu
                System.out.println("Creating default submenu...");
                var submenu = menuService.createSubmenu(menu.getId(), "default_submenu");
                // Add submenu as a menu item to the parent menu
                menuService.addSubmenuToMenu(menu.getId(), submenu.getId(), "default_submenu", 1);
                System.out.println("Default submenu created successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error setting up current user: " + e.getMessage());
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\n=== Smart Device OS Main Menu ===");
            System.out.println("1. User Management");
            System.out.println("2. Family Management");
            System.out.println("3. Device Management");
            System.out.println("4. View System Status");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> userManagementMenu.showMenu();
                    case 2 -> {
                        familyManagementMenu.setUserId(currentUserId);
                        familyManagementMenu.showMenu();
                    }
                    case 3 -> {
                        deviceMenu.setUserId(currentUserId);
                        deviceMenu.showMenu();
                    }
                    case 4 -> {
                        systemStatusMenu.setUserId(currentUserId);
                        systemStatusMenu.showStatus();
                    }
                    case 5 -> {
                        System.out.println("Exiting Smart Device OS CLI. Goodbye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
