package smartdeviceos.cli;

import smartdeviceos.service.DeviceService;
import smartdeviceos.service.UserService;
import smartdeviceos.service.MenuService;
import smartdeviceos.service.CustomizationService;
import smartdeviceos.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DeviceMenu {
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private MenuManagementMenu menuManagementMenu;
    
    @Autowired
    private IconManagementMenu iconManagementMenu;
    
    @Autowired
    private ApplicationManagementMenu applicationManagementMenu;
    
    @Autowired
    private CustomizationMenu customizationMenu;
    
    private final Scanner scanner = new Scanner(System.in);
    private String currentUserId;
    private String currentDeviceId;
    
    public void setUserId(String userId) {
        this.currentUserId = userId;
    }
    
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Device Management ===");
            System.out.println("1. Select Device to Manage");
            System.out.println("2. Create Device");
            System.out.println("3. Modify Device");
            System.out.println("4. List Devices");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> selectAndManageDevice();
                    case 2 -> createDevice();
                    case 3 -> modifyDevice();
                    case 4 -> listDevices();
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
    
    private void selectAndManageDevice() {
        try {
            var devices = deviceService.getAllDevicesWithUsers();
            if (devices.isEmpty()) {
                System.out.println("No devices found. Please create a device first.");
                return;
            }
            
            System.out.println("\n=== Select Device ===");
            for (int i = 0; i < devices.size(); i++) {
                var device = devices.get(i);
                System.out.println((i + 1) + ". " + device.getName() + " (Owner: " + device.getUser().getName() + ")");
            }
            
            System.out.print("Select device (1-" + devices.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= devices.size()) {
                    currentDeviceId = devices.get(choice - 1).getId();
                    showDeviceSpecificMenu(devices.get(choice - 1).getName());
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error selecting device: " + e.getMessage());
        }
    }
    
    private void showDeviceSpecificMenu(String deviceName) {
        while (true) {
            System.out.println("\n=== Managing Device: " + deviceName + " ===");
            System.out.println("1. Menu Management");
            System.out.println("2. Icon Management");
            System.out.println("3. Application Management");
            System.out.println("4. Customization (Wallpaper & Theme)");
            System.out.println("5. Back to Device List");
            System.out.print("Select an option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> {
                        menuManagementMenu.setUserId(currentUserId);
                        menuManagementMenu.setDeviceId(currentDeviceId);
                        menuManagementMenu.showMenu();
                    }
                    case 2 -> {
                        iconManagementMenu.setUserId(currentUserId);
                        iconManagementMenu.setDeviceId(currentDeviceId);
                        iconManagementMenu.showMenu();
                    }
                    case 3 -> {
                        applicationManagementMenu.setUserId(currentUserId);
                        applicationManagementMenu.setDeviceId(currentDeviceId);
                        applicationManagementMenu.showMenu();
                    }
                    case 4 -> {
                        customizationMenu.setUserId(currentUserId);
                        customizationMenu.setDeviceId(currentDeviceId);
                        customizationMenu.showMenu();
                    }
                    case 5 -> { return; }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private void createDevice() {
        try {
            // Get all users for owner selection
            var users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found. Please create a user first.");
                return;
            }
            
            System.out.println("\n=== Select Device Owner ===");
            for (int i = 0; i < users.size(); i++) {
                var user = users.get(i);
                System.out.println((i + 1) + ". " + user.getName());
            }
            
            System.out.print("Select owner (1-" + users.size() + "): ");
            try {
                int ownerChoice = Integer.parseInt(scanner.nextLine());
                if (ownerChoice >= 1 && ownerChoice <= users.size()) {
                    var selectedOwner = users.get(ownerChoice - 1);
                    
                    System.out.print("Enter device name: ");
                    String deviceName = scanner.nextLine().trim();
                    
                    if (deviceName.isEmpty()) {
                        System.out.println("Device name cannot be empty!");
                        return;
                    }
                    
                    var device = deviceService.createDevice(selectedOwner.getId(), deviceName);
                    System.out.println("Device created successfully: " + device.getName() + " (Owner: " + selectedOwner.getName() + ")");
                    
                    // Create default wallpaper and theme for the device
                    try {
                        var defaultWallpaper = customizationService.addDefaultWallpaper(device.getId(), "default_wallpaper", "images/default_wallpaper.png");
                        var defaultTheme = customizationService.addDefaultTheme(device.getId(), "light_theme", "#FFFFFF", "#000000", "Arial");
                        customizationService.selectWallpaper(device.getId(), defaultWallpaper.getId());
                        customizationService.changeTheme(device.getId(), defaultTheme.getId());
                    } catch (Exception e) {
                        System.out.println("Note: Could not create default wallpaper/theme for device: " + e.getMessage());
                    }
                    
                    // Create default menu and submenu for the device
                    try {
                        var menu = menuService.createMainMenu(device.getId(), "default_menu");
                        var submenu = menuService.createSubmenu(menu.getId(), "default_submenu");
                        menuService.addSubmenuToMenu(menu.getId(), submenu.getId(), "default_submenu", 1);
                        addDefaultAppsToMenu(menu.getId());
                    } catch (Exception e) {
                        System.out.println("Note: Could not create default menu for device: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error creating device: " + e.getMessage());
        }
    }
    
    private void modifyDevice() {
        try {
            var devices = deviceService.getDevicesByUserId(currentUserId);
            if (devices.isEmpty()) {
                System.out.println("No devices found. Please create a device first.");
                return;
            }
            
            System.out.println("\n=== Select Device to Modify ===");
            for (int i = 0; i < devices.size(); i++) {
                var device = devices.get(i);
                System.out.println((i + 1) + ". " + device.getName());
            }
            
            System.out.print("Select device (1-" + devices.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= devices.size()) {
                    var selectedDevice = devices.get(choice - 1);
                    
                    System.out.print("Enter new name for '" + selectedDevice.getName() + "': ");
                    String newName = scanner.nextLine().trim();
                    
                    if (newName.isEmpty()) {
                        System.out.println("New name cannot be empty!");
                        return;
                    }
                    
                    var updatedDevice = deviceService.updateDevice(selectedDevice.getId(), newName);
                    System.out.println("Device updated successfully: " + updatedDevice.getName());
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error updating device: " + e.getMessage());
        }
    }
    
    private void listDevices() {
        try {
            var devices = deviceService.getAllDevicesWithUsers();
            System.out.println("\n=== All Devices ===");
            if (devices.isEmpty()) {
                System.out.println("No devices found.");
            } else {
                devices.forEach(device -> System.out.println("Name: " + device.getName() + 
                    ", Owner: " + device.getUser().getName() +
                    (device.getWallpaper() != null ? ", Wallpaper: " + device.getWallpaper().getName() : "") +
                    (device.getTheme() != null ? ", Theme: " + device.getTheme().getName() : "")));
            }
        } catch (Exception e) {
            System.out.println("Error listing devices: " + e.getMessage());
        }
    }
    
    private void setDefaultMenu() {
        System.out.print("Enter device name: ");
        String deviceName = scanner.nextLine().trim();
        
        if (deviceName.isEmpty()) {
            System.out.println("Device name cannot be empty!");
            return;
        }
        
        try {
            var deviceOpt = deviceService.findDeviceByNameAndUserId(deviceName, currentUserId);
            if (deviceOpt.isEmpty()) {
                System.out.println("Device not found!");
                return;
            }
            
            deviceService.setAsDefaultMenu(deviceOpt.get().getId());
            System.out.println("Default menu set successfully for device: " + deviceName);
        } catch (Exception e) {
            System.out.println("Error setting default menu: " + e.getMessage());
        }
    }
    
    private void addDefaultAppsToMenu(String menuId) {
        String[] defaultApps = {"App Store", "Calculator", "Calendar", "Camera", "Phone", "Photos", "Settings"};
        int position = 2;
        
        for (String appName : defaultApps) {
            try {
                var appOpt = appService.findAppByName(appName);
                if (appOpt.isPresent()) {
                    menuService.addApplicationToMenu(menuId, appOpt.get().getId(), appName, position);
                    position++;
                }
            } catch (Exception e) {
            }
        }
    }
    
    private void setDefaultMenuForCurrentDevice() {
        if (currentDeviceId == null) {
            System.out.println("No device currently selected!");
            return;
        }
        
        try {
            deviceService.setAsDefaultMenu(currentDeviceId);
            System.out.println("Default menu set successfully for the current device.");
        } catch (Exception e) {
            System.out.println("Error setting default menu: " + e.getMessage());
        }
    }
}
