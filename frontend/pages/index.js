import ChatWindow from '../components/ChatWindow';
import EmbedButton from '../components/EmbedButton';

export default function Home() {
  return (
    <main style={{ maxWidth: 600, margin: '2rem auto', padding: 16 }}>
      <h1>Berkshire Chatbot</h1>
      <EmbedButton />
      <ChatWindow />
    </main>
  );
}
