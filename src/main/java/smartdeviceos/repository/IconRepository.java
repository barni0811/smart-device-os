package smartdeviceos.repository;

import smartdeviceos.entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IconRepository extends JpaRepository<Icon, String> {
    
    Optional<Icon> findByName(String name);
    
    List<Icon> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT i FROM Icon i ORDER BY i.name")
    List<Icon> findAllOrderByName();
    
    @Query("SELECT i FROM Icon i WHERE i.name LIKE %:keyword% ORDER BY i.name")
    List<Icon> findByNameContainingOrderBy(@Param("keyword") String keyword);
    
    boolean existsByName(String name);
}
