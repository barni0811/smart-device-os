package smartdeviceos.customization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WallpaperRepository extends JpaRepository<Wallpaper, String> {
    List<Wallpaper> findByDeviceIdOrderByName(String deviceId);
    Optional<Wallpaper> findByDeviceIdAndIsDefaultTrue(String deviceId);
    Optional<Wallpaper> findByName(String name);

    @Query("SELECT w FROM Wallpaper w ORDER BY w.name")
    List<Wallpaper> findAllOrderByName();

    @Query("SELECT w FROM Wallpaper w WHERE w.isDefault = true")
    List<Wallpaper> findDefaultWallpapers();
}
