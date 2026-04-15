package smartdeviceos.repository;

import smartdeviceos.entity.Family;
import smartdeviceos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, String> {
    
    Optional<Family> findByName(String name);
    
    List<Family> findByNameContainingIgnoreCase(String name);
    
    List<Family> findByOwnerId(String ownerUserId);
    
    Optional<Family> findByOwner(User owner);
    
    @Query("SELECT f FROM Family f JOIN FETCH f.owner WHERE f.owner.id = :userId")
    List<Family> findFamiliesOwnedByUser(@Param("userId") String userId);
    
    @Query("SELECT f FROM Family f JOIN f.members fm WHERE fm.user.id = :userId")
    List<Family> findFamiliesByMember(@Param("userId") String userId);
    
    boolean existsByName(String name);
}
