import React, { useState } from 'react';
import SimulationButton from './components/SimulationButton';
import UserManagement from './components/users/UserManagement';
import DeviceManagement from './components/devices/DeviceManagement';
import FamilyManagement from './components/families/FamilyManagement';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return (
          <div className="p-6">
            <h1 className="text-3xl font-bold mb-6">Smart Device OS Dashboard</h1>
            <SimulationButton />
          </div>
        );
      case 'users':
        return <UserManagement />;
      case 'devices':
        return <DeviceManagement />;
      case 'families':
        return <FamilyManagement />;
      default:
        return <div className="p-6">Select a tab</div>;
    }
  };

  return (
    <div className="min-h-screen bg-green-100">
      <nav className="bg-green-700 text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold">Smart Device OS</h1>
          <div className="space-x-4">
            <button
              onClick={() => setActiveTab('dashboard')}
              className={`px-4 py-2 rounded ${activeTab === 'dashboard' ? 'bg-green-950' : 'hover:bg-green-700'}`}
            >
              Dashboard
            </button>
            <button
              onClick={() => setActiveTab('users')}
              className={`px-4 py-2 rounded ${activeTab === 'users' ? 'bg-green-950' : 'hover:bg-green-700'}`}
            >
              Users
            </button>
            <button
              onClick={() => setActiveTab('families')}
              className={`px-4 py-2 rounded ${activeTab === 'families' ? 'bg-green-950' : 'hover:bg-green-700'}`}
            >
              Families
            </button>
            <button
              onClick={() => setActiveTab('devices')}
              className={`px-4 py-2 rounded ${activeTab === 'devices' ? 'bg-green-950' : 'hover:bg-green-700'}`}
            >
              Devices
            </button>
          </div>
        </div>
      </nav>
      <main className="container mx-auto py-6">
        {renderContent()}
      </main>
    </div>
  );
}

export default App;
