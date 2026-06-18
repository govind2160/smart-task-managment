import React, { useContext } from 'react';
import { NavLink } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Sidebar = () => {
  const { user } = useContext(AuthContext);

  return (
    <aside className="app-sidebar">
      <div className="sidebar-logo">

        <span style={{ fontWeight: 700, letterSpacing: '-0.025em' }}>SmartTask</span>
      </div>
      
      <nav className="sidebar-nav">
        <NavLink 
          to="/dashboard" 
          className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
        >

          <span>Dashboard</span>
        </NavLink>
        
        <NavLink 
          to="/projects" 
          className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
        >

          <span>Projects</span>
        </NavLink>
        
        <NavLink 
          to="/tasks" 
          className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
        >

          <span>Tasks</span>
        </NavLink>
        
        <NavLink 
          to="/users" 
          className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
        >

          <span>Users</span>
        </NavLink>
      </nav>
      
      <div className="sidebar-footer">
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <div className="avatar">
            {user?.name ? user.name.charAt(0).toUpperCase() : 'U'}
          </div>
          <div style={{ overflow: 'hidden' }}>
            <div style={{ fontSize: '0.8125rem', fontWeight: 600, color: 'var(--text-primary)', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
              {user?.name || 'User Profile'}
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
              {user?.role === 'ROLE_ADMIN' ? 'Administrator' : 'Team Member'}
            </div>
          </div>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
