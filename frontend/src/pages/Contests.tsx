import { Link } from 'react-router-dom';

export default function Contests() {
  return (
    <section>
      <h1>Contests</h1>
      <p>Placeholder contest list.</p>
      <p>
        <Link to="/contests/1">Sample contest #1</Link> · <Link to="/">Home</Link>
      </p>
    </section>
  );
}
