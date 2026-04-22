package smartdeviceos.controller;

import smartdeviceos.entity.*;
import smartdeviceos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "http://localhost:5173")
public class SimulationController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private AppService appService;
    
    @PostMapping("/load-dummy-data")
    public ResponseEntity<String> loadDummyData() {
        try {
            // Create users first
            User user1 = userService.createUser("admin");
            User user2 = userService.createUser("user1");
            User user3 = userService.createUser("user2");
            User user4 = userService.createUser("user3");
            User user5 = userService.createUser("user4");
            
            // Create families with actual user IDs
            Family family1 = familyService.createFamily("Smith Family", user1.getId());
            Family family2 = familyService.createFamily("Johnson Family", user2.getId());
            Family family3 = familyService.createFamily("Williams Family", user3.getId());
            
            // Add family members
            familyService.addFamilyMember(family1.getId(), user2.getId(), "parent");
            familyService.addFamilyMember(family1.getId(), user3.getId(), "child");
            familyService.addFamilyMember(family1.getId(), user4.getId(), "child");
            
            familyService.addFamilyMember(family2.getId(), user3.getId(), "parent");
            familyService.addFamilyMember(family2.getId(), user5.getId(), "child");
            
            familyService.addFamilyMember(family3.getId(), user4.getId(), "parent");
            familyService.addFamilyMember(family3.getId(), user5.getId(), "child");
            
            // Create devices for each user
            User[] users = {user1, user2, user3, user4, user5};
            for (int i = 0; i < users.length; i++) {
                User user = users[i];
                for (int j = 1; j <= 3; j++) {
                    Device device = deviceService.createDevice(user.getId(), "Device-" + (i + 1) + "-" + j);
                    
                    // Create default wallpaper and theme
                    Wallpaper wallpaper = customizationService.addDefaultWallpaper(
                        device.getId(), "default_wallpaper", "images/default_wallpaper.png"
                    );
                    Theme theme = customizationService.addDefaultTheme(
                        device.getId(), "light_theme", "#FFFFFF", "#000000", "Arial"
                    );
                    
                    customizationService.selectWallpaper(device.getId(), wallpaper.getId());
                    customizationService.changeTheme(device.getId(), theme.getId());
                    
                    // Create menus
                    Menu menu1 = menuService.createMainMenu(device.getId(), "Main Menu");
                    Menu menu2 = menuService.createMainMenu(device.getId(), "Secondary Menu");
                    
                    // Create submenus
                    Menu submenu1 = menuService.createSubmenu(menu1.getId(), "Apps");
                    menuService.addSubmenuToMenu(menu1.getId(), submenu1.getId(), "Apps", 1);
                    
                    // Add default apps to menu
                    addDefaultAppsToMenu(menu1.getId());
                }
            }
            
            return ResponseEntity.ok("Dummy data loaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading dummy data: " + e.getMessage());
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
                // Skip if app not found or already in menu
            }
        }
    }
}
