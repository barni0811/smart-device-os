package smartdeviceos.cli;

import smartdeviceos.entity.User;
import smartdeviceos.service.FamilyService;
import smartdeviceos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class FamilyManagementMenu {
    
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private UserService userService;
    
    private final Scanner scanner = new Scanner(System.in);
    private String currentUserId;
    
    public void setUserId(String userId) {
        this.currentUserId = userId;
    }
    
    public void showMenu() {
        while (true) {
            System.out.println("\n=== Family Management ===");
            System.out.println("1. Create Family");
            System.out.println("2. Add Family Member");
            System.out.println("3. List Families");
            System.out.println("4. Remove Family Member");
            System.out.println("5. Remove Family");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option (1-6): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> createFamily();
                    case 2 -> addFamilyMember();
                    case 3 -> listFamiliesWithMembers();
                    case 4 -> removeFamilyMember();
                    case 5 -> removeFamily();
                    case 6 -> { return; }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void createFamily() {
        System.out.print("Enter family name: ");
        String familyName = scanner.nextLine().trim();
        
        if (familyName.isEmpty()) {
            System.out.println("Family name cannot be empty!");
            return;
        }
        
        try {
            var family = familyService.createFamily(familyName, currentUserId);
            System.out.println("Family created successfully: " + family.getName());
        } catch (Exception e) {
            System.out.println("Error creating family: " + e.getMessage());
        }
    }
    
    private void addFamilyMember() {
        try {
            var families = familyService.getFamiliesOwnedByUser(currentUserId);
            if (families.isEmpty()) {
                System.out.println("No families found. Please create a family first.");
                return;
            }
            
            System.out.println("\n=== Select Family ===");
            for (int i = 0; i < families.size(); i++) {
                var family = families.get(i);
                System.out.println((i + 1) + ". " + family.getName() + " (Owner: " + family.getOwner().getName() + ")");
            }
            
            System.out.print("Select family (1-" + families.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= families.size()) {
                    var selectedFamily = families.get(choice - 1);
                    
                    User selectedUser = searchAndSelectUser();
                    if (selectedUser == null) {
                        return;
                    }
                    
                    String role = selectRole();
                    
                    var member = familyService.addFamilyMember(selectedFamily.getId(), selectedUser.getId(), role);
                    System.out.println("Family member added successfully: " + selectedUser.getName() + " as " + role);
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error adding family member: " + e.getMessage());
        }
    }
    
    private void listFamilies() {
        try {
            var families = familyService.getFamiliesOwnedByUser(currentUserId);
            System.out.println("\n=== Your Families ===");
            if (families.isEmpty()) {
                System.out.println("No families found.");
            } else {
                families.forEach(family -> {
                    System.out.println("Name: " + family.getName() + ", Owner: " + family.getOwner().getName());
                    
                    // Show family members for each family
                    var members = familyService.getFamilyMembers(family.getId());
                    if (members.isEmpty()) {
                        System.out.println("  Members: None");
                    } else {
                        System.out.println("  Members:");
                        members.forEach(member -> 
                            System.out.println("    - " + member.getUser().getName() + " (Role: " + member.getRole() + ")")
                        );
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error listing families: " + e.getMessage());
        }
    }
    
    private void listFamiliesWithMembers() {
        try {
            var families = familyService.getFamiliesOwnedByUser(currentUserId);
            System.out.println("\n=== Your Families with Members ===");
            if (families.isEmpty()) {
                System.out.println("No families found.");
            } else {
                families.forEach(family -> {
                    System.out.println("Family: " + family.getName() + " (Owner: " + family.getOwner().getName() + ")");
                    
                    var members = familyService.getFamilyMembers(family.getId());
                    if (members.isEmpty()) {
                        System.out.println("  Members: None");
                    } else {
                        System.out.println("  Members:");
                        members.forEach(member -> 
                            System.out.println("    - " + member.getUser().getName() + " (Role: " + member.getRole() + ")")
                        );
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error listing families with members: " + e.getMessage());
        }
    }
    
    private void listFamilyMembers() {
        try {
            var families = familyService.getFamiliesOwnedByUser(currentUserId);
            if (families.isEmpty()) {
                System.out.println("No families found. Please create a family first.");
                return;
            }
            
            System.out.println("\n=== Select Family ===");
            for (int i = 0; i < families.size(); i++) {
                var family = families.get(i);
                System.out.println((i + 1) + ". " + family.getName() + " (Owner: " + family.getOwner().getName() + ")");
            }
            
            System.out.print("Select family (1-" + families.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= families.size()) {
                    var selectedFamily = families.get(choice - 1);
                    
                    var members = familyService.getFamilyMembers(selectedFamily.getId());
                    System.out.println("\n=== Family Members of " + selectedFamily.getName() + " ===");
                    if (members.isEmpty()) {
                        System.out.println("No members found.");
                    } else {
                        members.forEach(member -> System.out.println("Name: " + member.getUser().getName() + ", Role: " + member.getRole()));
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error listing family members: " + e.getMessage());
        }
    }
    
    private User searchAndSelectUser() {
        System.out.print("Enter username (or part of name): ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty!");
            return null;
        }
        
        try {
            var users = userService.searchUsersByName(searchTerm);
            if (users.isEmpty()) {
                System.out.println("No users found matching: " + searchTerm);
                return null;
            }
            
            if (users.size() == 1) {
                return users.get(0);
            }
            
            System.out.println("Found users:");
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
            System.out.println("Error searching users: " + e.getMessage());
            return null;
        }
    }
    
    private void removeFamilyMember() {
        try {
            var families = familyService.getFamiliesOwnedByUser(currentUserId);
            if (families.isEmpty()) {
                System.out.println("No families found. Please create a family first.");
                return;
            }
            
            System.out.println("\n=== Select Family ===");
            for (int i = 0; i < families.size(); i++) {
                var family = families.get(i);
                System.out.println((i + 1) + ". " + family.getName() + " (Owner: " + family.getOwner().getName() + ")");
            }
            
            System.out.print("Select family (1-" + families.size() + "): ");
            try {
                int familyChoice = Integer.parseInt(scanner.nextLine());
                if (familyChoice >= 1 && familyChoice <= families.size()) {
                    var selectedFamily = families.get(familyChoice - 1);
                    var members = familyService.getFamilyMembers(selectedFamily.getId());
                    
                    if (members.isEmpty()) {
                        System.out.println("No members found in this family.");
                        return;
                    }
                    
                    System.out.println("\n=== Family Members ===");
                    for (int i = 0; i < members.size(); i++) {
                        var member = members.get(i);
                        System.out.println((i + 1) + ". " + member.getUser().getName() + " (Role: " + member.getRole() + ")");
                    }
                    
                    System.out.print("Select member to remove (1-" + members.size() + "): ");
                    try {
                        int memberChoice = Integer.parseInt(scanner.nextLine());
                        if (memberChoice >= 1 && memberChoice <= members.size()) {
                            var selectedMember = members.get(memberChoice - 1);
                            
                            // Check if trying to remove the family owner
                            if (selectedMember.getUser().getId().equals(selectedFamily.getOwner().getId())) {
                                System.out.println("Cannot remove the family owner from the family!");
                                return;
                            }
                            
                            familyService.removeFamilyMember(selectedFamily.getId(), selectedMember.getUser().getId());
                            System.out.println("Family member removed successfully: " + selectedMember.getUser().getName());
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error removing family member: " + e.getMessage());
        }
    }
    
    private String selectRole() {
        while (true) {
            System.out.println("\n=== Select Role ===");
            System.out.println("1. Parent");
            System.out.println("2. Child");
            System.out.print("Select role (1-2): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> { return "parent"; }
                    case 2 -> { return "child"; }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private void removeFamily() {
        try {
            var families = familyService.getFamiliesOwnedByUser(currentUserId);
            if (families.isEmpty()) {
                System.out.println("No families found to remove.");
                return;
            }
            
            System.out.println("\n=== Select Family to Remove ===");
            for (int i = 0; i < families.size(); i++) {
                var family = families.get(i);
                System.out.println((i + 1) + ". " + family.getName() + " (Owner: " + family.getOwner().getName() + ")");
            }
            
            System.out.print("Select family to remove (1-" + families.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= families.size()) {
                    var selectedFamily = families.get(choice - 1);
                    
                    // Show confirmation with member count
                    var members = familyService.getFamilyMembers(selectedFamily.getId());
                    System.out.println("\nFamily to remove: " + selectedFamily.getName());
                    System.out.println("Members: " + members.size());
                    System.out.println("WARNING: This will permanently delete the family and remove all members!");
                    
                    System.out.print("Are you sure you want to remove this family? (yes/no): ");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    
                    if (confirmation.equals("yes")) {
                        familyService.deleteFamily(selectedFamily.getId());
                        System.out.println("Family removed successfully: " + selectedFamily.getName());
                    } else {
                        System.out.println("Family removal cancelled.");
                    }
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        } catch (Exception e) {
            System.out.println("Error removing family: " + e.getMessage());
        }
    }
}
