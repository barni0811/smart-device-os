package smartdeviceos.controller;

import smartdeviceos.entity.Theme;
import smartdeviceos.service.CustomizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ThemeController {
    
    @Autowired
    private CustomizationService customizationService;
    
    @GetMapping
    public ResponseEntity<List<Theme>> getAllThemes() {
        return ResponseEntity.ok(customizationService.getAllThemes());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable String id) {
        return customizationService.findThemeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<Theme>> getThemesByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(customizationService.getThemesByDeviceId(deviceId));
    }
    
    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        try {
            Theme createdTheme = customizationService.addTheme(
                theme.getDeviceId(),
                theme.getName(),
                theme.getPrimaryColor(),
                theme.getSecondaryColor(),
                theme.getFontFamily()
            );
            return ResponseEntity.ok(createdTheme);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable String id) {
        try {
            customizationService.deleteTheme(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable String id, @RequestBody Theme theme) {
        try {
            System.out.println("Updating theme: id=" + id + ", name=" + theme.getName());
            Theme updatedTheme = customizationService.updateTheme(
                id,
                theme.getName(),
                theme.getPrimaryColor(),
                theme.getSecondaryColor(),
                theme.getFontFamily()
            );
            return ResponseEntity.ok(updatedTheme);
        } catch (Exception e) {
            System.err.println("Error updating theme: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/select")
    public ResponseEntity<Void> selectTheme(@RequestBody SelectThemeRequest request) {
        try {
            customizationService.changeTheme(request.getDeviceId(), request.getThemeId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static class SelectThemeRequest {
        private String deviceId;
        private String themeId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getThemeId() {
            return themeId;
        }

        public void setThemeId(String themeId) {
            this.themeId = themeId;
        }
    }
}
