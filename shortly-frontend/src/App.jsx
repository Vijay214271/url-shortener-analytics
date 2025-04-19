import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Landing from './pages/LandingPage';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Analytics from './pages/Analytics';
import Navbar from './components/Navbar';
import Shorten from './pages/Shorten';
import PrivateRoute from './utils/PrivateRoute'; // Auth route guard

// ✅ Wrapper layout for authenticated pages
function ProtectedLayout({ children }) {
  return (
    <>
      <Navbar />
      <div className="container mt-4">
        {children}
      </div>
    </>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes (with Navbar) */}
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <ProtectedLayout>
                <Dashboard />
              </ProtectedLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/shorten"
          element={
            <PrivateRoute>
              <ProtectedLayout>
                <Shorten />
              </ProtectedLayout>
            </PrivateRoute>
          }
        />
        <Route
          path="/analytics/:shortCode"
          element={
            <PrivateRoute>
              <ProtectedLayout>
                <Analytics />
              </ProtectedLayout>
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
