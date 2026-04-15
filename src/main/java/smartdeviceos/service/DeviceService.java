package smartdeviceos.service;

import smartdeviceos.entity.Device;
import smartdeviceos.entity.User;
import smartdeviceos.entity.Wallpaper;
import smartdeviceos.entity.Theme;
import smartdeviceos.repository.UserRepository;
import smartdeviceos.repository.DeviceRepository;
import smartdeviceos.repository.WallpaperRepository;
import smartdeviceos.repository.ThemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DeviceService {
    
    private final DeviceRepository deviceRepository;
    private final WallpaperRepository wallpaperRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    
    public DeviceService(DeviceRepository deviceRepository, 
                        WallpaperRepository wallpaperRepository,
                        ThemeRepository themeRepository,
                        UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.wallpaperRepository = wallpaperRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
    }
    
    public Device createDevice(String userId, String deviceName) {
        Device device = new Device();
        device.setId(UUID.randomUUID().toString());
        device.setName(deviceName);
        device.setIsDefaultMenu(false);
        
        // Find and set the user
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(device::setUser);
        
        wallpaperRepository.findByIsDefaultTrue().ifPresent(device::setWallpaper);
        themeRepository.findByIsDefaultTrue().ifPresent(device::setTheme);
        
        return deviceRepository.save(device);
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
