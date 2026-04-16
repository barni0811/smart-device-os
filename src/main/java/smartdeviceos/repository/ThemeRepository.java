package smartdeviceos.repository;

import smartdeviceos.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, String> {
    
    Optional<Theme> findByName(String name);
    
    List<Theme> findByNameContainingIgnoreCase(String name);
    
    List<Theme> findByIsDefault(Boolean isDefault);
    
    Optional<Theme> findByIsDefaultTrue();
    
    List<Theme> findByDeviceId(String deviceId);
    
    Optional<Theme> findByDeviceIdAndIsDefaultTrue(String deviceId);
    
    @Query("SELECT t FROM Theme t WHERE t.deviceId = :deviceId ORDER BY t.name")
    List<Theme> findByDeviceIdOrderByName(@Param("deviceId") String deviceId);
    
    @Query("SELECT t FROM Theme t ORDER BY t.name")
    List<Theme> findAllOrderByName();
    
    @Query("SELECT t FROM Theme t WHERE t.primaryColor = :color OR t.secondaryColor = :color")
    List<Theme> findByColor(@Param("color") String color);
    
    @Query("SELECT t FROM Theme t WHERE t.fontFamily = :fontFamily")
    List<Theme> findByFontFamily(@Param("fontFamily") String fontFamily);
    
    boolean existsByName(String name);
}
