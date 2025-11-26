import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import './index.css';
import Track from './pages/Track';
import { AuthProvider } from './contexts/AuthContext';
import Welcome from './pages/Welcome';
import Login from './pages/Login.jsx'

ReactDOM.createRoot(document.getElementById('root')).render(
  <AuthProvider>

    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Welcome />} />
        <Route path="/track" element={<Track />} />


      </Routes>
    </BrowserRouter>
  </AuthProvider>

);