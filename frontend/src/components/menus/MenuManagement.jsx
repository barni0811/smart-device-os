import React, { useState, useEffect } from 'react';
import { menuApi, deviceApi } from '../../api/apiClient';

function MenuManagement() {
  const [menus, setMenus] = useState([]);
  const [devices, setDevices] = useState([]);
  const [selectedDeviceId, setSelectedDeviceId] = useState('');
  const [menuName, setMenuName] = useState('');
  const [loading, setLoading] = useState(false);
  const [managingMenu, setManagingMenu] = useState(null);

  const fetchMenus = async () => {
    try {
      const response = await menuApi.getAll();
      setMenus(response.data);
    } catch (error) {
      console.error('Error fetching menus:', error);
    }
  };

  const fetchDevices = async () => {
    try {
      const response = await deviceApi.getAll();
      setDevices(response.data);
      if (response.data.length > 0) {
        setSelectedDeviceId(response.data[0].id);
      }
    } catch (error) {
      console.error('Error fetching devices:', error);
    }
  };

  useEffect(() => {
    fetchMenus();
    fetchDevices();
  }, []);

  const handleCreateMenu = async (e) => {
    e.preventDefault();
    if (!menuName.trim() || !selectedDeviceId) return;

    setLoading(true);
    try {
      await menuApi.create({
        name: menuName,
        device: { id: selectedDeviceId }
      });
      setMenuName('');
      fetchMenus();
    } catch (error) {
      console.error('Error creating menu:', error);
      alert('Error creating menu');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteMenu = async (menuId) => {
    if (!window.confirm('Are you sure you want to delete this menu?')) return;

    try {
      await menuApi.delete(menuId);
      fetchMenus();
    } catch (error) {
      console.error('Error deleting menu:', error);
      alert('Error deleting menu');
    }
  };

  const handleManageMenu = (menu) => {
    setManagingMenu(menu);
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Menu Management</h1>
      
      <div className="bg-green-50 rounded-lg shadow p-6 mb-6">
        <h2 className="text-xl font-bold mb-4">Create New Menu</h2>
        <form onSubmit={handleCreateMenu} className="flex gap-4">
          <select
            value={selectedDeviceId}
            onChange={(e) => setSelectedDeviceId(e.target.value)}
            className="px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
          >
            {devices.map((device) => (
              <option key={device.id} value={device.id}>
                {device.name}
              </option>
            ))}
          </select>
          <input
            type="text"
            value={menuName}
            onChange={(e) => setMenuName(e.target.value)}
            placeholder="Menu name"
            className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
          >
            {loading ? 'Creating...' : 'Create Menu'}
          </button>
        </form>
      </div>

      <div className="bg-green-50 rounded-lg shadow p-6">
        <h2 className="text-xl font-bold mb-4">Menus ({menus.length})</h2>
        {menus.length === 0 ? (
          <p className="text-gray-500">No menus found</p>
        ) : (
          <div className="grid gap-4">
            {menus.map((menu) => (
              <div key={menu.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                <div>
                  <h3 className="font-bold">{menu.name}</h3>
                  {menu.device && <p className="text-sm text-gray-500">Device: {menu.device.name}</p>}
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

      {managingMenu && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-green-50 rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-hidden">
            <div className="bg-gray-100 px-6 py-4 border-b flex justify-between items-center">
              <h2 className="text-xl font-bold">Managing Menu: {managingMenu.name}</h2>
              <button
                onClick={() => setManagingMenu(null)}
                className="text-gray-500 hover:text-gray-700 text-2xl"
              >
                ×
              </button>
            </div>
            <div className="p-6 overflow-y-auto max-h-[calc(90vh-80px)]">
              <h3 className="font-bold mb-4">Menu Items</h3>
              <p className="text-gray-500">Menu item management for {managingMenu.name} coming soon.</p>
              <p className="text-sm text-gray-400 mt-2">This will allow adding/removing menu items, submenus, and applications.</p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default MenuManagement;
