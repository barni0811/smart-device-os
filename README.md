# Smart Device OS - Intelligent Device Operating System

## Task Description
Implement a client-server architecture operating system for intelligent devices. The system works as follows:

- **Intelligent Devices**: Information collection and display
- **Cloud**: Computational operations and data storage
- **Users**: Families, companies, groups performing shared activities

## Family Example Scenario
A family (parents and children) uses the system:

1. **First Step**: One parent registers and creates the family account
2. **Add Family Members**: The parent adds the other parent and children
3. **Default State**: All device menus are identical
4. **Customization**: 
   - Mother: installs recipe application
   - Father: installs GPS program  
   - Children: install games

## Functional Requirements

1. **User Account Management**
   - Create (Name)
   - Modify
   - Delete

2. **Menu System**
   - Main menu creation
   - Submenu management from main menu
   - Hierarchical menu structure support

3. **Icon Management**
   - Add new icon (Name)
   - Modify icon
   - Delete icon

4. **Application Management**
   - Application installation
   - Application launching
   - Usage statistics

5. **Customization**
   - Add wallpaper (Name)
   - Select wallpaper (Name)
   - Theme switching (Name)

## Technical Requirements

1. **Object-Oriented Model**
   - Java programming language
   - Unique text identifiers for all objects
   - "One-to-many" and "many-to-many" relationships with `List` types

2. **JPA Annotations**
   - Standard persistence solution (Hibernate)
   - Database schema generation from annotated classes

3. **Maven Application**
   - Buildable as Maven project
   - Console-runnable interactive application
   - Automatic user and favorite applications addition on startup

## Architecture

```
User
  -> Device
    -> Menu
      -> MenuItem
        -> App
```

### Key Design Decisions

- **Devices**: Owned by users, store UI configuration
- **Menus**: Device-specific but support default templates
- **Applications**: Installable per user, independent from menu structure
- **Menu Items**: Links between UI and applications
- **Submenus**: Implemented via recursive menu references
- **Family Management**: Role-based access (parent/child/member)

## Database Design

- **One-to-many relationships**:
  - User -> Devices
  - Family -> FamilyMembers
  - Menu -> MenuItems
  - Device -> Menu

- **Many-to-many relationships**:
  - User <-> Apps (user_apps join table)
  - Device <-> Apps (device_apps join table)
  - Family <-> Users (family_members join table)

- **Self-referencing relationships**:
  - MenuItem -> Menu (submenu support)

### Database Schema

The database schema contains the following tables:
- `users` - Users
- `families` - Families/groups  
- `family_members` - Family members with roles
- `devices` - Devices
- `menus` - Menus per device
- `menu_items` - Menu items (applications and submenus)
- `apps` - System applications
- `user_apps` - User application access
- `device_apps` - Device-specific application installations
- `icons` - Icons
- `wallpapers` - Wallpapers
- `themes` - Themes

The database diagram is available in `docs/db-diagram.png`.

## Technology Stack

- **Java** - Programming language
- **Spring Boot** - Framework
- **Spring Data JPA** - Database access
- **Hibernate** - ORM implementation
- **Maven** - Build and dependency management
- **H2/PostgreSQL** - Database (development/production)

## Running the Application

```bash
mvn clean compile
mvn spring-boot:run
```

## Interactive CLI

On application startup:
1. Automatically adds the current user
2. Creates default device and menu
3. Adds favorite applications
4. Displays interactive menu for feature access