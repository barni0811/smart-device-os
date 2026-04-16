package smartdeviceos.repository;

import smartdeviceos.entity.Wallpaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WallpaperRepository extends JpaRepository<Wallpaper, String> {
    
    Optional<Wallpaper> findByName(String name);
    
    List<Wallpaper> findByNameContainingIgnoreCase(String name);
    
    List<Wallpaper> findByIsDefault(Boolean isDefault);
    
    Optional<Wallpaper> findByIsDefaultTrue();
    
    List<Wallpaper> findByDeviceId(String deviceId);
    
    Optional<Wallpaper> findByDeviceIdAndIsDefaultTrue(String deviceId);
    
    @Query("SELECT w FROM Wallpaper w WHERE w.deviceId = :deviceId ORDER BY w.name")
    List<Wallpaper> findByDeviceIdOrderByName(@Param("deviceId") String deviceId);
    
    @Query("SELECT w FROM Wallpaper w ORDER BY w.name")
    List<Wallpaper> findAllOrderByName();
    
    @Query("SELECT w FROM Wallpaper w WHERE w.isDefault = true")
    List<Wallpaper> findDefaultWallpapers();
    
    boolean existsByName(String name);
}
