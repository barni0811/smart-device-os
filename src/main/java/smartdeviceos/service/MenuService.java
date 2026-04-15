package smartdeviceos.service;

import smartdeviceos.entity.Menu;
import smartdeviceos.entity.MenuItem;
import smartdeviceos.entity.Device;
import smartdeviceos.entity.App;
import smartdeviceos.entity.Icon;
import smartdeviceos.repository.MenuRepository;
import smartdeviceos.repository.MenuItemRepository;
import smartdeviceos.repository.DeviceRepository;
import smartdeviceos.repository.AppRepository;
import smartdeviceos.repository.IconRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MenuService {
    
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final DeviceRepository deviceRepository;
    private final AppRepository appRepository;
    private final IconRepository iconRepository;
    private final AppService appService;
    
    public MenuService(MenuRepository menuRepository, MenuItemRepository menuItemRepository,
                      DeviceRepository deviceRepository, AppRepository appRepository,
                      IconRepository iconRepository, AppService appService) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
        this.deviceRepository = deviceRepository;
        this.appRepository = appRepository;
        this.iconRepository = iconRepository;
        this.appService = appService;
    }
    
    public Menu createMainMenu(String deviceId, String menuName) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
        
        // Allow multiple menus per device - remove restriction
        
        Menu menu = new Menu();
        menu.setId(UUID.randomUUID().toString());
        menu.setName(menuName);
        menu.setDevice(deviceOpt.get());
        menu.setIsDefault(true);
        
        return menuRepository.save(menu);
    }
    
    public Menu createSubmenu(String parentMenuId, String submenuName) {
        Optional<Menu> parentMenuOpt = menuRepository.findById(parentMenuId);
        if (parentMenuOpt.isEmpty()) {
            throw new IllegalArgumentException("Parent menu not found with ID: " + parentMenuId);
        }
        
        Menu submenu = new Menu();
        submenu.setId(UUID.randomUUID().toString());
        submenu.setName(submenuName);
        submenu.setDevice(parentMenuOpt.get().getDevice());
        submenu.setIsDefault(false);
        
        return menuRepository.save(submenu);
    }
    
    public MenuItem addApplicationToMenu(String menuId, String appId, String itemName, int position) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        Optional<App> appOpt = appRepository.findById(appId);
        
        if (menuOpt.isEmpty()) {
            throw new IllegalArgumentException("Menu not found with ID: " + menuId);
        }
        if (appOpt.isEmpty()) {
            throw new IllegalArgumentException("App not found with ID: " + appId);
        }
        
        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID().toString());
        menuItem.setName(itemName);
        menuItem.setMenu(menuOpt.get());
        menuItem.setApp(appOpt.get());
        menuItem.setPosition(position);
        
        return menuItemRepository.save(menuItem);
    }
    
    public MenuItem addSubmenuToMenu(String parentMenuId, String submenuId, String itemName, int position) {
        Optional<Menu> parentMenuOpt = menuRepository.findById(parentMenuId);
        Optional<Menu> submenuOpt = menuRepository.findById(submenuId);
        
        if (parentMenuOpt.isEmpty()) {
            throw new IllegalArgumentException("Parent menu not found with ID: " + parentMenuId);
        }
        if (submenuOpt.isEmpty()) {
            throw new IllegalArgumentException("Submenu not found with ID: " + submenuId);
        }
        
        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID().toString());
        menuItem.setName(itemName);
        menuItem.setMenu(parentMenuOpt.get());
        menuItem.setSubmenu(submenuOpt.get());
        menuItem.setPosition(position);
        
        return menuItemRepository.save(menuItem);
    }
    
    public MenuItem addLeafItemToMenu(String menuId, String itemName, int position) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (menuOpt.isEmpty()) {
            throw new IllegalArgumentException("Menu not found with ID: " + menuId);
        }
        
        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID().toString());
        menuItem.setName(itemName);
        menuItem.setMenu(menuOpt.get());
        menuItem.setPosition(position);
        
        return menuItemRepository.save(menuItem);
    }
    
    public void removeMenuItem(String menuItemId) {
        menuItemRepository.deleteById(menuItemId);
    }
    
    public Optional<Menu> findMenuById(String menuId) {
        return menuRepository.findById(menuId);
    }
    
    public Optional<Menu> findMenuByDeviceId(String deviceId) {
        return menuRepository.findByDeviceId(deviceId);
    }
    
    public List<Menu> getMenusByDeviceId(String deviceId) {
        return menuRepository.findAllByDeviceId(deviceId);
    }
    
    public List<Menu> getMenusByDeviceIdWithDevice(String deviceId) {
        return menuRepository.findAllByDeviceIdWithDevice(deviceId);
    }
    
    public List<Menu> getMainMenusByDeviceId(String deviceId) {
        return menuRepository.findMainMenusByDeviceId(deviceId);
    }
    
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }
    
    public MenuItem addFileToMenu(String menuId, String fileName, int position) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        
        if (menuOpt.isEmpty()) {
            throw new IllegalArgumentException("Menu not found with ID: " + menuId);
        }
        
        MenuItem menuItem = new MenuItem();
        menuItem.setId(UUID.randomUUID().toString());
        menuItem.setName(fileName);
        menuItem.setMenu(menuOpt.get());
        menuItem.setPosition(position);
        
        return menuItemRepository.save(menuItem);
    }
    
    public List<MenuItem> getMenuItems(String menuId) {
        return menuItemRepository.findByMenuIdOrderByPosition(menuId);
    }
    
    public List<Menu> getMenusByUserId(String userId) {
        return menuRepository.findByUserId(userId);
    }
    
    public void createDefaultMenuWithApps(String deviceId) {
        // Create default menu
        Menu defaultMenu = createMainMenu(deviceId, "Default Family Menu");
        
        // Create family-friendly applications
        String[][] familyApps = {
            {"GPS Navigator", "Real-time GPS navigation and maps", "Navigation"},
            {"Family Cookbook", "Store and share family recipes", "Cooking"},
            {"Chess Master", "Play chess with family members", "Games"},
            {"Photo Gallery", "Store and organize family photos", "Media"},
            {"Weather Station", "Check weather forecasts", "Utilities"},
            {"Shopping List", "Shared family shopping lists", "Productivity"},
            {"Calendar", "Family events and appointments", "Productivity"},
            {"Music Player", "Play family music collection", "Entertainment"},
            {"Video Player", "Watch family videos", "Entertainment"},
            {"Notes", "Family notes and reminders", "Productivity"},
            {"Calculator", "Basic calculator for homework", "Utilities"},
            {"Camera", "Take family photos", "Media"},
            {"Messages", "Family chat and messaging", "Communication"},
            {"Emergency Contacts", "Important family contacts", "Utilities"},
            {"Fitness Tracker", "Track family fitness activities", "Health"},
            {"Recipe Timer", "Timer for cooking recipes", "Utilities"},
            {"Drawing Pad", "Digital drawing for kids", "Creative"},
            {"Story Books", "Digital story books for children", "Education"},
            {"Budget Tracker", "Family budget management", "Finance"},
            {"Games Center", "Collection of family games", "Games"}
        };
        
        // Create apps and add them to the menu
        for (int i = 0; i < familyApps.length; i++) {
            try {
                // Create app (using null for icon for now)
                App app = appService.createApp(familyApps[i][0], null);
                
                // Add app to menu with position
                addApplicationToMenu(defaultMenu.getId(), app.getId(), familyApps[i][0], i + 1);
            } catch (Exception e) {
                // Skip if app already exists or other error
                System.out.println("Note: App '" + familyApps[i][0] + "' may already exist or had an error: " + e.getMessage());
            }
        }
    }
}
