package smartdeviceos.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<App, String> {
    Optional<App> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT a FROM App a WHERE a.isActive = true ORDER BY a.name")
    List<App> findActiveAppsOrderByName();
}
