package smartdeviceos.repository;

import smartdeviceos.entity.FamilyMember;
import smartdeviceos.entity.FamilyMemberId;
import smartdeviceos.entity.Family;
import smartdeviceos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, FamilyMemberId> {
    
    List<FamilyMember> findByFamilyId(String familyId);
    
    List<FamilyMember> findByFamily(Family family);
    
    List<FamilyMember> findByUserId(String userId);
    
    List<FamilyMember> findByUser(User user);
    
    Optional<FamilyMember> findByFamilyIdAndUserId(String familyId, String userId);
    
    List<FamilyMember> findByRole(String role);
    
    List<FamilyMember> findByFamilyIdAndRole(String familyId, String role);
    
    @Query("SELECT fm FROM FamilyMember fm JOIN FETCH fm.user WHERE fm.family.id = :familyId ORDER BY fm.role, fm.joinedAt")
    List<FamilyMember> findByFamilyIdOrderByRole(@Param("familyId") String familyId);
    
    @Query("SELECT COUNT(fm) FROM FamilyMember fm WHERE fm.family.id = :familyId")
    long countByFamilyId(@Param("familyId") String familyId);
    
    boolean existsByFamilyIdAndUserId(String familyId, String userId);
}
