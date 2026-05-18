package smartdeviceos.family;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, String> {
    Optional<Family> findByName(String name);
    boolean existsByName(String name);
    List<Family> findByOwnerId(String ownerId);

    @Query("SELECT f FROM Family f JOIN FETCH f.members m WHERE m.user.id = ?1")
    List<Family> findFamiliesByMember(String userId);
}
