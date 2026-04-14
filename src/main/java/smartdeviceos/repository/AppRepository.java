package smartdeviceos.repository;

import smartdeviceos.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<App, String> {
    
    Optional<App> findByName(String name);
    
    List<App> findByNameContainingIgnoreCase(String name);
    
    List<App> findByCategory(String category);
    
    List<App> findByIsActive(Boolean isActive);
    
    @Query("SELECT a FROM App a WHERE a.isActive = true ORDER BY a.name")
    List<App> findActiveAppsOrderByName();
    
    @Query("SELECT DISTINCT a.category FROM App a WHERE a.isActive = true")
    List<String> findActiveCategories();
    
    boolean existsByName(String name);
}
