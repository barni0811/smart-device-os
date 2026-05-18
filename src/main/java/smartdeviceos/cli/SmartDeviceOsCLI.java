package smartdeviceos.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import smartdeviceos.app.AppService;
import smartdeviceos.customization.CustomizationService;
import smartdeviceos.device.Device;
import smartdeviceos.device.DeviceService;
import smartdeviceos.menu.MenuService;
import smartdeviceos.user.UserService;

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
    private CustomizationService customizationService;

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
        Thread cliThread = new Thread(() -> {
            try {
                setupCurrentUser();
                showMainMenu();
            } catch (Exception e) {
                System.err.println("Error in CLI: " + e.getMessage());
            }
        });
        cliThread.start();
    }

    private void setupCurrentUser() {
        try {
            customizationService.cleanupDuplicateWallpapers();
            customizationService.cleanupDuplicateThemes();

            var currentUser = userService.createCurrentUser();
            currentUserId = currentUser.getId();

            appService.createDefaultIPhoneApps();

            var existingDevice = deviceService.findDeviceByNameAndUserId("default_device", currentUserId);
            Device defaultDevice;

            if (existingDevice.isPresent()) {
                defaultDevice = existingDevice.get();
            } else {
                defaultDevice = deviceService.createDevice(currentUserId, "default_device");
            }

            var wallpapers = customizationService.getWallpapersByDeviceId(defaultDevice.getId());
            if (wallpapers == null || wallpapers.isEmpty()) {
                var defaultWallpaper = customizationService.addDefaultWallpaper(defaultDevice.getId(), "default_wallpaper", "images/default_wallpaper.png");
                customizationService.selectWallpaper(defaultDevice.getId(), defaultWallpaper.getId());
            }

            var themes = customizationService.getThemesByDeviceId(defaultDevice.getId());
            if (themes == null || themes.isEmpty()) {
                var defaultTheme = customizationService.addDefaultTheme(defaultDevice.getId(), "light_theme", "#FFFFFF", "#000000", "Arial");
                customizationService.changeTheme(defaultDevice.getId(), defaultTheme.getId());
            }

            var deviceOptForMenu = deviceService.findDeviceByNameAndUserId("default_device", currentUserId);
            if (deviceOptForMenu.isPresent()) {
                var menu = menuService.createMainMenu(deviceOptForMenu.get().getId(), "default_menu");
                var submenu = menuService.createSubmenu(menu.getId(), "default_submenu");
                menuService.addSubmenuToMenu(menu.getId(), submenu.getId(), "default_submenu", 1);
                addDefaultAppsToMenu(menu.getId());
            }
        } catch (Exception e) {
            System.out.println("Error setting up current user: " + e.getMessage());
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
