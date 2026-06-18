import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const TopNavbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="app-navbar">
      <div></div>
      
      <div className="navbar-actions">
        
        <div className="user-profile">
          <div className="avatar">
            {user?.name ? user.name.charAt(0).toUpperCase() : 'U'}
          </div>
          <span style={{ fontSize: '0.875rem', fontWeight: 500, color: 'var(--text-primary)' }}>
            {user?.name || 'Workspace User'}
          </span>
        </div>
        
        <button 
          onClick={handleLogout} 
          className="btn btn-secondary"
          style={{ padding: '8px 14px', fontSize: '0.8125rem', height: '36px' }}
        >
          Logout
        </button>
      </div>
    </header>
  );
};

export default TopNavbar;
