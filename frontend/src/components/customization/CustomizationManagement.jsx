import React, { useState, useEffect } from 'react';
import { wallpaperApi, themeApi, deviceApi } from '../../api/apiClient';

function CustomizationManagement() {
  const [wallpapers, setWallpapers] = useState([]);
  const [themes, setThemes] = useState([]);
  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [selectedDeviceId, setSelectedDeviceId] = useState('');
  const [activeTab, setActiveTab] = useState('wallpapers');
  const [loading, setLoading] = useState(false);

  const fetchWallpapers = async () => {
    try {
      const response = await wallpaperApi.getAll();
      setWallpapers(response.data);
    } catch (error) {
      console.error('Error fetching wallpapers:', error);
    }
  };

  const fetchThemes = async () => {
    try {
      const response = await themeApi.getAll();
      setThemes(response.data);
    } catch (error) {
      console.error('Error fetching themes:', error);
    }
  };

  const fetchDevices = async () => {
    try {
      const response = await deviceApi.getAll();
      setDevices(response.data);
      if (response.data.length > 0) {
        const firstDeviceId = response.data[0].id;
        setSelectedDeviceId(firstDeviceId);
        // Fetch device details to get current wallpaper/theme for active indicator
        const deviceResponse = await deviceApi.getById(firstDeviceId);
        setSelectedDevice(deviceResponse.data);
      }
    } catch (error) {
      console.error('Error fetching devices:', error);
    }
  };

  useEffect(() => {
    fetchWallpapers();
    fetchThemes();
    fetchDevices();
  }, []);

  const handleDeviceChange = async (deviceId) => {
    setSelectedDeviceId(deviceId);
    if (deviceId) {
      try {
        const [deviceResponse, wallpaperResponse, themeResponse] = await Promise.all([
          deviceApi.getById(deviceId),
          wallpaperApi.getByDeviceId(deviceId),
          themeApi.getByDeviceId(deviceId)
        ]);
        setSelectedDevice(deviceResponse.data);
        setWallpapers(wallpaperResponse.data);
        setThemes(themeResponse.data);
      } catch (error) {
        console.error('Error fetching device-specific data:', error);
      }
    } else {
      setSelectedDevice(null);
    }
  };

  const handleCreateWallpaper = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const name = formData.get('name');
    const imagePath = formData.get('imagePath');

    if (!name.trim() || !imagePath.trim() || !selectedDeviceId) return;

    setLoading(true);
    try {
      await wallpaperApi.create({
        name,
        imagePath,
        deviceId: selectedDeviceId
      });
      e.target.reset();
      fetchWallpapers();
    } catch (error) {
      console.error('Error creating wallpaper:', error);
      alert('Error creating wallpaper');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTheme = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const name = formData.get('name');
    const primaryColor = formData.get('primaryColor');
    const secondaryColor = formData.get('secondaryColor');
    const fontFamily = formData.get('fontFamily');

    if (!name.trim() || !primaryColor.trim() || !secondaryColor.trim() || !fontFamily.trim() || !selectedDeviceId) return;

    setLoading(true);
    try {
      await themeApi.create({
        name,
        primaryColor,
        secondaryColor,
        fontFamily,
        deviceId: selectedDeviceId
      });
      e.target.reset();
      fetchThemes();
    } catch (error) {
      console.error('Error creating theme:', error);
      alert('Error creating theme');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteWallpaper = async (wallpaperId) => {
    if (!window.confirm('Are you sure you want to delete this wallpaper?')) return;

    try {
      await wallpaperApi.delete(wallpaperId);
      fetchWallpapers();
    } catch (error) {
      console.error('Error deleting wallpaper:', error);
      alert('Error deleting wallpaper');
    }
  };

  const handleSelectWallpaper = async (wallpaperId) => {
    if (!selectedDeviceId) return;

    try {
      await wallpaperApi.select(selectedDeviceId, wallpaperId);
      const deviceResponse = await deviceApi.getById(selectedDeviceId);
      setSelectedDevice(deviceResponse.data);
    } catch (error) {
      console.error('Error selecting wallpaper:', error);
      alert('Error selecting wallpaper');
    }
  };

  const handleDeleteTheme = async (themeId) => {
    if (!window.confirm('Are you sure you want to delete this theme?')) return;

    try {
      await themeApi.delete(themeId);
      fetchThemes();
    } catch (error) {
      console.error('Error deleting theme:', error);
      alert('Error deleting theme');
    }
  };

  const handleSelectTheme = async (themeId) => {
    if (!selectedDeviceId) return;

    try {
      await themeApi.select(selectedDeviceId, themeId);
      const deviceResponse = await deviceApi.getById(selectedDeviceId);
      setSelectedDevice(deviceResponse.data);
    } catch (error) {
      console.error('Error selecting theme:', error);
      alert('Error selecting theme');
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Customization Management</h1>
      
      <div className="bg-green-50 rounded-lg shadow p-6 mb-6">
        <h2 className="text-xl font-bold mb-4">Select Device</h2>
        <select
          value={selectedDeviceId}
          onChange={(e) => handleDeviceChange(e.target.value)}
          className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
        >
          <option value="">Select a device</option>
          {devices.map((device) => (
            <option key={device.id} value={device.id}>
              {device.name}
            </option>
          ))}
        </select>
      </div>

      <div className="mb-4">
        <div className="flex gap-4">
          <button
            onClick={() => setActiveTab('wallpapers')}
            className={`px-4 py-2 rounded ${activeTab === 'wallpapers' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
          >
            Wallpapers
          </button>
          <button
            onClick={() => setActiveTab('themes')}
            className={`px-4 py-2 rounded ${activeTab === 'themes' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
          >
            Themes
          </button>
        </div>
      </div>

      {activeTab === 'wallpapers' && (
        <div className="space-y-6">
          <div className="bg-green-50 rounded-lg shadow p-6">
            <h2 className="text-xl font-bold mb-4">Create New Wallpaper</h2>
            <form onSubmit={handleCreateWallpaper} className="space-y-4">
              <input
                name="name"
                type="text"
                placeholder="Wallpaper name"
                className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
              />
              <input
                name="imagePath"
                type="text"
                placeholder="Image path"
                className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
              />
              <button
                type="submit"
                disabled={loading}
                className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
              >
                {loading ? 'Creating...' : 'Create Wallpaper'}
              </button>
            </form>
          </div>

          <div className="bg-green-50 rounded-lg shadow p-6">
            <h2 className="text-xl font-bold mb-4">Wallpapers ({wallpapers.length})</h2>
            {wallpapers.length === 0 ? (
              <p className="text-gray-500">No wallpapers found</p>
            ) : (
              <div className="grid gap-4">
                {wallpapers.map((wallpaper) => (
                  <div key={wallpaper.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                    <div>
                      <h3 className="font-bold">{wallpaper.name}</h3>
                      <p className="text-sm text-gray-500">Path: {wallpaper.imagePath}</p>
                      <div className="flex gap-2 mt-2">
                        {wallpaper.isDefault && <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded">Default</span>}
                        {selectedDevice?.wallpaper?.id === wallpaper.id && <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">Active</span>}
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleSelectWallpaper(wallpaper.id)}
                        disabled={selectedDevice?.wallpaper?.id === wallpaper.id}
                        className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-1 px-4 rounded disabled:bg-gray-400"
                      >
                        {selectedDevice?.wallpaper?.id === wallpaper.id ? 'Selected' : 'Select'}
                      </button>
                      <button
                        onClick={() => alert('Manage wallpaper functionality coming soon')}
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
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}

      {activeTab === 'themes' && (
        <div className="space-y-6">
          <div className="bg-green-50 rounded-lg shadow p-6">
            <h2 className="text-xl font-bold mb-4">Create New Theme</h2>
            <form onSubmit={handleCreateTheme} className="space-y-4">
              <input
                name="name"
                type="text"
                placeholder="Theme name"
                className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
              />
              <input
                name="primaryColor"
                type="text"
                placeholder="Primary color (e.g., #FFFFFF)"
                className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
              />
              <input
                name="secondaryColor"
                type="text"
                placeholder="Secondary color (e.g., #000000)"
                className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
              />
              <input
                name="fontFamily"
                type="text"
                placeholder="Font family (e.g., Arial)"
                className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
              />
              <button
                type="submit"
                disabled={loading}
                className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
              >
                {loading ? 'Creating...' : 'Create Theme'}
              </button>
            </form>
          </div>

          <div className="bg-green-50 rounded-lg shadow p-6">
            <h2 className="text-xl font-bold mb-4">Themes ({themes.length})</h2>
            {themes.length === 0 ? (
              <p className="text-gray-500">No themes found</p>
            ) : (
              <div className="grid gap-4">
                {themes.map((theme) => (
                  <div key={theme.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                    <div>
                      <h3 className="font-bold">{theme.name}</h3>
                      <p className="text-sm text-gray-500">Primary: {theme.primaryColor}</p>
                      <p className="text-sm text-gray-500">Secondary: {theme.secondaryColor}</p>
                      <p className="text-sm text-gray-500">Font: {theme.fontFamily}</p>
                      <div className="flex gap-2 mt-2">
                        {theme.isDefault && <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded">Default</span>}
                        {selectedDevice?.theme?.id === theme.id && <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">Active</span>}
                      </div>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleSelectTheme(theme.id)}
                        disabled={selectedDevice?.theme?.id === theme.id}
                        className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-1 px-4 rounded disabled:bg-gray-400"
                      >
                        {selectedDevice?.theme?.id === theme.id ? 'Selected' : 'Select'}
                      </button>
                      <button
                        onClick={() => alert('Manage theme functionality coming soon')}
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
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default CustomizationManagement;
