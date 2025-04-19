// src/pages/Analytics.jsx
import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from '../api/axios';

function Analytics() {
  const { shortCode } = useParams();
  const [clicks, setClicks] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/analytics/daily/${shortCode}`);
        console.log('📊 Analytics API response:', res.data);
        setClicks(res.data);
      } catch (err) {
        setError('Failed to fetch analytics');
      }
    };

    fetchAnalytics();
  }, [shortCode]);

  return (
    <div className="container mt-5">
      <h2>Click Analytics for: <code>{shortCode}</code></h2>
      <Link to="/dashboard" className="btn btn-link mb-3">← Back to Dashboard</Link>

      {error && <div className="alert alert-danger">{error}</div>}

      {Object.entries(clicks).length === 0 ? (
        <div>No clicks yet.</div>
      ) : (
        <table className="table table-bordered">
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
            {Object.entries(clicks).map((click, index) => (
              <tr key={click.id}>
                <td>{index + 1}</td>
                <td>{click.ipAddress}</td>
                <td>{click.deviceType}</td>
                <td>{click.userAgent}...</td>
                <td>{new Date(click.timestamp).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Analytics;
