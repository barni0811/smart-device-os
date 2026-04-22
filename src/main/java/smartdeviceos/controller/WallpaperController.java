package smartdeviceos.controller;

import smartdeviceos.entity.Wallpaper;
import smartdeviceos.service.CustomizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RestController
@RequestMapping("/api/wallpapers")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class WallpaperController {
    
    @Autowired
    private CustomizationService customizationService;
    
    @GetMapping
    public ResponseEntity<List<Wallpaper>> getAllWallpapers() {
        return ResponseEntity.ok(customizationService.getAllWallpapers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Wallpaper> getWallpaperById(@PathVariable String id) {
        return customizationService.findWallpaperById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<Wallpaper>> getWallpapersByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(customizationService.getWallpapersByDeviceId(deviceId));
    }
    
    @PostMapping
    public ResponseEntity<Wallpaper> createWallpaper(@RequestBody Wallpaper wallpaper) {
        try {
            Wallpaper createdWallpaper = customizationService.addWallpaper(
                wallpaper.getDeviceId(),
                wallpaper.getName(),
                wallpaper.getImagePath()
            );
            return ResponseEntity.ok(createdWallpaper);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallpaper(@PathVariable String id) {
        try {
            customizationService.deleteWallpaper(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wallpaper> updateWallpaper(@PathVariable String id, @RequestBody Wallpaper wallpaper) {
        try {
            System.out.println("Updating wallpaper: id=" + id + ", name=" + wallpaper.getName() + ", path=" + wallpaper.getImagePath());
            Wallpaper updatedWallpaper = customizationService.updateWallpaper(id, wallpaper.getName(), wallpaper.getImagePath());
            return ResponseEntity.ok(updatedWallpaper);
        } catch (Exception e) {
            System.err.println("Error updating wallpaper: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/select")
    public ResponseEntity<Void> selectWallpaper(@RequestBody SelectWallpaperRequest request) {
        try {
            customizationService.selectWallpaper(request.getDeviceId(), request.getWallpaperId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static class SelectWallpaperRequest {
        private String deviceId;
        private String wallpaperId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getWallpaperId() {
            return wallpaperId;
        }

        public void setWallpaperId(String wallpaperId) {
            this.wallpaperId = wallpaperId;
        }
    }
}
