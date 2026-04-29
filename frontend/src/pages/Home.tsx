import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <section>
      <h1>JudgeMesh</h1>
      <p>Welcome to JudgeMesh — an online judge platform. This is a placeholder home page.</p>
      <p>
        Try <Link to="/problems">Problems</Link> or <Link to="/login">Login</Link>.
      </p>
    </section>
  );
}
