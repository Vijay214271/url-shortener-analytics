// src/pages/Landing.jsx
import { Link, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import './Landing.css';

function Landing() {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) navigate('/dashboard');
  }, []);

  return (
    <div className="landing-container">
      <div className="landing-overlay" />
      <div className="landing-content fade-in">
        <h1 className="landing-title">
          Welcome to <span className="brand-name">Shortly</span> 🚀
        </h1>
        <p className="landing-subtitle">
          Transform long links into smart, trackable URLs in seconds.
        </p>
        <div className="landing-buttons">
          <Link to="/login" className="btn btn-primary">Login</Link>
          <Link to="/register" className="btn btn-outline-light ms-2">Register</Link>
        </div>
      </div>
    </div>
  );
}

export default Landing;
