package smartdeviceos.repository;

import smartdeviceos.entity.Menu;
import smartdeviceos.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {
    
    Optional<Menu> findByDeviceId(String deviceId);
    
    Optional<Menu> findByDevice(Device device);
    
    List<Menu> findAllByDeviceId(String deviceId);
    
    List<Menu> findByIsDefault(Boolean isDefault);
    
    @Query("SELECT m FROM Menu m WHERE m.device.id = :deviceId AND m.isDefault = true")
    Optional<Menu> findDefaultMenuByDeviceId(@Param("deviceId") String deviceId);
    
    @Query("SELECT m FROM Menu m WHERE m.device.user.id = :userId")
    List<Menu> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.device WHERE m.device.id = :deviceId")
    List<Menu> findAllByDeviceIdWithDevice(@Param("deviceId") String deviceId);
    
    // Find menus that are NOT submenus (don't appear as submenu in any MenuItem)
    @Query("SELECT m FROM Menu m WHERE m.device.id = :deviceId AND m.id NOT IN (SELECT mi.submenu.id FROM MenuItem mi WHERE mi.submenu IS NOT NULL)")
    List<Menu> findMainMenusByDeviceId(@Param("deviceId") String deviceId);
    
    boolean existsByDeviceId(String deviceId);
    boolean existsByNameAndDeviceId(String name, String deviceId);
}
