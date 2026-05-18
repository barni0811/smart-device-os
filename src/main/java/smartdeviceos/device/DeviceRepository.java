package smartdeviceos.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByUser_Id(String userId);
    Optional<Device> findByNameAndUserId(String name, String userId);
    Optional<Device> findById(String id);

    @Query("SELECT d FROM Device d JOIN FETCH d.user")
    List<Device> findAllWithUsers();

    @Query("SELECT d FROM Device d WHERE d.user.id = ?1 AND d.isDefaultMenu = true")
    Optional<Device> findDefaultDeviceByUserId(String userId);
}
