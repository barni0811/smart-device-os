package smartdeviceos.family;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, FamilyMemberId> {
    List<FamilyMember> findByFamilyIdOrderByRole(String familyId);
    Optional<FamilyMember> findByFamilyIdAndUserId(String familyId, String userId);
    boolean existsByFamilyIdAndUserId(String familyId, String userId);
    List<FamilyMember> findByRole(String role);
    long countByFamilyId(String familyId);
}
