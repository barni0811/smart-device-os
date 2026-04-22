package smartdeviceos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartdeviceos.entity.Icon;
import smartdeviceos.service.IconService;

import java.util.List;

@RestController
@RequestMapping("/api/icons")
public class IconController {

    @Autowired
    private IconService iconService;

    @GetMapping
    public ResponseEntity<List<Icon>> getAllIcons() {
        return ResponseEntity.ok(iconService.getAllIcons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Icon> getIconById(@PathVariable String id) {
        return iconService.findIconById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Icon> getIconByName(@PathVariable String name) {
        return iconService.findIconByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Icon> createIcon(@RequestBody Icon icon) {
        try {
            Icon createdIcon = iconService.createIcon(icon.getName(), icon.getImagePath());
            return ResponseEntity.ok(createdIcon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Icon> updateIcon(@PathVariable String id, @RequestBody Icon icon) {
        try {
            Icon updatedIcon = iconService.updateIcon(id, icon.getName(), icon.getImagePath());
            return ResponseEntity.ok(updatedIcon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIcon(@PathVariable String id) {
        try {
            iconService.deleteIcon(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
