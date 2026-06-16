import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import TopNavbar from './TopNavbar';

const Layout = () => {
  return (
    <div className="app-layout">
      {/* Fixed Left Sidebar */}
      <Sidebar />
      
      {/* Main Panel Content Area */}
      <div className="app-main">
        {/* Sticky Header Topbar */}
        <TopNavbar />
        
        {/* Workspace Route Page Content */}
        <main className="app-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
