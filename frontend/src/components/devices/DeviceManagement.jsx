import React, { useState, useEffect } from 'react';
import { deviceApi, userApi, menuApi, wallpaperApi, themeApi, appApi, iconApi } from '../../api/apiClient';

function DeviceManagement() {
  const [devices, setDevices] = useState([]);
  const [users, setUsers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState('');
  const [deviceName, setDeviceName] = useState('');
  const [loading, setLoading] = useState(false);
  const [managingDevice, setManagingDevice] = useState(null);
  const [manageTab, setManageTab] = useState('menus');
  const [deviceMenus, setDeviceMenus] = useState([]);
  const [newMenuName, setNewMenuName] = useState('');
  const [deviceWallpapers, setDeviceWallpapers] = useState([]);
  const [deviceThemes, setDeviceThemes] = useState([]);
  const [newWallpaperName, setNewWallpaperName] = useState('');
  const [newWallpaperPath, setNewWallpaperPath] = useState('');
  const [newThemeName, setNewThemeName] = useState('');
  const [newPrimaryColor, setNewPrimaryColor] = useState('');
  const [newSecondaryColor, setNewSecondaryColor] = useState('');
  const [newFontFamily, setNewFontFamily] = useState('');
  // Menu Item Management State
  const [managingMenu, setManagingMenu] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const [menuItemTab, setMenuItemTab] = useState('items'); // items, addApp, addFile, addSubmenu
  const [availableApps, setAvailableApps] = useState([]);
  const [newFileName, setNewFileName] = useState('');
  const [newSubmenuName, setNewSubmenuName] = useState('');
  const [renameItemId, setRenameItemId] = useState(null);
  const [renameItemName, setRenameItemName] = useState('');
  // Wallpaper and Theme Management State
  const [editingWallpaper, setEditingWallpaper] = useState(null);
  const [editWallpaperName, setEditWallpaperName] = useState('');
  const [editWallpaperPath, setEditWallpaperPath] = useState('');
  const [editingTheme, setEditingTheme] = useState(null);
  const [editThemeName, setEditThemeName] = useState('');
  const [editPrimaryColor, setEditPrimaryColor] = useState('');
  const [editSecondaryColor, setEditSecondaryColor] = useState('');
  const [editFontFamily, setEditFontFamily] = useState('');
  // Icons and Apps Management State
  const [deviceApps, setDeviceApps] = useState([]);
  const [editingApp, setEditingApp] = useState(null);
  const [editAppName, setEditAppName] = useState('');
  const [icons, setIcons] = useState([]);
  const [newIconName, setNewIconName] = useState('');
  const [newIconPath, setNewIconPath] = useState('');
  const [editingIcon, setEditingIcon] = useState(null);
  const [managingIcons, setManagingIcons] = useState(false);
  const [editDeviceName, setEditDeviceName] = useState('');

  const fetchDevices = async () => {
    try {
      const response = await deviceApi.getAll();
      setDevices(response.data);
    } catch (error) {
      console.error('Error fetching devices:', error);
    }
  };

  const fetchUsers = async () => {
    try {
      const response = await userApi.getAll();
      setUsers(response.data);
      if (response.data.length > 0) {
        setSelectedUserId(response.data[0].id);
      }
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  useEffect(() => {
    fetchDevices();
    fetchUsers();
  }, []);

  // Auto-load data when switching tabs in device management
  useEffect(() => {
    if (!managingDevice) return;
    if (manageTab === 'menus') {
      fetchDeviceMenus(managingDevice.id);
    } else if (manageTab === 'icons') {
      fetchIcons();
    } else if (manageTab === 'apps') {
      fetchDeviceApps();
    } else if (manageTab === 'customization') {
      fetchDeviceWallpapers(managingDevice.id);
      fetchDeviceThemes(managingDevice.id);
    }
  }, [manageTab, managingDevice]);

  const handleCreateDevice = async (e) => {
    e.preventDefault();
    if (!deviceName.trim() || !selectedUserId) return;

    setLoading(true);
    try {
      await deviceApi.create({
        name: deviceName,
        user: { id: selectedUserId }
      });
      setDeviceName('');
      fetchDevices();
    } catch (error) {
      console.error('Error creating device:', error);
      alert('Error creating device');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteDevice = async (deviceId) => {
    try {
      await deviceApi.delete(deviceId);
      fetchDevices();
    } catch (error) {
      console.error('Error deleting device:', error);
      alert('Error deleting device');
    }
  };

  const handleManageDevice = (device) => {
    setManagingDevice(device);
    setEditDeviceName(device.name);
    setManageTab('modify');
    fetchDeviceMenus(device.id);
    fetchDeviceWallpapers(device.id);
    fetchDeviceThemes(device.id);
  };

  const handleUpdateDevice = async (e) => {
    e.preventDefault();
    if (!managingDevice || !editDeviceName.trim()) return;
    try {
      await deviceApi.update(managingDevice.id, { name: editDeviceName });
      setManagingDevice({ ...managingDevice, name: editDeviceName });
      fetchDevices();
    } catch (error) {
      console.error('Error updating device:', error);
      alert('Error updating device');
    }
  };

  const fetchDeviceMenus = async (deviceId) => {
    try {
      const response = await menuApi.getByDeviceId(deviceId);
      setDeviceMenus(response.data);
    } catch (error) {
      console.error('Error fetching device menus:', error);
    }
  };

  const fetchDeviceWallpapers = async (deviceId) => {
    try {
      const response = await wallpaperApi.getByDeviceId(deviceId);
      setDeviceWallpapers(response.data);
    } catch (error) {
      console.error('Error fetching device wallpapers:', error);
    }
  };

  const fetchDeviceThemes = async (deviceId) => {
    try {
      const response = await themeApi.getByDeviceId(deviceId);
      setDeviceThemes(response.data);
    } catch (error) {
      console.error('Error fetching device themes:', error);
    }
  };

  const handleCreateMenu = async (e) => {
    e.preventDefault();
    if (!newMenuName.trim() || !managingDevice) return;

    try {
      await menuApi.create({ deviceId: managingDevice.id, name: newMenuName });
      setNewMenuName('');
      fetchDeviceMenus(managingDevice.id);
    } catch (error) {
      console.error('Error creating menu:', error);
      alert('Error creating menu');
    }
  };

  const handleDeleteMenu = async (menuId) => {
    try {
      await menuApi.delete(menuId);
      fetchDeviceMenus(managingDevice.id);
    } catch (error) {
      console.error('Error deleting menu:', error);
      alert('Error deleting menu');
    }
  };

  const handleCreateWallpaper = async (e) => {
    e.preventDefault();
    if (!newWallpaperName.trim() || !newWallpaperPath.trim() || !managingDevice) return;

    try {
      await wallpaperApi.create({
        name: newWallpaperName,
        imagePath: newWallpaperPath,
        deviceId: managingDevice.id
      });
      setNewWallpaperName('');
      setNewWallpaperPath('');
      fetchDeviceWallpapers(managingDevice.id);
    } catch (error) {
      console.error('Error creating wallpaper:', error);
      alert('Error creating wallpaper');
    }
  };

  const handleCreateTheme = async (e) => {
    e.preventDefault();
    if (!newThemeName.trim() || !newPrimaryColor.trim() || !newSecondaryColor.trim() || !newFontFamily.trim() || !managingDevice) return;

    try {
      await themeApi.create({
        name: newThemeName,
        primaryColor: newPrimaryColor,
        secondaryColor: newSecondaryColor,
        fontFamily: newFontFamily,
        deviceId: managingDevice.id
      });
      setNewThemeName('');
      setNewPrimaryColor('');
      setNewSecondaryColor('');
      setNewFontFamily('');
      fetchDeviceThemes(managingDevice.id);
    } catch (error) {
      console.error('Error creating theme:', error);
      alert('Error creating theme');
    }
  };

  const handleSelectWallpaper = async (wallpaperId) => {
    if (!managingDevice) return;
    try {
      await wallpaperApi.select(managingDevice.id, wallpaperId);
      const deviceResponse = await deviceApi.getById(managingDevice.id);
      setManagingDevice(deviceResponse.data);
      fetchDeviceWallpapers(managingDevice.id);
    } catch (error) {
      console.error('Error selecting wallpaper:', error);
      alert('Error selecting wallpaper');
    }
  };

  const handleSelectTheme = async (themeId) => {
    if (!managingDevice) return;
    try {
      await themeApi.select(managingDevice.id, themeId);
      const deviceResponse = await deviceApi.getById(managingDevice.id);
      setManagingDevice(deviceResponse.data);
      fetchDeviceThemes(managingDevice.id);
    } catch (error) {
      console.error('Error selecting theme:', error);
      alert('Error selecting theme');
    }
  };

  // Menu Item Management Functions
  const handleManageMenu = async (menu) => {
    setManagingMenu(menu);
    setMenuItemTab('items');
    fetchMenuItems(menu.id);
  };

  const fetchMenuItems = async (menuId) => {
    try {
      const response = await menuApi.getMenuItems(menuId);
      setMenuItems(response.data);
      fetchAvailableApps(response.data);
    } catch (error) {
      console.error('Error fetching menu items:', error);
    }
  };

  const fetchAvailableApps = async (currentMenuItems = menuItems) => {
    try {
      const response = await appApi.getAll();
      const addedAppNames = currentMenuItems.filter(item => item.type === 'app').map(item => item.appName);
      const available = response.data.filter(app => !addedAppNames.includes(app.name));
      setAvailableApps(available);
    } catch (error) {
      console.error('Error fetching apps:', error);
    }
  };

  const handleAddApplicationToMenu = async (appId, appName) => {
    if (!managingMenu) return;
    try {
      const position = menuItems.length + 1;
      await menuApi.addApplicationToMenu(managingMenu.id, appId, appName, position);
      fetchMenuItems(managingMenu.id);
      setMenuItemTab('items');
    } catch (error) {
      console.error('Error adding application to menu:', error);
      alert('Error adding application to menu');
    }
  };

  const handleAddFileToMenu = async (e) => {
    e.preventDefault();
    if (!managingMenu || !newFileName.trim()) return;
    try {
      const position = menuItems.length + 1;
      await menuApi.addFileToMenu(managingMenu.id, newFileName, position);
      setNewFileName('');
      fetchMenuItems(managingMenu.id);
      setMenuItemTab('items');
    } catch (error) {
      console.error('Error adding file to menu:', error);
      alert('Error adding file to menu');
    }
  };

  const handleAddSubmenuToMenu = async (e) => {
    e.preventDefault();
    if (!managingMenu || !newSubmenuName.trim()) return;
    try {
      const position = menuItems.length + 1;
      await menuApi.addSubmenuToMenu(managingMenu.id, newSubmenuName, position);
      setNewSubmenuName('');
      fetchMenuItems(managingMenu.id);
      setMenuItemTab('items');
    } catch (error) {
      console.error('Error adding submenu to menu:', error);
      alert('Error adding submenu to menu');
    }
  };

  const handleRemoveMenuItem = async (itemId) => {
    try {
      await menuApi.removeMenuItem(itemId);
      fetchMenuItems(managingMenu.id);
    } catch (error) {
      console.error('Error removing menu item:', error);
      alert('Error removing menu item');
    }
  };

  const handleRenameMenuItem = async (e) => {
    e.preventDefault();
    if (!renameItemId || !renameItemName.trim()) return;
    try {
      await menuApi.renameMenuItem(renameItemId, renameItemName);
      setRenameItemId(null);
      setRenameItemName('');
      fetchMenuItems(managingMenu.id);
    } catch (error) {
      console.error('Error renaming menu item:', error);
      alert('Error renaming menu item');
    }
  };

  const startRenameMenuItem = (item) => {
    setRenameItemId(item.id);
    setRenameItemName(item.name);
  };

  const handleDeleteWallpaper = async (wallpaperId) => {
    try {
      await wallpaperApi.delete(wallpaperId);
      fetchDeviceWallpapers(managingDevice.id);
    } catch (error) {
      console.error('Error deleting wallpaper:', error);
      alert('Error deleting wallpaper');
    }
  };

  const handleDeleteTheme = async (themeId) => {
    try {
      await themeApi.delete(themeId);
      fetchDeviceThemes(managingDevice.id);
    } catch (error) {
      console.error('Error deleting theme:', error);
      alert('Error deleting theme');
    }
  };

  // Wallpaper Management Functions
  const startEditWallpaper = (wallpaper) => {
    setEditingWallpaper(wallpaper);
    setEditWallpaperName(wallpaper.name);
    setEditWallpaperPath(wallpaper.imagePath);
  };

  const handleUpdateWallpaper = async (e) => {
    e.preventDefault();
    if (!editingWallpaper || !editWallpaperName.trim() || !editWallpaperPath.trim()) return;
    try {
      console.log('Sending wallpaper update:', { id: editingWallpaper.id, name: editWallpaperName, imagePath: editWallpaperPath });
      await wallpaperApi.update(editingWallpaper.id, {
        name: editWallpaperName,
        imagePath: editWallpaperPath
      });
      setEditingWallpaper(null);
      setEditWallpaperName('');
      setEditWallpaperPath('');
      fetchDeviceWallpapers(managingDevice.id);
    } catch (error) {
      console.error('Error updating wallpaper:', error);
      console.error('Error response:', error.response);
      alert('Error updating wallpaper: ' + (error.response?.status || error.message));
    }
  };

  // Theme Management Functions
  const startEditTheme = (theme) => {
    setEditingTheme(theme);
    setEditThemeName(theme.name);
    setEditPrimaryColor(theme.primaryColor);
    setEditSecondaryColor(theme.secondaryColor);
    setEditFontFamily(theme.fontFamily);
  };

  const handleUpdateTheme = async (e) => {
    e.preventDefault();
    if (!editingTheme || !editThemeName.trim() || !editPrimaryColor.trim() || !editSecondaryColor.trim() || !editFontFamily.trim()) return;
    try {
      console.log('Sending theme update:', { id: editingTheme.id, name: editThemeName, primaryColor: editPrimaryColor, secondaryColor: editSecondaryColor, fontFamily: editFontFamily });
      await themeApi.update(editingTheme.id, {
        name: editThemeName,
        primaryColor: editPrimaryColor,
        secondaryColor: editSecondaryColor,
        fontFamily: editFontFamily
      });
      setEditingTheme(null);
      setEditThemeName('');
      setEditPrimaryColor('');
      setEditSecondaryColor('');
      setEditFontFamily('');
      fetchDeviceThemes(managingDevice.id);
    } catch (error) {
      console.error('Error updating theme:', error);
      console.error('Error response:', error.response);
      alert('Error updating theme: ' + (error.response?.status || error.message));
    }
  };

  // Apps Management Functions
  const fetchDeviceApps = async () => {
    try {
      const response = await appApi.getAll();
      setDeviceApps(response.data);
    } catch (error) {
      console.error('Error fetching apps:', error);
    }
  };

  const startEditApp = (app) => {
    setEditingApp(app);
    setEditAppName(app.name);
  };

  const handleUpdateApp = async (e) => {
    e.preventDefault();
    if (!editingApp || !editAppName.trim()) return;
    try {
      await appApi.update(editingApp.id, { name: editAppName });
      setEditingApp(null);
      setEditAppName('');
      fetchDeviceApps();
    } catch (error) {
      console.error('Error updating app:', error);
      alert('Error updating app');
    }
  };

  const handleDeleteApp = async (appId) => {
    try {
      await appApi.delete(appId);
      fetchDeviceApps();
    } catch (error) {
      console.error('Error deleting app:', error);
      alert('Error deleting app');
    }
  };

  // Icons Management Functions
  const fetchIcons = async () => {
    try {
      const response = await iconApi.getAll();
      setIcons(response.data);
    } catch (error) {
      console.error('Error fetching icons:', error);
    }
  };

  const handleCreateIcon = async (e) => {
    e.preventDefault();
    if (!newIconName.trim() || !newIconPath.trim()) return;
    try {
      await iconApi.create({ name: newIconName, imagePath: newIconPath });
      setNewIconName('');
      setNewIconPath('');
      fetchIcons();
    } catch (error) {
      console.error('Error creating icon:', error);
      alert('Error creating icon');
    }
  };

  const handleUpdateIcon = async (e) => {
    e.preventDefault();
    if (!editingIcon) return;
    try {
      await iconApi.update(editingIcon.id, { name: newIconName, imagePath: newIconPath });
      setEditingIcon(null);
      setNewIconName('');
      setNewIconPath('');
      fetchIcons();
    } catch (error) {
      console.error('Error updating icon:', error);
      alert('Error updating icon');
    }
  };

  const handleDeleteIcon = async (iconId) => {
    try {
      await iconApi.delete(iconId);
      fetchIcons();
    } catch (error) {
      console.error('Error deleting icon:', error);
      alert('Error deleting icon');
    }
  };

  const startEditIcon = (icon) => {
    setEditingIcon(icon);
    setNewIconName(icon.name);
    setNewIconPath(icon.imagePath);
  };

  const cancelEditIcon = () => {
    setEditingIcon(null);
    setNewIconName('');
    setNewIconPath('');
  };

  // Icons Management Functions

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Device Management</h1>
      
      <div className="bg-green-50 rounded-lg shadow p-6 mb-6">
        <h2 className="text-xl font-bold mb-4">Create New Device</h2>
        <form onSubmit={handleCreateDevice} className="flex gap-4">
          <select
            value={selectedUserId}
            onChange={(e) => setSelectedUserId(e.target.value)}
            className="px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
          >
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name}
              </option>
            ))}
          </select>
          <input
            type="text"
            value={deviceName}
            onChange={(e) => setDeviceName(e.target.value)}
            placeholder="Device name"
            className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
          >
            {loading ? 'Creating...' : 'Create Device'}
          </button>
        </form>
      </div>

      <div className="bg-green-50 rounded-lg shadow p-6">
        <h2 className="text-xl font-bold mb-4">Devices ({devices.length})</h2>
        {devices.length === 0 ? (
          <p className="text-gray-500">No devices found</p>
        ) : (
          <div className="grid gap-4">
            {devices.map((device) => (
              <div key={device.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                <div>
                  <h3 className="font-bold">{device.name}</h3>
                  {device.user && <p className="text-sm text-gray-500">Owner: {device.user.name}</p>}
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => handleManageDevice(device)}
                    className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                  >
                    Manage
                  </button>
                  <button
                    onClick={() => handleDeleteDevice(device.id)}
                    className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-4 rounded"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {managingDevice && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-green-50 rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden">
            <div className="bg-gray-100 px-6 py-4 border-b flex justify-between items-center">
              <h2 className="text-xl font-bold">Managing Device: {managingDevice.name}</h2>
              <button
                onClick={() => setManagingDevice(null)}
                className="text-gray-500 hover:text-gray-700 text-2xl"
              >
                ×
              </button>
            </div>
            <div className="flex border-b">
              <button
                onClick={() => setManageTab('modify')}
                className={`px-6 py-3 ${manageTab === 'modify' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Modify
              </button>
              <button
                onClick={() => setManageTab('menus')}
                className={`px-6 py-3 ${manageTab === 'menus' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Menus
              </button>
              <button
                onClick={() => setManageTab('icons')}
                className={`px-6 py-3 ${manageTab === 'icons' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Icons
              </button>
              <button
                onClick={() => setManageTab('apps')}
                className={`px-6 py-3 ${manageTab === 'apps' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Apps
              </button>
              <button
                onClick={() => setManageTab('customization')}
                className={`px-6 py-3 ${manageTab === 'customization' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Customization
              </button>
            </div>
            <div className="p-6 overflow-y-auto max-h-[calc(90vh-120px)]">
              {manageTab === 'modify' && (
                <div>
                  <h3 className="font-bold mb-4">Modify Device</h3>
                  <form onSubmit={handleUpdateDevice} className="flex gap-2">
                    <input
                      type="text"
                      value={editDeviceName}
                      onChange={(e) => setEditDeviceName(e.target.value)}
                      placeholder="Device name"
                      className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                      required
                      autoFocus
                    />
                    <button
                      type="submit"
                      className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                    >
                      Save
                    </button>
                  </form>
                </div>
              )}
              {manageTab === 'menus' && (
                <div>
                  <h3 className="font-bold mb-4">Menu Management</h3>
                  <form onSubmit={handleCreateMenu} className="flex gap-2 mb-6">
                    <input
                      type="text"
                      value={newMenuName}
                      onChange={(e) => setNewMenuName(e.target.value)}
                      placeholder="New menu name"
                      className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <button
                      type="submit"
                      className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                    >
                      Add Menu
                    </button>
                  </form>
                  {deviceMenus.length === 0 ? (
                    <p className="text-gray-500 mb-6">No menus found for this device.</p>
                  ) : (
                    <div className="grid gap-4">
                      {deviceMenus.map((menu) => (
                        <div key={menu.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          <div>
                            <h4 className="font-bold">{menu.name}</h4>
                          </div>
                          <div className="flex gap-2">
                            <button
                              onClick={() => handleManageMenu(menu)}
                              className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                            >
                              Manage
                            </button>
                            <button
                              onClick={() => handleDeleteMenu(menu.id)}
                              className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-4 rounded"
                            >
                              Delete
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
              {manageTab === 'icons' && (
                <div>
                  <h3 className="font-bold mb-4">Icon Management</h3>
                  {editingIcon ? (
                    <form onSubmit={handleUpdateIcon} className="flex gap-2 mb-6">
                      <input
                        type="text"
                        value={newIconName}
                        onChange={(e) => setNewIconName(e.target.value)}
                        placeholder="Icon name"
                        className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                        required
                      />
                      <input
                        type="text"
                        value={newIconPath}
                        onChange={(e) => setNewIconPath(e.target.value)}
                        placeholder="Image path"
                        className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                        required
                      />
                      <button
                        type="submit"
                        className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                      >
                        Save
                      </button>
                      <button
                        type="button"
                        onClick={cancelEditIcon}
                        className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
                      >
                        Cancel
                      </button>
                    </form>
                  ) : (
                    <form onSubmit={handleCreateIcon} className="flex gap-2 mb-6">
                      <input
                        type="text"
                        value={newIconName}
                        onChange={(e) => setNewIconName(e.target.value)}
                        placeholder="Icon name"
                        className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                        required
                      />
                      <input
                        type="text"
                        value={newIconPath}
                        onChange={(e) => setNewIconPath(e.target.value)}
                        placeholder="Image path"
                        className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                        required
                      />
                      <button
                        type="submit"
                        className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                      >
                        Add Icon
                      </button>
                    </form>
                  )}
                  {icons.length === 0 ? (
                    <p className="text-gray-500">No icons found.</p>
                  ) : (
                    <div className="grid gap-4">
                      {icons.map((icon) => (
                        <div key={icon.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          <div className="flex items-center gap-2">
                            <span className="text-2xl"></span>
                            <h4 className="font-bold">{icon.name}</h4>
                            <span className="text-xs bg-purple-100 text-purple-800 px-2 py-1 rounded">{icon.imagePath}</span>
                          </div>
                          <div className="flex gap-2">
                            <button
                              onClick={() => startEditIcon(icon)}
                              className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                            >
                              Modify
                            </button>
                            <button
                              onClick={() => handleDeleteIcon(icon.id)}
                              className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-4 rounded"
                            >
                              Delete
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
              {manageTab === 'apps' && (
                <div>
                  <h3 className="font-bold mb-4">Application Management</h3>
                  {deviceApps.length === 0 ? (
                    <p className="text-gray-500">Loading applications...</p>
                  ) : (
                    <div className="grid gap-4">
                      {deviceApps.map((app) => (
                        <div key={app.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          <div className="flex items-center gap-2">
                            <span className="text-2xl"></span>
                            <h4 className="font-bold">{app.name}</h4>
                          </div>
                          <button
                            onClick={() => handleDeleteApp(app.id)}
                            className="text-red-500 hover:text-red-700 font-medium"
                          >
                            Remove
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
              {manageTab === 'customization' && (
                <div>
                  <h3 className="font-bold mb-4">Wallpaper Management</h3>
                  <form onSubmit={handleCreateWallpaper} className="flex gap-2 mb-6">
                    <input
                      type="text"
                      value={newWallpaperName}
                      onChange={(e) => setNewWallpaperName(e.target.value)}
                      placeholder="Wallpaper name"
                      className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <input
                      type="text"
                      value={newWallpaperPath}
                      onChange={(e) => setNewWallpaperPath(e.target.value)}
                      placeholder="Image path"
                      className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <button
                      type="submit"
                      className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                    >
                      Add Wallpaper
                    </button>
                  </form>
                  {deviceWallpapers.length === 0 ? (
                    <p className="text-gray-500 mb-6">No wallpapers found for this device.</p>
                  ) : (
                    <div className="grid gap-4 mb-6">
                      {deviceWallpapers.map((wallpaper) => (
                        <div key={wallpaper.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          {editingWallpaper?.id === wallpaper.id ? (
                            <form onSubmit={handleUpdateWallpaper} className="flex-1 flex gap-2">
                              <input
                                type="text"
                                value={editWallpaperName}
                                onChange={(e) => setEditWallpaperName(e.target.value)}
                                className="flex-1 px-2 py-1 border rounded bg-green-50"
                                placeholder="Name"
                              />
                              <input
                                type="text"
                                value={editWallpaperPath}
                                onChange={(e) => setEditWallpaperPath(e.target.value)}
                                className="flex-1 px-2 py-1 border rounded bg-green-50"
                                placeholder="Path"
                              />
                              <button
                                type="submit"
                                className="bg-green-600 hover:bg-green-700 text-white font-bold py-1 px-3 rounded"
                              >
                                Save
                              </button>
                              <button
                                type="button"
                                onClick={() => setEditingWallpaper(null)}
                                className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-3 rounded"
                              >
                                Cancel
                              </button>
                            </form>
                          ) : (
                            <>
                              <div>
                                <h4 className="font-bold">{wallpaper.name}</h4>
                                <p className="text-sm text-gray-500">Path: {wallpaper.imagePath}</p>
                                <div className="flex gap-2 mt-2">
                                  {wallpaper.isDefault && <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded">Default</span>}
                                  {managingDevice.wallpaper && managingDevice.wallpaper.id === wallpaper.id && <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">Active</span>}
                                </div>
                              </div>
                              <div className="flex gap-2">
                                <button
                                  onClick={() => handleSelectWallpaper(wallpaper.id)}
                                  disabled={managingDevice.wallpaper && managingDevice.wallpaper.id === wallpaper.id}
                                  className="bg-green-600 hover:bg-green-700 text-white font-bold py-1 px-4 rounded disabled:bg-gray-400"
                                >
                                  {managingDevice.wallpaper && managingDevice.wallpaper.id === wallpaper.id ? 'Selected' : 'Select'}
                                </button>
                                <button
                                  onClick={() => startEditWallpaper(wallpaper)}
                                  className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                                >
                                  Manage
                                </button>
                                <button
                                  onClick={() => handleDeleteWallpaper(wallpaper.id)}
                                  className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-4 rounded"
                                >
                                  Delete
                                </button>
                              </div>
                            </>
                          )}
                        </div>
                      ))}
                    </div>
                  )}

                  <h3 className="font-bold mb-4 mt-8">Theme Management</h3>
                  <form onSubmit={handleCreateTheme} className="space-y-2 mb-6">
                    <input
                      type="text"
                      value={newThemeName}
                      onChange={(e) => setNewThemeName(e.target.value)}
                      placeholder="Theme name"
                      className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <input
                      type="text"
                      value={newPrimaryColor}
                      onChange={(e) => setNewPrimaryColor(e.target.value)}
                      placeholder="Primary color (e.g., #FFFFFF)"
                      className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <input
                      type="text"
                      value={newSecondaryColor}
                      onChange={(e) => setNewSecondaryColor(e.target.value)}
                      placeholder="Secondary color (e.g., #000000)"
                      className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <input
                      type="text"
                      value={newFontFamily}
                      onChange={(e) => setNewFontFamily(e.target.value)}
                      placeholder="Font family (e.g., Arial)"
                      className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <button
                      type="submit"
                      className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                    >
                      Add Theme
                    </button>
                  </form>
                  {deviceThemes.length === 0 ? (
                    <p className="text-gray-500">No themes found for this device.</p>
                  ) : (
                    <div className="grid gap-4">
                      {deviceThemes.map((theme) => (
                        <div key={theme.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          {editingTheme?.id === theme.id ? (
                            <form onSubmit={handleUpdateTheme} className="flex-1 space-y-2">
                              <input
                                type="text"
                                value={editThemeName}
                                onChange={(e) => setEditThemeName(e.target.value)}
                                className="w-full px-2 py-1 border rounded bg-green-50"
                                placeholder="Theme name"
                              />
                              <input
                                type="text"
                                value={editPrimaryColor}
                                onChange={(e) => setEditPrimaryColor(e.target.value)}
                                className="w-full px-2 py-1 border rounded bg-green-50"
                                placeholder="Primary color"
                              />
                              <input
                                type="text"
                                value={editSecondaryColor}
                                onChange={(e) => setEditSecondaryColor(e.target.value)}
                                className="w-full px-2 py-1 border rounded bg-green-50"
                                placeholder="Secondary color"
                              />
                              <input
                                type="text"
                                value={editFontFamily}
                                onChange={(e) => setEditFontFamily(e.target.value)}
                                className="w-full px-2 py-1 border rounded bg-green-50"
                                placeholder="Font family"
                              />
                              <div className="flex gap-2">
                                <button
                                  type="submit"
                                  className="bg-green-600 hover:bg-green-700 text-white font-bold py-1 px-3 rounded"
                                >
                                  Save
                                </button>
                                <button
                                  type="button"
                                  onClick={() => setEditingTheme(null)}
                                  className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-3 rounded"
                                >
                                  Cancel
                                </button>
                              </div>
                            </form>
                          ) : (
                            <>
                              <div>
                                <h4 className="font-bold">{theme.name}</h4>
                                <p className="text-sm text-gray-500">Primary: {theme.primaryColor}</p>
                                <p className="text-sm text-gray-500">Secondary: {theme.secondaryColor}</p>
                                <p className="text-sm text-gray-500">Font: {theme.fontFamily}</p>
                                <div className="flex gap-2 mt-2">
                                  {theme.isDefault && <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded">Default</span>}
                                  {managingDevice.theme && managingDevice.theme.id === theme.id && <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">Active</span>}
                                </div>
                              </div>
                              <div className="flex gap-2">
                                <button
                                  onClick={() => handleSelectTheme(theme.id)}
                                  disabled={managingDevice.theme && managingDevice.theme.id === theme.id}
                                  className="bg-green-600 hover:bg-green-700 text-white font-bold py-1 px-4 rounded disabled:bg-gray-400"
                                >
                                  {managingDevice.theme && managingDevice.theme.id === theme.id ? 'Selected' : 'Select'}
                                </button>
                                <button
                                  onClick={() => startEditTheme(theme)}
                                  className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                                >
                                  Manage
                                </button>
                                <button
                                  onClick={() => handleDeleteTheme(theme.id)}
                                  className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-4 rounded"
                                >
                                  Delete
                                </button>
                              </div>
                            </>
                          )}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      )}

      {/* Menu Item Management Modal */}
      {managingMenu && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-green-50 rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden">
            <div className="bg-gray-100 px-6 py-4 border-b flex justify-between items-center">
              <h2 className="text-xl font-bold">Managing Menu: {managingMenu.name}</h2>
              <button
                onClick={() => setManagingMenu(null)}
                className="text-gray-500 hover:text-gray-700 text-2xl"
              >
                ×
              </button>
            </div>
            <div className="flex border-b">
              <button
                onClick={() => setMenuItemTab('items')}
                className={`px-6 py-3 ${menuItemTab === 'items' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                List Items
              </button>
              <button
                onClick={() => setMenuItemTab('addApp')}
                className={`px-6 py-3 ${menuItemTab === 'addApp' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Add Application
              </button>
              <button
                onClick={() => setMenuItemTab('addFile')}
                className={`px-6 py-3 ${menuItemTab === 'addFile' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Add File
              </button>
              <button
                onClick={() => setMenuItemTab('addSubmenu')}
                className={`px-6 py-3 ${menuItemTab === 'addSubmenu' ? 'bg-green-700 text-white' : 'hover:bg-green-100'}`}
              >
                Add Submenu
              </button>
            </div>
            <div className="p-6 overflow-y-auto max-h-[calc(90vh-120px)]">
              {/* List Menu Items Tab */}
              {menuItemTab === 'items' && (
                <div>
                  <h3 className="font-bold mb-4">Menu Items ({menuItems.length})</h3>
                  {menuItems.length === 0 ? (
                    <p className="text-gray-500">No items in this menu.</p>
                  ) : (
                    <div className="grid gap-4">
                      {menuItems.map((item, index) => (
                        <div key={item.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          <div>
                            <div className="flex items-center gap-2">
                              <span className="text-gray-500">{index + 1}.</span>
                              <h4 className="font-bold">{item.name}</h4>
                              {item.type === 'submenu' ? (
                                <span className="text-xs bg-purple-100 text-purple-800 px-2 py-1 rounded">[Submenu]</span>
                              ) : item.type === 'app' ? (
                                <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">[App]</span>
                              ) : (
                                <span className="text-xs bg-gray-100 text-gray-800 px-2 py-1 rounded">[File]</span>
                              )}
                            </div>
                          </div>
                          <div className="flex gap-2">
                            {renameItemId === item.id ? (
                              <form onSubmit={handleRenameMenuItem} className="flex gap-2">
                                <input
                                  type="text"
                                  value={renameItemName}
                                  onChange={(e) => setRenameItemName(e.target.value)}
                                  className="px-2 py-1 border rounded bg-green-50"
                                  autoFocus
                                />
                                <button
                                  type="submit"
                                  className="bg-green-600 hover:bg-green-700 text-white font-bold py-1 px-3 rounded"
                                >
                                  Save
                                </button>
                                <button
                                  type="button"
                                  onClick={() => setRenameItemId(null)}
                                  className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-3 rounded"
                                >
                                  Cancel
                                </button>
                              </form>
                            ) : (
                              <>
                                <button
                                  onClick={() => startRenameMenuItem(item)}
                                  className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                                >
                                  Rename
                                </button>
                                <button
                                  onClick={() => handleRemoveMenuItem(item.id)}
                                  className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-4 rounded"
                                >
                                  Remove
                                </button>
                              </>
                            )}
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}

              {/* Add Application Tab */}
              {menuItemTab === 'addApp' && (
                <div>
                  <h3 className="font-bold mb-4">Add Application to Menu</h3>
                  {availableApps.length === 0 ? (
                    <p className="text-gray-500">No applications available.</p>
                  ) : (
                    <div className="grid gap-4">
                      {availableApps.map((app) => (
                        <div key={app.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                          <div>
                            <h4 className="font-bold">{app.name}</h4>
                          </div>
                          <button
                            onClick={() => handleAddApplicationToMenu(app.id, app.name)}
                            className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-1 px-4 rounded"
                          >
                            Add to Menu
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}

              {/* Add File Tab */}
              {menuItemTab === 'addFile' && (
                <div>
                  <h3 className="font-bold mb-4">Add File to Menu</h3>
                  <form onSubmit={handleAddFileToMenu} className="flex gap-2 mb-6">
                    <input
                      type="text"
                      value={newFileName}
                      onChange={(e) => setNewFileName(e.target.value)}
                      placeholder="File name"
                      className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <button
                      type="submit"
                      className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                    >
                      Add File
                    </button>
                  </form>
                </div>
              )}

              {/* Add Submenu Tab */}
              {menuItemTab === 'addSubmenu' && (
                <div>
                  <h3 className="font-bold mb-4">Add Submenu to Menu</h3>
                  <form onSubmit={handleAddSubmenuToMenu} className="flex gap-2 mb-6">
                    <input
                      type="text"
                      value={newSubmenuName}
                      onChange={(e) => setNewSubmenuName(e.target.value)}
                      placeholder="Submenu name"
                      className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <button
                      type="submit"
                      className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                    >
                      Add Submenu
                    </button>
                  </form>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default DeviceManagement;
