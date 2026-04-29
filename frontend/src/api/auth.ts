import client from './client';
import type { LoginResponse, User } from '../types';

export const login = (body: { username: string; password: string }) =>
  client.post<LoginResponse>('/api/users/login', body);

export const register = (body: { username: string; email: string; password: string }) =>
  client.post<User>('/api/users/register', body);

export const me = () => client.get<User>('/api/users/me');
