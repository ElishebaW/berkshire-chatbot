import { useState } from 'react';

export default function ChatWindow() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim()) return;
    const userMsg = { sender: 'user', text: input };
    setMessages(msgs => [...msgs, userMsg]);
    setLoading(true);
    try {
      const res = await fetch('/api/ask', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ question: input }),
      });
      const text = await res.text();
      setMessages(msgs => [...msgs, { sender: 'bot', text }]);
    } catch (e) {
      setMessages(msgs => [...msgs, { sender: 'bot', text: 'Error: ' + e.message }]);
    }
    setInput('');
    setLoading(false);
  };

  return (
    <div style={{ border: '1px solid #ccc', borderRadius: 8, padding: 16, minHeight: 300 }}>
      <div style={{ marginBottom: 12, maxHeight: 200, overflowY: 'auto' }}>
        {messages.map((msg, i) => (
          <div key={i} style={{ textAlign: msg.sender === 'user' ? 'right' : 'left', margin: '6px 0' }}>
            <span style={{ background: msg.sender === 'user' ? '#e0e7ff' : '#f3f4f6', padding: '6px 12px', borderRadius: 6 }}>
              {msg.text}
            </span>
          </div>
        ))}
      </div>
      <div style={{ display: 'flex', gap: 8 }}>
        <input
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' ? sendMessage() : null}
          style={{ flex: 1, padding: 8, borderRadius: 6, border: '1px solid #bbb' }}
          placeholder="Ask a question..."
          disabled={loading}
        />
        <button onClick={sendMessage} disabled={loading || !input.trim()} style={{ padding: '8px 16px' }}>
          {loading ? '...' : 'Send'}
        </button>
      </div>
    </div>
  );
}
