import { StrictMode, useState, useEffect } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import About from './About.jsx'
import MobileApp from './MobileApp.jsx'
import MobileAbout from './MobileAbout.jsx'
import Login from './Login.jsx'

function isMobileDevice() {
  if (typeof navigator === 'undefined' || typeof window === 'undefined') return false
  return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ||
    (window.innerWidth > 0 && window.innerWidth < 768)
}

function RootOrRedirect() {
  const [mobile, setMobile] = useState(null)
  useEffect(() => {
    setMobile(isMobileDevice())
  }, [])
  if (mobile === null) return null
  if (mobile) return <Navigate to="/mobile" replace />
  return <App />
}

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RootOrRedirect />} />
        <Route path="/about" element={<About />} />
        <Route path="/login" element={<Login />} />
        <Route path="/mobile" element={<MobileApp />} />
        <Route path="/mobile/about" element={<MobileAbout />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
