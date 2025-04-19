import { Navigate } from 'react-router-dom';

// Wrapper that checks if user is authenticated
function PrivateRoute({ children }) {
  const token = localStorage.getItem('token');

  if (!token) {
    // 🚫 No token? Redirect to login
    return <Navigate to="/login" replace />;
  }

  // ✅ Token exists, allow route
  return children;
}

export default PrivateRoute;
