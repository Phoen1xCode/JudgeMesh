import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import RootLayout from './layouts/RootLayout';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import ProblemList from './pages/ProblemList';
import ProblemDetail from './pages/ProblemDetail';
import Submits from './pages/Submits';
import Contests from './pages/Contests';
import ContestDetail from './pages/ContestDetail';
import Profile from './pages/Profile';
import NotFound from './pages/NotFound';

const router = createBrowserRouter([
  {
    path: '/',
    Component: RootLayout,
    children: [
      { index: true, Component: Home },
      { path: 'login', Component: Login },
      { path: 'register', Component: Register },
      { path: 'problems', Component: ProblemList },
      { path: 'problems/:id', Component: ProblemDetail },
      { path: 'submits', Component: Submits },
      { path: 'contests', Component: Contests },
      { path: 'contests/:id', Component: ContestDetail },
      { path: 'profile', Component: Profile },
      { path: '*', Component: NotFound },
    ],
  },
]);

export default function AppRouter() {
  return <RouterProvider router={router} />;
}
