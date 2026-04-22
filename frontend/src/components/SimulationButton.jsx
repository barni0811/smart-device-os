import React, { useState } from 'react';
import { simulationApi } from '../api/apiClient';

function SimulationButton() {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleLoadDummyData = async () => {
    setLoading(true);
    setMessage('');
    try {
      const response = await simulationApi.loadDummyData();
      setMessage(response.data);
    } catch (error) {
      setMessage('Error loading dummy data: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-green-50 rounded-lg shadow p-6">
      <h2 className="text-2xl font-bold mb-4">Data Simulation</h2>
      <p className="text-gray-600 mb-4">
        Load dummy data into the application including families, users, devices, menus, wallpapers, and themes.
      </p>
      <button
        onClick={handleLoadDummyData}
        disabled={loading}
        className="bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-6 rounded-lg disabled:bg-gray-400"
      >
        {loading ? 'Loading...' : 'Load Dummy Data'}
      </button>
      {message && (
        <div className={`mt-4 p-4 rounded ${message.includes('Error') ? 'bg-red-100 text-red-700' : 'bg-green-100 text-green-700'}`}>
          {message}
        </div>
      )}
    </div>
  );
}

export default SimulationButton;
