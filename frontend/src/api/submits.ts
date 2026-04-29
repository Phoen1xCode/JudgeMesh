import client from './client';
import type { Submit } from '../types';

export const fetchMySubmits = () => client.get<Submit[]>('/api/submits/mine');

export const fetchSubmit = (id: string | number) =>
  client.get<Submit>(`/api/submits/${id}`);

export const createSubmit = (body: { problemId: number; language: string; code: string }) =>
  client.post<Submit>('/api/submits', body);
