import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from '../api/axios';
import './Analytics.css';  // Import custom CSS

function Analytics() {
  const { shortCode } = useParams();
  const [clicks, setClicks] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const res = await axios.get(`https://url-shortener-analytics.onrender.com/api/analytics/${shortCode}`);
        console.log('📊 Analytics API response:', res.data);
        setClicks(res.data);
      } catch (err) {
        setError('Failed to fetch analytics');
      }
    };

    fetchAnalytics();
  }, [shortCode]);

  return (
    <div className="container">
      <h2 className="mb-4">Click Analytics for: <code>{shortCode}</code></h2>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <Link to="/dashboard" className="btn btn-outline-primary">
          ← Back to Dashboard
        </Link>

        {/* CSV Export Button */}
        {clicks.length > 0 && (
          <a
            href={`https://url-shortener-analytics.onrender.com/api/analytics/export/${shortCode}`}
            className="btn btn-success"
            download
          >
            Export CSV
          </a>
        )}
      </div>

      {/* Error Message */}
      {error && <div className="alert alert-danger">{error}</div>}

      {/* Analytics Table */}
      {clicks.length === 0 ? (
        <div className="alert alert-info">No clicks yet.</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-striped table-hover table-bordered">
            <thead>
              <tr>
                <th>#</th>
                <th>IP Address</th>
                <th>Device</th>
                <th>User Agent</th>
                <th>Time</th>
              </tr>
            </thead>
            <tbody>
              {clicks.map((click, index) => (
                <tr key={click._id || index}>
                  <td>{index + 1}</td>
                  <td>{click.ipAddress}</td>
                  <td>{click.deviceType}</td>
                  <td title={click.userAgent}>{click.userAgent}</td>
                  <td>{new Date(click.timestamp).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default Analytics;
