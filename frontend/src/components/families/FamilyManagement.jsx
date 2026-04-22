import React, { useState, useEffect } from 'react';
import { familyApi, userApi } from '../../api/apiClient';

function FamilyManagement() {
  const [families, setFamilies] = useState([]);
  const [familyName, setFamilyName] = useState('');
  const [loading, setLoading] = useState(false);
  const [managingFamily, setManagingFamily] = useState(null);
  const [users, setUsers] = useState([]);

  const fetchFamilies = async () => {
    try {
      const response = await familyApi.getAll();
      setFamilies(response.data);
    } catch (error) {
      console.error('Error fetching families:', error);
    }
  };

  const fetchUsers = async () => {
    try {
      const response = await userApi.getAll();
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  useEffect(() => {
    fetchFamilies();
    fetchUsers();
  }, []);

  const handleCreateFamily = async (e) => {
    e.preventDefault();
    if (!familyName.trim()) return;

    setLoading(true);
    try {
      await familyApi.create({ name: familyName });
      setFamilyName('');
      fetchFamilies();
    } catch (error) {
      console.error('Error creating family:', error);
      alert('Error creating family');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteFamily = async (familyId) => {
    try {
      await familyApi.delete(familyId);
      fetchFamilies();
    } catch (error) {
      console.error('Error deleting family:', error);
      alert('Error deleting family');
    }
  };

  const [familyMembers, setFamilyMembers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState('');
  const [selectedRole, setSelectedRole] = useState('child');
  const [editFamilyName, setEditFamilyName] = useState('');

  const handleManageFamily = async (family) => {
    setManagingFamily(family);
    setEditFamilyName(family.name);
    fetchFamilyMembers(family.id);
  };

  const handleUpdateFamily = async (e) => {
    e.preventDefault();
    if (!managingFamily || !editFamilyName.trim()) return;
    try {
      await familyApi.update(managingFamily.id, { name: editFamilyName });
      setManagingFamily({ ...managingFamily, name: editFamilyName });
      fetchFamilies();
    } catch (error) {
      console.error('Error updating family:', error);
      alert('Error updating family');
    }
  };

  const fetchFamilyMembers = async (familyId) => {
    try {
      const response = await familyApi.getMembers(familyId);
      setFamilyMembers(response.data);
    } catch (error) {
      console.error('Error fetching family members:', error);
    }
  };

  const handleAddMember = async (e) => {
    e.preventDefault();
    if (!selectedUserId || !managingFamily) return;
    try {
      await familyApi.addMember(managingFamily.id, selectedUserId, selectedRole);
      setSelectedUserId('');
      setSelectedRole('child');
      fetchFamilyMembers(managingFamily.id);
    } catch (error) {
      console.error('Error adding family member:', error);
      alert('Error adding family member: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleRemoveMember = async (userId) => {
    if (!managingFamily) return;
    try {
      await familyApi.removeMember(managingFamily.id, userId);
      fetchFamilyMembers(managingFamily.id);
    } catch (error) {
      console.error('Error removing family member:', error);
      alert('Error removing family member: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleUpdateRole = async (userId, newRole) => {
    if (!managingFamily) return;
    try {
      await familyApi.updateMemberRole(managingFamily.id, userId, newRole);
      fetchFamilyMembers(managingFamily.id);
    } catch (error) {
      console.error('Error updating member role:', error);
      alert('Error updating member role');
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Family Management</h1>
      
      <div className="bg-green-50 rounded-lg shadow p-6 mb-6">
        <h2 className="text-xl font-bold mb-4">Create New Family</h2>
        <form onSubmit={handleCreateFamily} className="flex gap-4">
          <input
            type="text"
            value={familyName}
            onChange={(e) => setFamilyName(e.target.value)}
            placeholder="Family name"
            className="flex-1 px-4 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
          >
            {loading ? 'Creating...' : 'Create Family'}
          </button>
        </form>
      </div>

      <div className="bg-green-50 rounded-lg shadow p-6">
        <h2 className="text-xl font-bold mb-4">Families ({families.length})</h2>
        {families.length === 0 ? (
          <p className="text-gray-500">No families found</p>
        ) : (
          <div className="grid gap-4">
            {families.map((family) => (
              <div key={family.id} className="flex justify-between items-center p-4 border rounded-lg hover:bg-gray-50">
                <div>
                  <h3 className="font-bold">{family.name}</h3>
                  {family.owner && <p className="text-sm text-gray-500">Owner: {family.owner.name}</p>}
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => handleManageFamily(family)}
                    className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-4 rounded"
                  >
                    Manage
                  </button>
                  <button
                    onClick={() => handleDeleteFamily(family.id)}
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

      {managingFamily && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-green-50 rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-hidden">
            <div className="bg-gray-100 px-6 py-4 border-b flex justify-between items-center">
              <h2 className="text-xl font-bold">Managing Family: {managingFamily.name}</h2>
              <button
                onClick={() => setManagingFamily(null)}
                className="text-gray-500 hover:text-gray-700 text-2xl"
              >
                ×
              </button>
            </div>
            <div className="p-6 overflow-y-auto max-h-[calc(90vh-80px)]">
              {/* Family Name Edit */}
              <form onSubmit={handleUpdateFamily} className="mb-8">
                <h4 className="font-bold mb-3">Family Name</h4>
                <div className="flex gap-2">
                  <input
                    type="text"
                    value={editFamilyName}
                    onChange={(e) => setEditFamilyName(e.target.value)}
                    className="flex-1 px-3 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    required
                  />
                  <button
                    type="submit"
                    className="bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                  >
                    Save
                  </button>
                </div>
              </form>

              {/* Add Member Form */}
              <form onSubmit={handleAddMember} className="mb-8">
                <h4 className="font-bold mb-3">Add New Member</h4>
                <div className="flex gap-2 mb-3">
                  <select
                    value={selectedUserId}
                    onChange={(e) => setSelectedUserId(e.target.value)}
                    className="flex-1 px-3 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                    required
                  >
                    <option value="">Select User</option>
                    {users
                      .filter(user => !familyMembers.some(member => member.user?.id === user.id))
                      .map(user => (
                        <option key={user.id} value={user.id}>{user.name}</option>
                      ))}
                  </select>
                  <select
                    value={selectedRole}
                    onChange={(e) => setSelectedRole(e.target.value)}
                    className="px-3 py-2 border rounded-lg bg-green-50 focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="parent">Parent</option>
                    <option value="child">Child</option>
                  </select>
                </div>
                <button
                  type="submit"
                  className="w-full bg-emerald-500 hover:bg-emerald-600 text-white font-bold py-2 px-4 rounded"
                >
                  Add Member
                </button>
              </form>

              {/* Members List */}
              <div>
                <h4 className="font-bold mb-3">Family Members</h4>
                {familyMembers.length === 0 ? (
                  <p className="text-gray-500">No members in this family yet.</p>
                ) : (
                  <div className="space-y-2">
                    {familyMembers.map((member) => (
                      <div key={member.user?.id || member.id} className="flex justify-between items-center p-3 border rounded-lg">
                        <div>
                          <span className="font-medium">{member.user?.name || 'Unknown'}</span>
                          <span className="text-sm text-gray-500 ml-2">
                            <select
                              value={member.role}
                              onChange={(e) => handleUpdateRole(member.user?.id, e.target.value)}
                              className="px-2 py-1 text-sm border rounded focus:outline-none focus:ring-2 focus:ring-green-500"
                            >
                              <option value="parent">Parent</option>
                              <option value="child">Child</option>
                            </select>
                          </span>
                        </div>
                        <button
                          onClick={() => handleRemoveMember(member.user?.id)}
                          disabled={member.role === 'owner' || managingFamily?.owner?.id === member.user?.id}
                          className="text-red-500 hover:text-red-700 font-medium text-sm disabled:text-gray-400"
                        >
                          Remove
                        </button>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default FamilyManagement;
