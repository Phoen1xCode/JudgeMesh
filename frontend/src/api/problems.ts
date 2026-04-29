import client from './client';
import type { Problem } from '../types';

export const fetchProblems = () => client.get<Problem[]>('/api/problems');

export const fetchProblem = (id: string | number) =>
  client.get<Problem>(`/api/problems/${id}`);
