import { Link, useParams } from 'react-router-dom';

export default function ProblemDetail() {
  const { id } = useParams<{ id: string }>();
  return (
    <section>
      <h1>Problem #{id}</h1>
      <p>Placeholder problem detail page.</p>
      <p>
        <Link to="/problems">Back to problems</Link> · <Link to="/">Home</Link>
      </p>
    </section>
  );
}
