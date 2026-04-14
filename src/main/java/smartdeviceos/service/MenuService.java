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
    
    public MenuService(MenuRepository menuRepository, MenuItemRepository menuItemRepository,
                      DeviceRepository deviceRepository, AppRepository appRepository,
                      IconRepository iconRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
        this.deviceRepository = deviceRepository;
        this.appRepository = appRepository;
        this.iconRepository = iconRepository;
    }
    
    public Menu createMainMenu(String deviceId, String menuName) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
        
        if (menuRepository.existsByDeviceId(deviceId)) {
            throw new IllegalArgumentException("Device already has a menu");
        }
        
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
    
    public List<MenuItem> getMenuItems(String menuId) {
        return menuItemRepository.findByMenuIdOrderByPosition(menuId);
    }
    
    public List<Menu> getMenusByUserId(String userId) {
        return menuRepository.findByUserId(userId);
    }
}
