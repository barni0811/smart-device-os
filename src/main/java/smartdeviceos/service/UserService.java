package smartdeviceos.service;

import smartdeviceos.entity.User;
import smartdeviceos.entity.Device;
import smartdeviceos.entity.UserApp;
import smartdeviceos.entity.App;
import smartdeviceos.repository.UserRepository;
import smartdeviceos.repository.DeviceRepository;
import smartdeviceos.repository.UserAppRepository;
import smartdeviceos.repository.AppRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final UserAppRepository userAppRepository;
    private final AppRepository appRepository;
    
    public UserService(UserRepository userRepository, DeviceRepository deviceRepository, 
                      UserAppRepository userAppRepository, AppRepository appRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.userAppRepository = userAppRepository;
        this.appRepository = appRepository;
    }
    
    public User createUser(String name) {
        if (userRepository.existsByName(name)) {
            throw new IllegalArgumentException("User with name '" + name + "' already exists");
        }
        
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        user.setIsActive(true);
        
        return userRepository.save(user);
    }
    
    public User updateUser(String userId, String newName) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        
        if (!user.getName().equals(newName) && userRepository.existsByName(newName)) {
            throw new IllegalArgumentException("User with name '" + newName + "' already exists");
        }
        
        user.setName(newName);
        return userRepository.save(user);
    }
    
    public void deleteUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        User user = userOpt.get();
        user.setIsActive(false);
        userRepository.save(user);
    }
    
    public Optional<User> findUserById(String userId) {
        return userRepository.findById(userId);
    }
    
    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findByIsActive(true);
    }
    
    public List<User> searchUsersByName(String searchTerm) {
        return userRepository.findByNameContainingIgnoreCase(searchTerm);
    }
    
    public User createCurrentUser() {
        String currentUserName = "current_user";
        return createUser(currentUserName);
    }
    
    public void addFavoriteApps(User user, List<String> appNames) {
        for (String appName : appNames) {
            Optional<App> appOpt = appRepository.findByName(appName);
            if (appOpt.isPresent()) {
                App app = appOpt.get();
                
                if (!userAppRepository.existsByUserIdAndAppId(user.getId(), app.getId())) {
                    UserApp userApp = new UserApp();
                    userApp.setUser(user);
                    userApp.setApp(app);
                    userApp.setIsFavorite(true);
                    userAppRepository.save(userApp);
                }
            }
        }
    }
    
    public List<UserApp> getFavoriteApps(String userId) {
        return userAppRepository.findByUserIdAndIsFavorite(userId, true);
    }
}
