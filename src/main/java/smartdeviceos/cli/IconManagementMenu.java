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
        System.out.print("Enter icon name to modify: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Icon name cannot be empty!");
            return;
        }
        
        try {
            var iconOpt = iconService.findIconByName(name);
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
            
            if (!newName.isEmpty()) {
                icon.setName(newName);
            }
            if (!newImagePath.isEmpty()) {
                icon.setImagePath(newImagePath);
            }
            
            var updatedIcon = iconService.updateIcon(icon.getId(), icon.getName(), icon.getImagePath());
            System.out.println("Icon updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating icon: " + e.getMessage());
        }
    }
    
    private void deleteIcon() {
        System.out.print("Enter icon name to delete: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Icon name cannot be empty!");
            return;
        }
        
        try {
            var iconOpt = iconService.findIconByName(name);
            if (iconOpt.isEmpty()) {
                System.out.println("Icon not found!");
                return;
            }
            
            iconService.deleteIcon(iconOpt.get().getId());
            System.out.println("Icon deleted successfully!");
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
