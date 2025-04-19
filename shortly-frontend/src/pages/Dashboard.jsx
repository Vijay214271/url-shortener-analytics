import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function Dashboard() {
  const [urls, setUrls] = useState([]);

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
                <a href={url.shortCode} target="_blank" rel="noreferrer" className="ms-2">
                  {window.location.origin}/{url.shortCode}
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
