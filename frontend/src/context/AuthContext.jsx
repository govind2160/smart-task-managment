import React, { createContext, useState, useEffect } from 'react';
import { authApi } from '../services/api';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedId = localStorage.getItem('user_id');
    const storedEmail = localStorage.getItem('user_email');
    const storedName = localStorage.getItem('user_name');
    const storedRole = localStorage.getItem('user_role');

    if (storedToken && storedEmail) {
      setUser({
        id: storedId ? parseInt(storedId, 10) : null,
        email: storedEmail,
        name: storedName,
        role: storedRole
      });
      setToken(storedToken);
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    setLoading(true);
    try {
      const response = await authApi.login({ email, password });
      const { id, token: jwt, email: userEmail, name: userName, role: userRole } = response.data;
      
      localStorage.setItem('token', jwt);
      localStorage.setItem('user_id', id);
      localStorage.setItem('user_email', userEmail || email);
      localStorage.setItem('user_name', userName || 'User');
      localStorage.setItem('user_role', userRole || 'ROLE_USER');

      setUser({ 
        id,
        email: userEmail || email, 
        name: userName || 'User', 
        role: userRole || 'ROLE_USER' 
      });
      setToken(jwt);
      setLoading(false);
      return response.data;
    } catch (error) {
      setLoading(false);
      throw error;
    }
  };

  const register = async (name, email, password, role = 'ROLE_USER') => {
    try {
      const response = await authApi.register({ name, email, password, role });
      const { id, token: jwt, email: userEmail, name: userName, role: userRole } = response.data;

      localStorage.setItem('token', jwt);
      localStorage.setItem('user_id', id);
      localStorage.setItem('user_email', userEmail || email);
      localStorage.setItem('user_name', userName || name);
      localStorage.setItem('user_role', userRole || role);

      // Update state immediately without loading state
      setUser({ 
        id,
        email: userEmail || email, 
        name: userName || name, 
        role: userRole || role 
      });
      setToken(jwt);
      return response.data;
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user_id');
    localStorage.removeItem('user_email');
    localStorage.removeItem('user_name');
    localStorage.removeItem('user_role');
    setUser(null);
    setToken(null);
  };

  const value = {
    token,
    user,
    isAuthenticated: !!token,
    loading,
    login,
    register,
    logout
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
