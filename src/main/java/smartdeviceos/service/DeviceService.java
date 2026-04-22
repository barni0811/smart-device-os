package smartdeviceos.service;

import smartdeviceos.entity.Device;
import smartdeviceos.entity.User;
import smartdeviceos.repository.UserRepository;
import smartdeviceos.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final CustomizationService customizationService;

    public DeviceService(DeviceRepository deviceRepository,
                        UserRepository userRepository,
                        CustomizationService customizationService) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.customizationService = customizationService;
    }
    
    public Device createDevice(String userId, String deviceName) {
        Device device = new Device();
        device.setId(UUID.randomUUID().toString());
        device.setName(deviceName);
        device.setIsDefaultMenu(false);

        // Find and set the user
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(device::setUser);

        // Save the device first
        Device savedDevice = deviceRepository.save(device);

        // Create default wallpaper and theme for the device only if none exist
        var wallpapers = customizationService.getWallpapersByDeviceId(savedDevice.getId());
        boolean hasDefaultWallpaper = wallpapers.stream().anyMatch(w -> w.getName().equals("default_wallpaper"));
        if (!hasDefaultWallpaper) {
            var defaultWallpaper = customizationService.addDefaultWallpaper(savedDevice.getId(), "default_wallpaper", "images/default_wallpaper.png");
            customizationService.selectWallpaper(savedDevice.getId(), defaultWallpaper.getId());
        } else if (savedDevice.getWallpaper() == null) {
            // Select the first wallpaper if device has no wallpaper set
            var firstWallpaper = wallpapers.get(0);
            customizationService.selectWallpaper(savedDevice.getId(), firstWallpaper.getId());
        }

        var themes = customizationService.getThemesByDeviceId(savedDevice.getId());
        boolean hasDefaultTheme = themes.stream().anyMatch(t -> t.getName().equals("light_theme"));
        if (!hasDefaultTheme) {
            var defaultTheme = customizationService.addDefaultTheme(savedDevice.getId(), "light_theme", "#FFFFFF", "#000000", "Arial");
            customizationService.changeTheme(savedDevice.getId(), defaultTheme.getId());
        } else if (savedDevice.getTheme() == null) {
            // Select the first theme if device has no theme set
            var firstTheme = themes.get(0);
            customizationService.changeTheme(savedDevice.getId(), firstTheme.getId());
        }

        return savedDevice;
    }
    
    public Device updateDevice(String deviceId, String newName) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
        
        Device device = deviceOpt.get();
        device.setName(newName);
        
        return deviceRepository.save(device);
    }
    
    public void deleteDevice(String deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
        
        deviceRepository.deleteById(deviceId);
    }
    
    public void setAsDefaultMenu(String deviceId) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
    }
    
    public Optional<Device> findDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }
    
    public Optional<Device> findDeviceByNameAndUserId(String name, String userId) {
        return deviceRepository.findByNameAndUserId(name, userId);
    }
    
    public List<Device> getDevicesByUserId(String userId) {
        return deviceRepository.findByUser_Id(userId);
    }
    
    public Optional<Device> getDefaultDeviceByUserId(String userId) {
        return deviceRepository.findDefaultDeviceByUserId(userId);
    }
    
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    
    public List<Device> getAllDevicesWithUsers() {
        return deviceRepository.findAllWithUsers();
    }
}
