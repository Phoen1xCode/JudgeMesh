import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  return (
    <section>
      <h1>Login</h1>
      <p>Placeholder login form (does not call API yet).</p>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          // Placeholder: real auth wiring in Sprint 1.
          console.log('[login] submit', { username, password: '***' });
        }}
        style={{ display: 'grid', gap: 8, maxWidth: 320 }}
      >
        <label>
          Username
          <input
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
          />
        </label>
        <button type="submit">Sign in</button>
      </form>
      <p>
        No account? <Link to="/register">Register</Link> · <Link to="/">Home</Link>
      </p>
    </section>
  );
}
