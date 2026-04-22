package smartdeviceos.controller;

import smartdeviceos.entity.Menu;
import smartdeviceos.entity.MenuItem;
import smartdeviceos.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:5173")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable String id) {
        return menuService.findMenuById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<Menu>> getMenusByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(menuService.getMenusByDeviceId(deviceId));
    }
    
    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        try {
            Menu createdMenu = menuService.createMainMenu(menu.getDevice().getId(), menu.getName());
            return ResponseEntity.ok(createdMenu);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{menuId}/submenu")
    public ResponseEntity<Menu> createSubmenu(@PathVariable String menuId, @RequestBody Menu submenu) {
        try {
            Menu createdSubmenu = menuService.createSubmenu(menuId, submenu.getName());
            return ResponseEntity.ok(createdSubmenu);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String id) {
        try {
            menuService.deleteMenu(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Menu Item Management Endpoints

    @GetMapping("/{menuId}/items")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable String menuId) {
        return ResponseEntity.ok(menuService.getMenuItems(menuId));
    }

    @PostMapping("/{menuId}/items/app")
    public ResponseEntity<MenuItem> addApplicationToMenu(
            @PathVariable String menuId,
            @RequestBody Map<String, Object> request) {
        try {
            String appId = (String) request.get("appId");
            String name = (String) request.get("name");
            Integer position = (Integer) request.get("position");
            MenuItem item = menuService.addApplicationToMenu(menuId, appId, name, position);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{menuId}/items/file")
    public ResponseEntity<MenuItem> addFileToMenu(
            @PathVariable String menuId,
            @RequestBody Map<String, Object> request) {
        try {
            String fileName = (String) request.get("fileName");
            Integer position = (Integer) request.get("position");
            MenuItem item = menuService.addFileToMenu(menuId, fileName, position);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{menuId}/items/submenu")
    public ResponseEntity<MenuItem> addSubmenuToMenu(
            @PathVariable String menuId,
            @RequestBody Map<String, Object> request) {
        try {
            String submenuName = (String) request.get("submenuName");
            Integer position = (Integer) request.get("position");
            Menu submenu = menuService.createSubmenu(menuId, submenuName);
            MenuItem item = menuService.addSubmenuToMenu(menuId, submenu.getId(), submenuName, position);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeMenuItem(@PathVariable String itemId) {
        try {
            menuService.removeMenuItem(itemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/items/{itemId}/name")
    public ResponseEntity<MenuItem> renameMenuItem(
            @PathVariable String itemId,
            @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("name");
            MenuItem item = menuService.renameMenuItem(itemId, newName);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
