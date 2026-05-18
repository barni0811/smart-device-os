package smartdeviceos.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, UserAppId> {
    List<UserApp> findByUserId(String userId);
    List<UserApp> findByUserIdAndIsFavorite(String userId, boolean isFavorite);
    boolean existsByUserIdAndAppId(String userId, String appId);
}
