package smartdeviceos.repository;

import smartdeviceos.entity.UserApp;
import smartdeviceos.entity.UserAppId;
import smartdeviceos.entity.User;
import smartdeviceos.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, UserAppId> {
    
    List<UserApp> findByUserId(String userId);
    
    List<UserApp> findByUser(User user);
    
    List<UserApp> findByAppId(String appId);
    
    List<UserApp> findByApp(App app);
    
    List<UserApp> findByIsFavorite(Boolean isFavorite);
    
    List<UserApp> findByUserIdAndIsFavorite(String userId, Boolean isFavorite);
    
    @Query("SELECT ua FROM UserApp ua WHERE ua.user.id = :userId ORDER BY ua.installedAt DESC")
    List<UserApp> findByUserIdOrderByInstalledAtDesc(@Param("userId") String userId);
    
    @Query("SELECT ua FROM UserApp ua WHERE ua.user.id = :userId AND ua.isFavorite = true ORDER BY ua.installedAt DESC")
    List<UserApp> findFavoriteAppsByUserId(@Param("userId") String userId);
    
    @Query("SELECT COUNT(ua) FROM UserApp ua WHERE ua.user.id = :userId")
    long countByUserId(@Param("userId") String userId);
    
    boolean existsByUserIdAndAppId(String userId, String appId);
}
