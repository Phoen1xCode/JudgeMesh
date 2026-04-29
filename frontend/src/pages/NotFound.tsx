import { Link } from 'react-router-dom';

export default function NotFound() {
  return (
    <section>
      <h1>404 — Not Found</h1>
      <p>The page you requested does not exist.</p>
      <p>
        <Link to="/">Back to home</Link>
      </p>
    </section>
  );
}
