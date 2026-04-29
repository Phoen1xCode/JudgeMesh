import { Link, useParams } from 'react-router-dom';

export default function ContestDetail() {
  const { id } = useParams<{ id: string }>();
  return (
    <section>
      <h1>Contest #{id}</h1>
      <p>Placeholder contest detail page.</p>
      <p>
        <Link to="/contests">Back to contests</Link> · <Link to="/">Home</Link>
      </p>
    </section>
  );
}
