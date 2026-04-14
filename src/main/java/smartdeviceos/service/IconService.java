package smartdeviceos.service;

import smartdeviceos.entity.Icon;
import smartdeviceos.repository.IconRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class IconService {
    
    private final IconRepository iconRepository;
    
    public IconService(IconRepository iconRepository) {
        this.iconRepository = iconRepository;
    }
    
    public Icon createIcon(String name, String imagePath) {
        if (iconRepository.existsByName(name)) {
            throw new IllegalArgumentException("Icon with name '" + name + "' already exists");
        }
        
        Icon icon = new Icon();
        icon.setId(UUID.randomUUID().toString());
        icon.setName(name);
        icon.setImagePath(imagePath);
        
        return iconRepository.save(icon);
    }
    
    public Icon updateIcon(String iconId, String newName, String newImagePath) {
        Optional<Icon> iconOpt = iconRepository.findById(iconId);
        if (iconOpt.isEmpty()) {
            throw new IllegalArgumentException("Icon not found with ID: " + iconId);
        }
        
        Icon icon = iconOpt.get();
        
        if (!icon.getName().equals(newName) && iconRepository.existsByName(newName)) {
            throw new IllegalArgumentException("Icon with name '" + newName + "' already exists");
        }
        
        icon.setName(newName);
        icon.setImagePath(newImagePath);
        
        return iconRepository.save(icon);
    }
    
    public void deleteIcon(String iconId) {
        if (!iconRepository.existsById(iconId)) {
            throw new IllegalArgumentException("Icon not found with ID: " + iconId);
        }
        
        iconRepository.deleteById(iconId);
    }
    
    public Optional<Icon> findIconById(String iconId) {
        return iconRepository.findById(iconId);
    }
    
    public Optional<Icon> findIconByName(String name) {
        return iconRepository.findByName(name);
    }
    
    public List<Icon> getAllIcons() {
        return iconRepository.findAllOrderByName();
    }
    
    public List<Icon> searchIcons(String keyword) {
        return iconRepository.findByNameContainingOrderBy(keyword);
    }
}
