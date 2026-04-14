package smartdeviceos.repository;

import smartdeviceos.entity.MenuItem;
import smartdeviceos.entity.Menu;
import smartdeviceos.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
    
    List<MenuItem> findByMenuId(String menuId);
    
    List<MenuItem> findByMenu(Menu menu);
    
    List<MenuItem> findByMenuIdOrderByPosition(String menuId);
    
    Optional<MenuItem> findByMenuIdAndName(String menuId, String name);
    
    List<MenuItem> findByApp(App app);
    
    List<MenuItem> findBySubmenu(Menu submenu);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.menu.id = :menuId ORDER BY mi.position")
    List<MenuItem> findByMenuIdOrderByPositionAsc(@Param("menuId") String menuId);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.app IS NOT NULL AND mi.menu.id = :menuId")
    List<MenuItem> findApplicationMenuItemsByMenuId(@Param("menuId") String menuId);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.submenu IS NOT NULL AND mi.menu.id = :menuId")
    List<MenuItem> findSubmenuMenuItemsByMenuId(@Param("menuId") String menuId);
    
    @Query("SELECT COUNT(mi) FROM MenuItem mi WHERE mi.menu.id = :menuId")
    long countByMenuId(@Param("menuId") String menuId);
    
    boolean existsByMenuIdAndName(String menuId, String name);
}
