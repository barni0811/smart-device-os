package smartdeviceos.customization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, String> {
    List<Theme> findByDeviceIdOrderByName(String deviceId);
    Optional<Theme> findByDeviceIdAndIsDefaultTrue(String deviceId);
    Optional<Theme> findByName(String name);

    @Query("SELECT t FROM Theme t ORDER BY t.name")
    List<Theme> findAllOrderByName();

    @Query("SELECT t FROM Theme t WHERE t.primaryColor = ?1 OR t.secondaryColor = ?1")
    List<Theme> findByColor(String color);

    List<Theme> findByFontFamily(String fontFamily);
    List<Theme> findByIsDefault(boolean isDefault);
}
