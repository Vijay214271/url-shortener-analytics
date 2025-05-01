import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './Dashboard.css'; // ⬅️ Add this

function Dashboard() {
  const [urls, setUrls] = useState([]);
  const BACKEND_BASE_URL = import.meta.env.VITE_BACKEND_URL;

  useEffect(() => {
    const fetchUrls = async () => {
      const token = localStorage.getItem("token");
      try {
        const res = await axios.get('https://url-shortener-analytics.onrender.com/api/urls', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setUrls(Array.isArray(res.data) ? res.data : []);
      } catch (err) {
        console.error("Fetch error:", err);
      }
    };
    fetchUrls();
  }, []);

  const deleteUrl = async (shortCode) => {
    const token = localStorage.getItem("token");
    if (!window.confirm("Delete this URL?")) return;
    try {
      await axios.delete(`https://url-shortener-analytics.onrender.com/api/urls/${shortCode}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUrls(prev => prev.filter(url => url.shortCode !== shortCode));
    } catch (err) {
      console.error("Delete error:", err);
      alert("Failed to delete URL.");
    }
  };

  const copyToClipboard = (shortUrl) => {
    navigator.clipboard.writeText(shortUrl);
    alert('Short URL copied!');
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-card">
        <h2>Your Shortened URLs</h2>
        <div className="url-list">
          {urls.map((url) => (
            <div key={url.shortCode} className="url-item">
              <div className="url-text">
                <div><strong>Original:</strong> {url.originalUrl}</div>
                <div>
                  <strong>Short:</strong>
                  <a href={`${BACKEND_BASE_URL}/${url.shortCode}`} target="_blank" rel="noreferrer">
                    {BACKEND_BASE_URL}/{url.shortCode}
                  </a>
                </div>
              </div>
              <div className="url-actions">
                <button onClick={() => copyToClipboard(`${window.location.origin}/${url.shortCode}`)}>📋 Copy</button>
                <Link to={`/analytics/${url.shortCode}`} className="btn-secondary">📈 Analytics</Link>
                <button className="btn-danger" onClick={() => deleteUrl(url.shortCode)}>🗑️ Delete</button>
              </div>
            </div>
          ))}
          {urls.length === 0 && (
            <div className="text-muted mt-3">No URLs shortened yet.</div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
