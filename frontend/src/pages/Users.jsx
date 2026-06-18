import React, { useState, useEffect } from 'react';
import { userApi } from '../services/api';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchUsers = () => {
    setLoading(true);
    userApi.getUsers()
      .then(response => {
        setUsers(response.data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error fetching users:', err);
        setError('Failed to fetch workspace users. Make sure your backend connection is active.');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleDelete = (id) => {
    if (!window.confirm('Are you sure you want to delete this user?')) return;

    userApi.deleteUser(id)
      .then(() => {
        fetchUsers();
      })
      .catch(err => {
        console.error('Error deleting user:', err);
        alert('Failed to delete user: ' + (err.response?.data?.message || err.message));
      });
  };

  const getRoleBadgeClass = (role) => {
    return role === 'ROLE_ADMIN' ? 'status-active' : 'status-planned';
  };

  return (
    <div className="container">
      <div style={{ marginBottom: '32px' }}>
        <h1>Users Management</h1>
        <p className="subtitle">View and manage members of your workspace</p>
      </div>

      {error && (
        <div className="card" style={{ borderColor: 'var(--danger)', backgroundColor: 'var(--danger-light)', marginBottom: '24px', padding: '16px' }}>
          <p style={{ color: 'var(--danger)', fontWeight: 500, fontSize: '0.875rem' }}>{error}</p>
        </div>
      )}

      <div className="card">
        <h2 style={{ fontSize: '1rem', marginBottom: '20px' }}>Workspace Members</h2>
        
        {loading ? (
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Loading workspace members...</p>
        ) : users.length === 0 ? (
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>No members found in this workspace.</p>
        ) : (
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>Member Name</th>
                  <th>Email Address</th>
                  <th>Role</th>
                  <th style={{ textAlign: 'right' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map(user => (
                  <tr key={user.id}>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        <div className="avatar" style={{ width: '32px', height: '32px', fontSize: '0.75rem', margin: 0 }}>
                          {user.name.charAt(0).toUpperCase()}
                        </div>
                        <div style={{ fontWeight: 600, color: 'var(--primary)', fontSize: '0.9rem' }}>
                          {user.name}
                        </div>
                      </div>
                    </td>
                    <td>
                      <span style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
                        {user.email}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${getRoleBadgeClass(user.role)}`}>
                        {user.role === 'ROLE_ADMIN' ? 'Administrator' : 'Team Member'}
                      </span>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <button 
                        className="btn btn-danger-outline"
                        onClick={() => handleDelete(user.id)}
                        style={{ padding: '6px 12px', fontSize: '0.75rem' }}
                      >
                        Remove
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default Users;
