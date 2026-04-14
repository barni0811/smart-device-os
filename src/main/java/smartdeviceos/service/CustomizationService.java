package smartdeviceos.service;

import smartdeviceos.entity.Wallpaper;
import smartdeviceos.entity.Theme;
import smartdeviceos.entity.Device;
import smartdeviceos.repository.WallpaperRepository;
import smartdeviceos.repository.ThemeRepository;
import smartdeviceos.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CustomizationService {
    
    private final WallpaperRepository wallpaperRepository;
    private final ThemeRepository themeRepository;
    private final DeviceRepository deviceRepository;
    
    public CustomizationService(WallpaperRepository wallpaperRepository, 
                               ThemeRepository themeRepository,
                               DeviceRepository deviceRepository) {
        this.wallpaperRepository = wallpaperRepository;
        this.themeRepository = themeRepository;
        this.deviceRepository = deviceRepository;
    }
    
    // Wallpaper Management
    public Wallpaper addWallpaper(String name, String imagePath) {
        if (wallpaperRepository.existsByName(name)) {
            throw new IllegalArgumentException("Wallpaper with name '" + name + "' already exists");
        }
        
        Wallpaper wallpaper = new Wallpaper();
        wallpaper.setId(UUID.randomUUID().toString());
        wallpaper.setName(name);
        wallpaper.setImagePath(imagePath);
        wallpaper.setIsDefault(false);
        
        return wallpaperRepository.save(wallpaper);
    }
    
    public void selectWallpaper(String deviceId, String wallpaperId) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        Optional<Wallpaper> wallpaperOpt = wallpaperRepository.findById(wallpaperId);
        
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
        if (wallpaperOpt.isEmpty()) {
            throw new IllegalArgumentException("Wallpaper not found with ID: " + wallpaperId);
        }
        
        Device device = deviceOpt.get();
        device.setWallpaper(wallpaperOpt.get());
        deviceRepository.save(device);
    }
    
    public void deleteWallpaper(String wallpaperId) {
        if (!wallpaperRepository.existsById(wallpaperId)) {
            throw new IllegalArgumentException("Wallpaper not found with ID: " + wallpaperId);
        }
        
        wallpaperRepository.deleteById(wallpaperId);
    }
    
    // Theme Management
    public Theme addTheme(String name, String primaryColor, String secondaryColor, String fontFamily) {
        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("Theme with name '" + name + "' already exists");
        }
        
        Theme theme = new Theme();
        theme.setId(UUID.randomUUID().toString());
        theme.setName(name);
        theme.setPrimaryColor(primaryColor);
        theme.setSecondaryColor(secondaryColor);
        theme.setFontFamily(fontFamily);
        theme.setIsDefault(false);
        
        return themeRepository.save(theme);
    }
    
    public void changeTheme(String deviceId, String themeId) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        Optional<Theme> themeOpt = themeRepository.findById(themeId);
        
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Device not found with ID: " + deviceId);
        }
        if (themeOpt.isEmpty()) {
            throw new IllegalArgumentException("Theme not found with ID: " + themeId);
        }
        
        Device device = deviceOpt.get();
        device.setTheme(themeOpt.get());
        deviceRepository.save(device);
    }
    
    public void deleteTheme(String themeId) {
        if (!themeRepository.existsById(themeId)) {
            throw new IllegalArgumentException("Theme not found with ID: " + themeId);
        }
        
        themeRepository.deleteById(themeId);
    }
    
    // Query Methods
    public Optional<Wallpaper> findWallpaperById(String wallpaperId) {
        return wallpaperRepository.findById(wallpaperId);
    }
    
    public Optional<Wallpaper> findWallpaperByName(String name) {
        return wallpaperRepository.findByName(name);
    }
    
    public List<Wallpaper> getAllWallpapers() {
        return wallpaperRepository.findAllOrderByName();
    }
    
    public List<Wallpaper> getDefaultWallpapers() {
        return wallpaperRepository.findDefaultWallpapers();
    }
    
    public Optional<Theme> findThemeById(String themeId) {
        return themeRepository.findById(themeId);
    }
    
    public Optional<Theme> findThemeByName(String name) {
        return themeRepository.findByName(name);
    }
    
    public List<Theme> getAllThemes() {
        return themeRepository.findAllOrderByName();
    }
    
    public List<Theme> getThemesByColor(String color) {
        return themeRepository.findByColor(color);
    }
    
    public List<Theme> getThemesByFontFamily(String fontFamily) {
        return themeRepository.findByFontFamily(fontFamily);
    }
    
    public List<Theme> getDefaultThemes() {
        return themeRepository.findByIsDefault(true);
    }
}
