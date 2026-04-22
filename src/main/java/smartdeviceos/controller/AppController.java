package smartdeviceos.controller;

import smartdeviceos.entity.App;
import smartdeviceos.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apps")
@CrossOrigin(origins = "http://localhost:5173")
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping
    public ResponseEntity<List<App>> getAllApps() {
        return ResponseEntity.ok(appService.getAllActiveApps());
    }

    @GetMapping("/{id}")
    public ResponseEntity<App> getAppById(@PathVariable String id) {
        return appService.findAppById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<App> getAppByName(@PathVariable String name) {
        return appService.findAppByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<App> createApp(@RequestBody App app) {
        try {
            String iconId = app.getDefaultIcon() != null ? app.getDefaultIcon().getId() : null;
            App createdApp = appService.createApp(app.getName(), iconId);
            return ResponseEntity.ok(createdApp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable String id) {
        try {
            appService.deleteApp(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
