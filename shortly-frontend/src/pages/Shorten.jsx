import './Shorten.css'; // Don't forget to import
import { useState } from 'react';
import axios from '../api/axios';
import { useNavigate, Link } from 'react-router-dom';

function Shorten() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ originalUrl: '', expirationDate: '' });
  const [shortUrl, setShortUrl] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('/api/shorten', form, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      setShortUrl(res.data.shortUrl);
      setForm({ originalUrl: '', expirationDate: '' });
      setError('');
    } catch (err) {
      setError('Failed to shorten URL. Please try again.');
    }
  };

  return (
    <div className="page-overlay">
      <div className="card-glass">
        <h2>Create Short URL</h2>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleSubmit}>
          <input
            className="form-control"
            placeholder="Enter original URL"
            name="originalUrl"
            value={form.originalUrl}
            onChange={handleChange}
            required
          />
          <input
            className="form-control"
            placeholder="Expiration Date (optional)"
            name="expirationDate"
            type="datetime-local"
            value={form.expirationDate}
            onChange={handleChange}
          />
          <button className="btn btn-primary">Shorten</button>
          <Link to="/dashboard" className="btn btn-link">← Back</Link>
        </form>

        {shortUrl && (
          <div className="alert alert-success mt-3">
            Your short URL: <a href={shortUrl} target="_blank" rel="noopener noreferrer">{shortUrl}</a>
          </div>
        )}
      </div>
    </div>
  );
}

export default Shorten;
