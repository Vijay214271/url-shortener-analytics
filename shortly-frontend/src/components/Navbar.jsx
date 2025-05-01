import { Link, useNavigate } from 'react-router-dom';
import './Navbar.css'; // ⬅️ Add this

function Navbar() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <nav className="custom-navbar">
      <Link className="brand" to="/dashboard">Shortly</Link>
      <div className="nav-links">
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/shorten">Shorten</Link>
        <button onClick={logout}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;
