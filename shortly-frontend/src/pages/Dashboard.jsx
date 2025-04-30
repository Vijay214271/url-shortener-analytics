import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function Dashboard() {
  const [urls, setUrls] = useState([]);
  const BACKEND_BASE_URL=import.meta.env.VITE_BACKEND_URL;

  useEffect(() => {
    const fetchUrls = async () => {
      const token = localStorage.getItem("token");
      console.log("📦 Stored Token:", token);
  
      try {
        const res = await axios.get('http://localhost:8080/api/urls', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        console.log("🌐 API response:", res.data);
        setUrls(Array.isArray(res.data) ? res.data : []);
      } catch (err) {
        console.error("🚨 Fetch Error:", err);
      }
    };
    fetchUrls();
  }, []);

  const deleteUrl = async(shortCode) =>{
    const token = localStorage.getItem("token");
    if (!window.confirm("Are you sure you want to delete this URL?")) return;

    try {
      await axios.delete(`http://localhost:8080/api/urls/${shortCode}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      alert("URL deleted successfully!");
      setUrls(prev => prev.filter(url => url.shortCode !== shortCode));
    } catch (err) {
      console.error("❌ Delete error:", err);
      alert("Failed to delete URL.");
    }
  }
  


  
  const copyToClipboard = (shortUrl) => {
    navigator.clipboard.writeText(shortUrl);
    alert('Short URL copied to clipboard!');
  };

  return (
    <div className="container mt-5">
      <h2>Your Shortened URLs</h2>
      <div className="list-group mt-4">
        {urls.map((url) => (
          <div key={url.shortCode} className="list-group-item list-group-item-action d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center">
            <div className="mb-2 mb-md-0">
              <div><strong>Original:</strong> {url.originalUrl}</div>
              <div>
                <strong>Short:</strong> 
                <a href={`${BACKEND_BASE_URL}/${url.shortCode}`} target="_blank" rel="noreferrer" className="ms-2">
                  {BACKEND_BASE_URL}/{url.shortCode}
                </a>
              </div>
            </div>
            <div className="d-flex flex-column flex-md-row gap-2">
              <button className="btn btn-sm btn-outline-primary" onClick={() => copyToClipboard(`${window.location.origin}/${url.shortCode}`)}>
                📋 Copy
              </button>
              <Link to={`/analytics/${url.shortCode}`} className="btn btn-sm btn-outline-secondary">
                📈 View Analytics
              </Link>
              <button className="btn btn-sm btn-outline-danger" onClick={() => deleteUrl(url.shortCode)}>
              🗑️ Delete
            </button>
            </div>
          </div>
        ))}
        {urls.length === 0 && (
          <div className="text-muted mt-3">No URLs shortened yet.</div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
