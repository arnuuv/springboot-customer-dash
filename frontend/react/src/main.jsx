import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './components/context/AuthContext.jsx'
import ProtectedRoute from './components/shared/ProtectedRoute.jsx'
import SideBar from './components/shared/SideBar.jsx'
import Home from './Home.jsx'
import Customer from './Customer.jsx'
import Login from './components/login/Login.jsx'
import Signup from './components/signup/Signup.jsx'
import CustomerExportImport from './components/export-import/CustomerExportImport.jsx'
import CustomerActivityTimeline from './components/activity/CustomerActivityTimeline.jsx'
import CustomerNotificationCenter from './components/notification/CustomerNotificationCenter.jsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider>
      <BrowserRouter>
        <div className="app">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/" element={
              <ProtectedRoute>
                <SideBar />
                <div className="main-content">
                  <Routes>
                    <Route index element={<Home />} />
                    <Route path="customers" element={<Customer />} />
                    <Route path="export-import" element={<CustomerExportImport />} />
                    <Route path="activities" element={<CustomerActivityTimeline />} />
                    <Route path="notifications" element={<CustomerNotificationCenter />} />
                  </Routes>
                </div>
              </ProtectedRoute>
            } />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  </React.StrictMode>,
)
