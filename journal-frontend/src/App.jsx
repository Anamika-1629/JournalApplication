import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import { UserProvider } from './contexts/UserContext'
import { ToastProvider } from './contexts/ToastContext'

import Navbar from './components/layout/Navbar'
import Footer from './components/layout/Footer'
import ProtectedRoute from './components/layout/ProtectedRoute'
import ToastContainer from './components/ui/Toast'

import Landing from './pages/Landing'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import CreateEntry from './pages/CreateEntry'
import EditEntry from './pages/EditEntry'
import ViewEntry from './pages/ViewEntry'
import Profile from './pages/Profile'
import NotFound from './pages/NotFound'

function AppLayout({ children }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Navbar />
      <main style={{ flex: 1 }}>
        {children}
      </main>
      <Footer />
    </div>
  )
}

export default function App() {
  return (
    <ToastProvider>
      <AuthProvider>
        <UserProvider>
          <BrowserRouter>
            <ToastContainer />
            <AppLayout>
              <Routes>
                {/* Public routes */}
                <Route path="/" element={<Landing />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* Protected routes */}
                <Route
                  path="/dashboard"
                  element={
                    <ProtectedRoute>
                      <Dashboard />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/journal/new"
                  element={
                    <ProtectedRoute>
                      <CreateEntry />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/journal/:id"
                  element={
                    <ProtectedRoute>
                      <ViewEntry />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/journal/edit/:id"
                  element={
                    <ProtectedRoute>
                      <EditEntry />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/profile"
                  element={
                    <ProtectedRoute>
                      <Profile />
                    </ProtectedRoute>
                  }
                />

                {/* Catch-all */}
                <Route path="*" element={<NotFound />} />
              </Routes>
            </AppLayout>
          </BrowserRouter>
        </UserProvider>
      </AuthProvider>
    </ToastProvider>
  )
}
