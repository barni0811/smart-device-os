package smartdeviceos.cli;

import smartdeviceos.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class IconManagementMenu {
    
    @Autowired
    private IconService iconService;
    
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
            System.out.println("Icon name and image path cannot be empty!");
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
        try {
            var icons = iconService.getAllIcons();
            if (icons.isEmpty()) {
                System.out.println("No icons found. Please create an icon first.");
                return;
            }
            
            System.out.println("\n=== Select Icon to Modify ===");
            for (int i = 0; i < icons.size(); i++) {
                System.out.println((i + 1) + ". " + icons.get(i).getName());
            }
            System.out.println((icons.size() + 1) + ". Cancel");
            System.out.print("Select icon (1-" + (icons.size() + 1) + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == icons.size() + 1) {
                return;
            }
            
            if (choice >= 1 && choice <= icons.size()) {
                var icon = icons.get(choice - 1);
                System.out.println("Current name: " + icon.getName());
                System.out.println("Current image path: " + icon.getImagePath());
                
                System.out.print("Enter new name (leave blank to keep '" + icon.getName() + "'): ");
                String newName = scanner.nextLine().trim();
                
                System.out.print("Enter new image path (leave blank to keep '" + icon.getImagePath() + "'): ");
                String newImagePath = scanner.nextLine().trim();
                
                String finalName = newName.isEmpty() ? icon.getName() : newName;
                String finalPath = newImagePath.isEmpty() ? icon.getImagePath() : newImagePath;
                
                iconService.updateIcon(icon.getId(), finalName, finalPath);
                System.out.println("Icon updated successfully!");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error updating icon: " + e.getMessage());
        }
    }
    
    private void deleteIcon() {
        try {
            var icons = iconService.getAllIcons();
            if (icons.isEmpty()) {
                System.out.println("No icons found. Please create an icon first.");
                return;
            }
            
            System.out.println("\n=== Select Icon to Delete ===");
            for (int i = 0; i < icons.size(); i++) {
                System.out.println((i + 1) + ". " + icons.get(i).getName());
            }
            System.out.println((icons.size() + 1) + ". Cancel");
            System.out.print("Select icon (1-" + (icons.size() + 1) + "): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == icons.size() + 1) {
                return;
            }
            
            if (choice >= 1 && choice <= icons.size()) {
                var icon = icons.get(choice - 1);
                iconService.deleteIcon(icon.getId());
                System.out.println("Icon deleted successfully!");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error deleting icon: " + e.getMessage());
        }
    }
    
    private void listIcons() {
        try {
            var icons = iconService.getAllIcons();
            System.out.println("\n=== All Icons ===");
            if (icons.isEmpty()) {
                System.out.println("No icons found.");
            } else {
                icons.forEach(icon -> System.out.println("Name: " + icon.getName() + ", Path: " + icon.getImagePath()));
            }
        } catch (Exception e) {
            System.out.println("Error listing icons: " + e.getMessage());
        }
    }
}
