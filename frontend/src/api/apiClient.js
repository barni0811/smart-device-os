import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const userApi = {
  getAll: () => apiClient.get('/users'),
  getById: (id) => apiClient.get(`/users/${id}`),
  getByName: (name) => apiClient.get(`/users/name/${name}`),
  create: (user) => apiClient.post('/users', user),
  update: (id, user) => apiClient.put(`/users/${id}`, user),
  delete: (id) => apiClient.delete(`/users/${id}`),
};

export const deviceApi = {
  getAll: () => apiClient.get('/devices'),
  getById: (id) => apiClient.get(`/devices/${id}`),
  getByUserId: (userId) => apiClient.get(`/devices/user/${userId}`),
  create: (device) => apiClient.post('/devices', device),
  update: (id, device) => apiClient.put(`/devices/${id}`, device),
  delete: (id) => apiClient.delete(`/devices/${id}`),
};

export const menuApi = {
  getAll: () => apiClient.get('/menus'),
  getById: (id) => apiClient.get(`/menus/${id}`),
  getByDeviceId: (deviceId) => apiClient.get(`/menus/device/${deviceId}`),
  create: (menu) => apiClient.post('/menus', menu),
  createSubmenu: (menuId, submenu) => apiClient.post(`/menus/${menuId}/submenu`, submenu),
  delete: (id) => apiClient.delete(`/menus/${id}`),
  // Menu Item Management
  getMenuItems: (menuId) => apiClient.get(`/menus/${menuId}/items`),
  addApplicationToMenu: (menuId, appId, name, position) => apiClient.post(`/menus/${menuId}/items/app`, { appId, name, position }),
  addFileToMenu: (menuId, fileName, position) => apiClient.post(`/menus/${menuId}/items/file`, { fileName, position }),
  addSubmenuToMenu: (menuId, submenuName, position) => apiClient.post(`/menus/${menuId}/items/submenu`, { submenuName, position }),
  removeMenuItem: (itemId) => apiClient.delete(`/menus/items/${itemId}`),
  renameMenuItem: (itemId, name) => apiClient.put(`/menus/items/${itemId}/name`, { name }),
};

export const familyApi = {
  getAll: () => apiClient.get('/families'),
  getById: (id) => apiClient.get(`/families/${id}`),
  getByName: (name) => apiClient.get(`/families/name/${name}`),
  create: (family) => apiClient.post('/families', family),
  update: (id, family) => apiClient.put(`/families/${id}`, family),
  delete: (id) => apiClient.delete(`/families/${id}`),
  getMembers: (id) => apiClient.get(`/families/${id}/members`),
  addMember: (id, userId, role) => apiClient.post(`/families/${id}/members`, { userId, role }),
  removeMember: (id, userId) => apiClient.delete(`/families/${id}/members/${userId}`),
  updateMemberRole: (id, userId, role) => apiClient.put(`/families/${id}/members/${userId}/role`, { role }),
};

export const wallpaperApi = {
  getAll: () => apiClient.get('/wallpapers'),
  getById: (id) => apiClient.get(`/wallpapers/${id}`),
  getByDeviceId: (deviceId) => apiClient.get(`/wallpapers/device/${deviceId}`),
  create: (wallpaper) => apiClient.post('/wallpapers', wallpaper),
  update: (id, wallpaper) => apiClient.put(`/wallpapers/${id}`, wallpaper),
  delete: (id) => apiClient.delete(`/wallpapers/${id}`),
  select: (deviceId, wallpaperId) => apiClient.put('/wallpapers/select', { deviceId, wallpaperId }),
};

export const themeApi = {
  getAll: () => apiClient.get('/themes'),
  getById: (id) => apiClient.get(`/themes/${id}`),
  getByDeviceId: (deviceId) => apiClient.get(`/themes/device/${deviceId}`),
  create: (theme) => apiClient.post('/themes', theme),
  update: (id, theme) => apiClient.put(`/themes/${id}`, theme),
  delete: (id) => apiClient.delete(`/themes/${id}`),
  select: (deviceId, themeId) => apiClient.put('/themes/select', { deviceId, themeId }),
};

export const simulationApi = {
  loadDummyData: () => apiClient.post('/simulation/load-dummy-data'),
};

export const appApi = {
  getAll: () => apiClient.get('/apps'),
  getById: (id) => apiClient.get(`/apps/${id}`),
  getByName: (name) => apiClient.get(`/apps/name/${name}`),
  create: (app) => apiClient.post('/apps', app),
  delete: (id) => apiClient.delete(`/apps/${id}`),
};

export const iconApi = {
  getAll: () => apiClient.get('/icons'),
  getById: (id) => apiClient.get(`/icons/${id}`),
  getByName: (name) => apiClient.get(`/icons/name/${name}`),
  create: (icon) => apiClient.post('/icons', icon),
  update: (id, icon) => apiClient.put(`/icons/${id}`, icon),
  delete: (id) => apiClient.delete(`/icons/${id}`),
};

export default apiClient;
