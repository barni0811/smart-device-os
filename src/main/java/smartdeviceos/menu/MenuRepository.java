package smartdeviceos.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
    Optional<Menu> findByDeviceId(String deviceId);
    List<Menu> findAllByDeviceId(String deviceId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.device WHERE m.device.id = ?1")
    List<Menu> findAllByDeviceIdWithDevice(String deviceId);

    @Query("SELECT m FROM Menu m WHERE m.device.id = ?1 AND m.isDefault = true")
    List<Menu> findMainMenusByDeviceId(String deviceId);

    @Query("SELECT m FROM Menu m WHERE m.device.user.id = ?1")
    List<Menu> findByUserId(String userId);
}
