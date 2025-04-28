import { useState } from 'react';

export default function EmbedButton() {
  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);

  const handleEmbed = async () => {
    setLoading(true);
    setStatus('Embedding...');
    try {
      const res = await fetch('/api/embed', { method: 'POST' });
      const text = await res.text();
      setStatus(text);
    } catch (e) {
      setStatus('Error: ' + e.message);
    }
    setLoading(false);
  };

  return (
    <div style={{ marginBottom: 16 }}>
      <button onClick={handleEmbed} disabled={loading} style={{ padding: '8px 16px' }}>
        {loading ? 'Embedding...' : 'Embed PDF'}
      </button>
      {status && <div style={{ marginTop: 8, color: status.startsWith('Error') ? 'red' : 'green' }}>{status}</div>}
    </div>
  );
}
