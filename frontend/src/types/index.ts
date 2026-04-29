// Mirrors services/api/src/main/java/com/judgemesh/api/dto/*.java

export type UserRole = 'STUDENT' | 'SETTER' | 'ADMIN';

export interface User {
  id: number;
  username: string;
  email: string;
  avatarUrl?: string;
  /** STUDENT / SETTER / ADMIN */
  role: UserRole;
}

export type ProblemDifficulty = 'EASY' | 'MEDIUM' | 'HARD';
export type ProblemStatus = 'DRAFT' | 'PUBLISHED' | 'OFFLINE';

export interface Problem {
  id: number;
  title: string;
  description: string;
  difficulty: ProblemDifficulty | string;
  tags: string[];
  timeLimitMs: number;
  memoryLimitMb: number;
  /** DRAFT / PUBLISHED / OFFLINE */
  status: ProblemStatus | string;
}

export type SubmitStatus =
  | 'PENDING'
  | 'JUDGING'
  | 'AC'
  | 'WA'
  | 'TLE'
  | 'MLE'
  | 'RE'
  | 'CE'
  | 'SE';

export interface Submit {
  id: number;
  userId: number;
  problemId: number;
  language: string;
  /** PENDING / JUDGING / AC / WA / TLE / MLE / RE / CE / SE */
  status: SubmitStatus | string;
  timeUsedMs?: number;
  memoryUsedKb?: number;
  /** ISO8601 timestamp from java.time.Instant */
  createdAt: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}
