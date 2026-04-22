import React, { useState, useEffect } from 'react';
import { userApi } from '../../api/apiClient';

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [newUserName, setNewUserName] = useState('');
  const [loading, setLoading] = useState(false);
  const [managingUser, setManagingUser] = useState(null);
  const [editUserName, setEditUserName] = useState('');

  const fetchUsers = async () => {
    try {
      const response = await userApi.getAll();
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleCreateUser = async (e) => {
    e.preventDefault();
    if (!newUserName.trim()) return;

    setLoading(true);
    try {
      await userApi.create({ name: newUserName });
      setNewUserName('');
      fetchUsers();
    } catch (error) {
      console.error('Error creating user:', error);
      alert('Error creating user');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteUser = async (userId) => {
    try {
      await userApi.delete(userId);
      fetchUsers();
    } catch (error) {
      console.error('Error deleting user:', error);
      alert('Error deleting user');
    }
  };

  const handleManageUser = (user) => {
    setManagingUser(user);
    setEditUserName(user.name);
  };

  const handleUpdateUser = async (e) => {
    e.preventDefault();
    if (!editUserName.trim()) return;

    try {
      await userApi.update(managingUser.id, { name: editUserName });
      setManagingUser(null);
      fetchUsers();
    } catch (error) {
      console.error('Error updating user:', error);
      alert('Error updating user');
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">User Management</h1>
      
      <div className="bg-green-50 rounded-lg shadow p-6 mb-6">
        <h2 className="text-xl font-bold mb-4">Create New User</h2>
        <form onSubmit={handleCreateUser} className="flex gap-4">
          <input
            type="text"
            value={newUserName}
            onChange={(e) => setNewUserName(e.target.value)}
            placeholder="User name"
            className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
          >
            {loading ? 'Creating...' : 'Create User'}
          </button>
        </form>
      </div>

      <div className="bg-green-50 rounded-lg shadow p-6">
        <h2 className="text-xl font-bold mb-4">Users ({users.length})</h2>
        {users.length === 0 ? (
          <p className="text-gray-500">No users found</p>
        ) : (
          <div className="grid gap-4">
            {users.map((user) => (
              <div key={user.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                <div>
                  <h3 className="font-bold">{user.name}</h3>
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => handleManageUser(user)}
                    className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                  >
                    Manage
                  </button>
                  <button
                    onClick={() => handleDeleteUser(user.id)}
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

      {managingUser && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-green-50 rounded-lg shadow-xl w-full max-w-md">
            <div className="bg-gray-100 px-6 py-4 border-b flex justify-between items-center">
              <h2 className="text-xl font-bold">Manage User</h2>
              <button
                onClick={() => setManagingUser(null)}
                className="text-gray-500 hover:text-gray-700 text-2xl"
              >
                ×
              </button>
            </div>
            <form onSubmit={handleUpdateUser} className="p-6">
              <div className="mb-4">
                <label className="block text-sm font-medium mb-2">User Name</label>
                <input
                  type="text"
                  value={editUserName}
                  onChange={(e) => setEditUserName(e.target.value)}
                  className="w-full px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div className="flex gap-2 justify-end">
                <button
                  type="button"
                  onClick={() => setManagingUser(null)}
                  className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                >
                  Save
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default UserManagement;
