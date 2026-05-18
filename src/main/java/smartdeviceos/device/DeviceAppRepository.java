package smartdeviceos.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceAppRepository extends JpaRepository<DeviceApp, DeviceAppId> {
    Optional<DeviceApp> findByDeviceIdAndAppId(String deviceId, String appId);
    boolean existsByDeviceIdAndAppId(String deviceId, String appId);
    List<DeviceApp> findByDeviceId(String deviceId);

    @Query("SELECT da FROM DeviceApp da WHERE da.device.id = ?1 ORDER BY da.lastUsed DESC")
    List<DeviceApp> findRecentlyUsedAppsByDeviceId(String deviceId);
}
