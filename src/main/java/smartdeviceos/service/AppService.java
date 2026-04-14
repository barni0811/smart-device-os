package smartdeviceos.service;

import smartdeviceos.entity.App;
import smartdeviceos.entity.Icon;
import smartdeviceos.entity.UserApp;
import smartdeviceos.entity.DeviceApp;
import smartdeviceos.repository.AppRepository;
import smartdeviceos.repository.IconRepository;
import smartdeviceos.repository.UserAppRepository;
import smartdeviceos.repository.DeviceAppRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AppService {
    
    private final AppRepository appRepository;
    private final IconRepository iconRepository;
    private final UserAppRepository userAppRepository;
    private final DeviceAppRepository deviceAppRepository;
    
    public AppService(AppRepository appRepository, IconRepository iconRepository,
                     UserAppRepository userAppRepository, DeviceAppRepository deviceAppRepository) {
        this.appRepository = appRepository;
        this.iconRepository = iconRepository;
        this.userAppRepository = userAppRepository;
        this.deviceAppRepository = deviceAppRepository;
    }
    
    public App createApp(String name, String description, String category, String defaultIconId) {
        if (appRepository.existsByName(name)) {
            throw new IllegalArgumentException("App with name '" + name + "' already exists");
        }
        
        App app = new App();
        app.setId(UUID.randomUUID().toString());
        app.setName(name);
        app.setDescription(description);
        app.setCategory(category);
        app.setIsActive(true);
        
        if (defaultIconId != null) {
            Optional<Icon> iconOpt = iconRepository.findById(defaultIconId);
            iconOpt.ifPresent(app::setDefaultIcon);
        }
        
        return appRepository.save(app);
    }
    
    public App updateApp(String appId, String newName, String newDescription, String newCategory) {
        Optional<App> appOpt = appRepository.findById(appId);
        if (appOpt.isEmpty()) {
            throw new IllegalArgumentException("App not found with ID: " + appId);
        }
        
        App app = appOpt.get();
        
        if (!app.getName().equals(newName) && appRepository.existsByName(newName)) {
            throw new IllegalArgumentException("App with name '" + newName + "' already exists");
        }
        
        app.setName(newName);
        app.setDescription(newDescription);
        app.setCategory(newCategory);
        
        return appRepository.save(app);
    }
    
    public void deleteApp(String appId) {
        Optional<App> appOpt = appRepository.findById(appId);
        if (appOpt.isEmpty()) {
            throw new IllegalArgumentException("App not found with ID: " + appId);
        }
        
        App app = appOpt.get();
        app.setIsActive(false);
        appRepository.save(app);
    }
    
    public void launchApp(String userId, String deviceId, String appId) {
        Optional<DeviceApp> deviceAppOpt = deviceAppRepository.findByDeviceIdAndAppId(deviceId, appId);
        
        if (deviceAppOpt.isPresent()) {
            DeviceApp deviceApp = deviceAppOpt.get();
            deviceApp.setLastUsed(LocalDateTime.now());
            deviceApp.setUsageCount(deviceApp.getUsageCount() + 1);
            deviceAppRepository.save(deviceApp);
        } else {
            installAppOnDevice(deviceId, appId);
            launchApp(userId, deviceId, appId);
        }
    }
    
    public void installAppForUser(String userId, String appId) {
        if (userAppRepository.existsByUserIdAndAppId(userId, appId)) {
            throw new IllegalArgumentException("App already installed for user");
        }
        
        Optional<App> appOpt = appRepository.findById(appId);
        if (appOpt.isEmpty()) {
            throw new IllegalArgumentException("App not found with ID: " + appId);
        }
    }
    
    public void installAppOnDevice(String deviceId, String appId) {
        if (deviceAppRepository.existsByDeviceIdAndAppId(deviceId, appId)) {
            throw new IllegalArgumentException("App already installed on device");
        }
        
        Optional<App> appOpt = appRepository.findById(appId);
        if (appOpt.isEmpty()) {
            throw new IllegalArgumentException("App not found with ID: " + appId);
        }
    }
    
    public Optional<App> findAppById(String appId) {
        return appRepository.findById(appId);
    }
    
    public Optional<App> findAppByName(String name) {
        return appRepository.findByName(name);
    }
    
    public List<App> getAllActiveApps() {
        return appRepository.findActiveAppsOrderByName();
    }
    
    public List<App> getAppsByCategory(String category) {
        return appRepository.findByCategory(category);
    }
    
    public List<String> getActiveCategories() {
        return appRepository.findActiveCategories();
    }
    
    public List<DeviceApp> getRecentlyUsedApps(String deviceId) {
        return deviceAppRepository.findRecentlyUsedAppsByDeviceId(deviceId);
    }
}
