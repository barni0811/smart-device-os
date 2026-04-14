package smartdeviceos.repository;

import smartdeviceos.entity.DeviceApp;
import smartdeviceos.entity.DeviceAppId;
import smartdeviceos.entity.Device;
import smartdeviceos.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceAppRepository extends JpaRepository<DeviceApp, DeviceAppId> {
    
    List<DeviceApp> findByDeviceId(String deviceId);
    
    List<DeviceApp> findByDevice(Device device);
    
    List<DeviceApp> findByAppId(String appId);
    
    List<DeviceApp> findByApp(App app);
    
    @Query("SELECT da FROM DeviceApp da WHERE da.device.id = :deviceId ORDER BY da.usageCount DESC")
    List<DeviceApp> findByDeviceIdOrderByUsageCountDesc(@Param("deviceId") String deviceId);
    
    @Query("SELECT da FROM DeviceApp da WHERE da.device.id = :deviceId ORDER BY da.lastUsed DESC")
    List<DeviceApp> findByDeviceIdOrderByLastUsedDesc(@Param("deviceId") String deviceId);
    
    @Query("SELECT da FROM DeviceApp da WHERE da.device.id = :deviceId AND da.lastUsed IS NOT NULL ORDER BY da.lastUsed DESC")
    List<DeviceApp> findRecentlyUsedAppsByDeviceId(@Param("deviceId") String deviceId);
    
    @Query("SELECT COUNT(da) FROM DeviceApp da WHERE da.device.id = :deviceId")
    long countByDeviceId(@Param("deviceId") String deviceId);
    
    @Query("SELECT da FROM DeviceApp da WHERE da.device.id = :deviceId AND da.app.name LIKE %:appName%")
    List<DeviceApp> findByDeviceIdAndAppNameContaining(@Param("deviceId") String deviceId, @Param("appName") String appName);
    
    boolean existsByDeviceIdAndAppId(String deviceId, String appId);
    
    Optional<DeviceApp> findByDeviceIdAndAppId(String deviceId, String appId);
}
