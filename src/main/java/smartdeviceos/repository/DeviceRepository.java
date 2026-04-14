package smartdeviceos.repository;

import smartdeviceos.entity.Device;
import smartdeviceos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    
    List<Device> findByUserId(String userId);
    
    List<Device> findByUser(User user);
    
    Optional<Device> findByNameAndUserId(String name, String userId);
    
    List<Device> findByIsDefaultMenu(Boolean isDefaultMenu);
    
    @Query("SELECT d FROM Device d WHERE d.user.id = :userId AND d.isDefaultMenu = true")
    Optional<Device> findDefaultDeviceByUserId(@Param("userId") String userId);
    
    boolean existsByNameAndUserId(String name, String userId);
}
