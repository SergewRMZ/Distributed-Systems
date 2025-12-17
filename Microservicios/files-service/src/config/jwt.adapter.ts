import jwt from 'jsonwebtoken';
import { envs } from './envs.js';

const JWT_SEED = envs.JWT_SEED;

export class JwtAdapter {
  static validateToken <T>(token:string): Promise<T | null> {
    return new Promise ((resolve) => {
      jwt.verify(token, JWT_SEED, (err, decoded) => {
        if (err) return resolve(null);

        resolve(decoded as T);
      });
    });
  }
}