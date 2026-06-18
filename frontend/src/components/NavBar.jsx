import React, { useContext } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const NavBar = () => {
  const { isAuthenticated, user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to={isAuthenticated ? "/dashboard" : "/login"} className="nav-logo" style={{ textDecoration: 'none' }}>
          SmartTask
        </Link>
        
        <ul className="nav-links" style={{ display: 'flex', alignItems: 'center' }}>
          {isAuthenticated ? (
            <>
              <li>
                <NavLink 
                  to="/dashboard" 
                  className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                  style={{ textDecoration: 'none' }}
                >
                  Dashboard
                </NavLink>
              </li>
              <li>
                <NavLink 
                  to="/projects" 
                  className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                  style={{ textDecoration: 'none' }}
                >
                  Projects
                </NavLink>
              </li>
              <li>
                <NavLink 
                  to="/tasks" 
                  className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                  style={{ textDecoration: 'none' }}
                >
                  Tasks
                </NavLink>
              </li>
              {user?.role === 'ROLE_ADMIN' && (
                <li>
                  <NavLink 
                    to="/users" 
                    className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                    style={{ textDecoration: 'none' }}
                  >
                    Users
                  </NavLink>
                </li>
              )}
              {user && (
                <li className="nav-item" style={{ cursor: 'default', color: 'var(--accent-primary)', background: 'transparent', display: 'flex', alignItems: 'center', gap: '5px' }}>
                  {user.name}
                </li>
              )}
              <li>
                <button 
                  onClick={handleLogout} 
                  className="btn btn-danger" 
                  style={{ padding: '8px 16px', cursor: 'pointer', display: 'flex', alignItems: 'center' }}
                >
                  Logout
                </button>
              </li>
            </>
          ) : (
            <>
              <li>
                <NavLink 
                  to="/login" 
                  className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                  style={{ textDecoration: 'none' }}
                >
                  Sign In
                </NavLink>
              </li>
              <li>
                <NavLink 
                  to="/register" 
                  className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                  style={{ textDecoration: 'none' }}
                >
                  Sign Up
                </NavLink>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default NavBar;
