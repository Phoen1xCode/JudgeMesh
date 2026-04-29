import { Link } from 'react-router-dom';

export default function Submits() {
  return (
    <section>
      <h1>My Submits</h1>
      <p>Placeholder. Will hit GET /api/submits/mine via the gateway.</p>
      <p>
        <Link to="/">Home</Link>
      </p>
    </section>
  );
}
