package smartdeviceos.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String name);
    boolean existsByName(String name);
    List<User> findByIsActive(boolean isActive);
    List<User> findByNameContainingIgnoreCase(String searchTerm);
}
