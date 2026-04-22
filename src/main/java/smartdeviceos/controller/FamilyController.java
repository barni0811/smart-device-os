package smartdeviceos.controller;

import smartdeviceos.entity.Family;
import smartdeviceos.entity.FamilyMember;
import smartdeviceos.entity.User;
import smartdeviceos.service.FamilyService;
import smartdeviceos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

class FamilyMemberRequest {
    private String userId;
    private String role;
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

class RoleUpdateRequest {
    private String role;
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

@RestController
@RequestMapping("/api/families")
@CrossOrigin(origins = "http://localhost:5173")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<Family>> getAllFamilies() {
        return ResponseEntity.ok(familyService.getAllFamilies());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Family> getFamilyById(@PathVariable String id) {
        return familyService.findFamilyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Family> getFamilyByName(@PathVariable String name) {
        return familyService.findFamilyByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Family> createFamily(@RequestBody Family family) {
        try {
            // Use the owner's ID from the family object if available, otherwise get first user
            String ownerUserId;
            if (family.getOwner() != null && family.getOwner().getId() != null) {
                ownerUserId = family.getOwner().getId();
            } else {
                // Get the first user from the database as the default owner
                List<User> users = userService.getAllUsers();
                if (users.isEmpty()) {
                    // Create a default user if none exists
                    User defaultUser = userService.createUser("default_user");
                    ownerUserId = defaultUser.getId();
                } else {
                    ownerUserId = users.get(0).getId();
                }
            }
            Family createdFamily = familyService.createFamily(family.getName(), ownerUserId);
            return ResponseEntity.ok(createdFamily);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Family> updateFamily(@PathVariable String id, @RequestBody Family family) {
        try {
            Family updatedFamily = familyService.updateFamily(id, family.getName());
            return ResponseEntity.ok(updatedFamily);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable String id) {
        try {
            familyService.deleteFamily(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<FamilyMember>> getFamilyMembers(@PathVariable String id) {
        try {
            return ResponseEntity.ok(familyService.getFamilyMembers(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<FamilyMember> addFamilyMember(
            @PathVariable String id,
            @RequestBody FamilyMemberRequest request) {
        try {
            FamilyMember member = familyService.addFamilyMember(id, request.getUserId(), request.getRole());
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeFamilyMember(@PathVariable String id, @PathVariable String userId) {
        try {
            familyService.removeFamilyMember(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/members/{userId}/role")
    public ResponseEntity<FamilyMember> updateMemberRole(
            @PathVariable String id,
            @PathVariable String userId,
            @RequestBody RoleUpdateRequest request) {
        try {
            FamilyMember member = familyService.updateMemberRole(id, userId, request.getRole());
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
