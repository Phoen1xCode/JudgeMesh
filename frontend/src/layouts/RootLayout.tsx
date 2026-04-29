import { NavLink, Outlet } from 'react-router-dom';

const navItems = [
  { to: '/', label: 'Home', end: true },
  { to: '/problems', label: 'Problems' },
  { to: '/contests', label: 'Contests' },
  { to: '/submits', label: 'Submits' },
  { to: '/profile', label: 'Profile' },
  { to: '/login', label: 'Login' },
];

export default function RootLayout() {
  return (
    <div style={{ fontFamily: 'system-ui, sans-serif' }}>
      <header
        style={{
          padding: '12px 24px',
          borderBottom: '1px solid #e5e5e5',
          display: 'flex',
          gap: '16px',
          alignItems: 'center',
        }}
      >
        <strong style={{ marginRight: 12 }}>JudgeMesh</strong>
        <nav style={{ display: 'flex', gap: 12 }}>
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.end}
              style={({ isActive }) => ({
                textDecoration: 'none',
                color: isActive ? '#1d4ed8' : '#333',
                fontWeight: isActive ? 600 : 400,
              })}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </header>
      <main style={{ padding: '24px' }}>
        <Outlet />
      </main>
    </div>
  );
}
