// src/pages/Landing.jsx
import { Link } from 'react-router-dom';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Landing() {
    const navigate = useNavigate();

    useEffect(() => {
      const token = localStorage.getItem('token');
      if (token) navigate('/dashboard');
    }, []);
  return (
    <div className="d-flex flex-column justify-content-center align-items-center vh-100 text-center bg-light">
      <h1 className="display-4 mb-3">Welcome to <span className="text-primary">Shortly</span> 🚀</h1>
      <p className="lead mb-4">Shorten your links. Track every click. Get real-time analytics.</p>
      <div>
        <Link to="/login" className="btn btn-primary me-2">Login</Link>
        <Link to="/register" className="btn btn-outline-primary">Register</Link>
      </div>
    </div>
  );
}

export default Landing;
