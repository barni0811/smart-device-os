package smartdeviceos.cli;

import smartdeviceos.service.AppService;
import smartdeviceos.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ApplicationManagementMenu {
    
    @Autowired
    private AppService appService;
    
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
            System.out.println("\n=== Application Management ===");
            System.out.println("1. Create Application");
            System.out.println("2. List Applications");
            System.out.println("3. Remove Application");
            System.out.println("4. Modify Application");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> createApplication();
                    case 2 -> listApplications();
                    case 3 -> removeApplication();
                    case 4 -> modifyApplication();
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
    
    private void createApplication() {
        try {
            System.out.print("Enter application name: ");
            String appName = scanner.nextLine().trim();
            
            if (appName.isEmpty()) {
                System.out.println("Application name cannot be empty!");
                return;
            }
            
            var app = appService.createApp(appName, null);
            System.out.println("Application created successfully: " + app.getName());
        } catch (Exception e) {
            System.out.println("Error creating application: " + e.getMessage());
        }
    }
    
    private void createDefaultIPhoneApps() {
        try {
            System.out.println("Creating 20 default iPhone applications...");
            appService.createDefaultIPhoneApps();
            System.out.println("Default iPhone apps created successfully!");
            System.out.println("Apps created: Phone, Messages, Photos, Camera, Maps, Weather, Clock, Calendar, Contacts, Notes, Reminders, Mail, Music, Safari, Settings, App Store, Calculator, Voice Memos, Compass, Chess");
        } catch (Exception e) {
            System.out.println("Error creating default iPhone apps: " + e.getMessage());
        }
    }
    
    private void listApplications() {
        try {
            var apps = appService.getAllActiveApps();
            System.out.println("\n=== All Applications ===");
            if (apps.isEmpty()) {
                System.out.println("No applications found.");
            } else {
                apps.forEach(app -> System.out.println(app.getName()));
            }
        } catch (Exception e) {
            System.out.println("Error listing applications: " + e.getMessage());
        }
    }
    
    private void removeApplication() {
        try {
            var apps = appService.getAllActiveApps();
            if (apps.isEmpty()) {
                System.out.println("No applications found. Please create applications first.");
                return;
            }
            
            System.out.println("\n=== Select Application to Remove ===");
            for (int i = 0; i < apps.size(); i++) {
                var app = apps.get(i);
                System.out.println((i + 1) + ". " + app.getName());
            }
            
            System.out.print("Select application (1-" + apps.size() + "): ");
            try {
                int appChoice = Integer.parseInt(scanner.nextLine());
                if (appChoice >= 1 && appChoice <= apps.size()) {
                    var selectedApp = apps.get(appChoice - 1);
                    
                    System.out.print("Are you sure you want to remove '" + selectedApp.getName() + "'? (yes/no): ");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    
                    if (confirmation.equals("yes")) {
                        appService.deleteApp(selectedApp.getId());
                        System.out.println("Application removed successfully: " + selectedApp.getName());
                    } else {
                        System.out.println("Application removal cancelled.");
                    }
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
    
    private void modifyApplication() {
        try {
            var apps = appService.getAllActiveApps();
            if (apps.isEmpty()) {
                System.out.println("No applications found. Please create applications first.");
                return;
            }
            
            System.out.println("\n=== Select Application to Modify ===");
            for (int i = 0; i < apps.size(); i++) {
                var app = apps.get(i);
                System.out.println((i + 1) + ". " + app.getName());
            }
            
            System.out.print("Select application (1-" + apps.size() + "): ");
            try {
                int appChoice = Integer.parseInt(scanner.nextLine());
                if (appChoice >= 1 && appChoice <= apps.size()) {
                    var selectedApp = apps.get(appChoice - 1);
                    
                    System.out.print("Enter new application name (leave blank to keep '" + selectedApp.getName() + "'): ");
                    String newName = scanner.nextLine().trim();
                    
                    // Update only if name was changed
                    if (!newName.isEmpty()) {
                        appService.updateApp(selectedApp.getId(), newName);
                        System.out.println("Application modified successfully: " + selectedApp.getName());
                    } else {
                        System.out.println("No changes made. Application remains unchanged.");
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error modifying application: " + e.getMessage());
        }
    }
}
