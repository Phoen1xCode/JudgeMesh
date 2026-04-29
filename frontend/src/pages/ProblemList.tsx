import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchProblems } from '../api/problems';
import type { Problem } from '../types';

export default function ProblemList() {
  const [problems, setProblems] = useState<Problem[]>([]);
  const [loaded, setLoaded] = useState(false);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const { data } = await fetchProblems();
        if (!cancelled) setProblems(data);
      } catch (err) {
        // Backend may not be up yet — degrade gracefully.
        console.warn('[problems] fetch failed:', err);
      } finally {
        if (!cancelled) setLoaded(true);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <section>
      <h1>Problems</h1>
      <p>Placeholder list. Will hit GET /api/problems via the gateway.</p>
      {!loaded && <p>Loading…</p>}
      {loaded && problems.length === 0 && <p>(No problems loaded — backend may be offline.)</p>}
      {problems.length > 0 && (
        <ul>
          {problems.map((p) => (
            <li key={p.id}>
              <Link to={`/problems/${p.id}`}>{p.title}</Link> — {p.difficulty}
            </li>
          ))}
        </ul>
      )}
      <p>
        <Link to="/">Back to home</Link>
      </p>
    </section>
  );
}
