import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function Register() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  return (
    <section>
      <h1>Register</h1>
      <p>Placeholder registration form (does not call API yet).</p>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          // Placeholder: real registration wiring in Sprint 1.
          console.log('[register] submit', { username, email, password: '***' });
        }}
        style={{ display: 'grid', gap: 8, maxWidth: 320 }}
      >
        <label>
          Username
          <input value={username} onChange={(e) => setUsername(e.target.value)} />
        </label>
        <label>
          Email
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <button type="submit">Create account</button>
      </form>
      <p>
        Already registered? <Link to="/login">Login</Link> · <Link to="/">Home</Link>
      </p>
    </section>
  );
}
