package smartdeviceos.service;

import smartdeviceos.entity.Family;
import smartdeviceos.entity.FamilyMember;
import smartdeviceos.entity.User;
import smartdeviceos.repository.FamilyRepository;
import smartdeviceos.repository.FamilyMemberRepository;
import smartdeviceos.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FamilyService {
    
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final UserRepository userRepository;
    
    public FamilyService(FamilyRepository familyRepository, 
                        FamilyMemberRepository familyMemberRepository,
                        UserRepository userRepository) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
        this.userRepository = userRepository;
    }
    
    public Family createFamily(String familyName, String ownerUserId) {
        if (familyRepository.existsByName(familyName)) {
            throw new IllegalArgumentException("Family with name '" + familyName + "' already exists");
        }
        
        Optional<User> ownerOpt = userRepository.findById(ownerUserId);
        if (ownerOpt.isEmpty()) {
            throw new IllegalArgumentException("Owner user not found with ID: " + ownerUserId);
        }
        
        Family family = new Family();
        family.setId(UUID.randomUUID().toString());
        family.setName(familyName);
        family.setOwner(ownerOpt.get());
        
        Family savedFamily = familyRepository.save(family);
        
        addFamilyMember(savedFamily.getId(), ownerUserId, "parent");
        
        return savedFamily;
    }
    
    public FamilyMember addFamilyMember(String familyId, String userId, String role) {
        if (familyMemberRepository.existsByFamilyIdAndUserId(familyId, userId)) {
            throw new IllegalArgumentException("User is already a member of this family");
        }
        
        Optional<Family> familyOpt = familyRepository.findById(familyId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (familyOpt.isEmpty()) {
            throw new IllegalArgumentException("Family not found with ID: " + familyId);
        }
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFamily(familyOpt.get());
        familyMember.setUser(userOpt.get());
        familyMember.setRole(role);
        
        return familyMemberRepository.save(familyMember);
    }
    
    public void removeFamilyMember(String familyId, String userId) {
        Optional<FamilyMember> memberOpt = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Family member not found");
        }
        
        FamilyMember member = memberOpt.get();
        
        if (member.getFamily().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot remove family owner from the family");
        }
        
        familyMemberRepository.delete(member);
    }
    
    public FamilyMember updateMemberRole(String familyId, String userId, String newRole) {
        Optional<FamilyMember> memberOpt = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId);
        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("Family member not found");
        }
        
        FamilyMember member = memberOpt.get();
        member.setRole(newRole);
        
        return familyMemberRepository.save(member);
    }
    
    public void deleteFamily(String familyId) {
        if (!familyRepository.existsById(familyId)) {
            throw new IllegalArgumentException("Family not found with ID: " + familyId);
        }
        
        familyRepository.deleteById(familyId);
    }
    
    public Optional<Family> findFamilyById(String familyId) {
        return familyRepository.findById(familyId);
    }
    
    public Optional<Family> findFamilyByName(String name) {
        return familyRepository.findByName(name);
    }
    
    public List<Family> getFamiliesOwnedByUser(String userId) {
        return familyRepository.findByOwnerId(userId);
    }
    
    public List<Family> getFamiliesByMember(String userId) {
        return familyRepository.findFamiliesByMember(userId);
    }
    
    public List<FamilyMember> getFamilyMembers(String familyId) {
        return familyMemberRepository.findByFamilyIdOrderByRole(familyId);
    }
    
    public List<FamilyMember> getMembersByRole(String role) {
        return familyMemberRepository.findByRole(role);
    }
    
    public long getFamilyMemberCount(String familyId) {
        return familyMemberRepository.countByFamilyId(familyId);
    }
}
