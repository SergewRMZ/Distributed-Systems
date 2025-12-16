import jwt from 'jsonwebtoken';
import { envs } from './envs.js';

const JWT_SEED = envs.JWT_SEED;

export class JwtAdapter {
  /** Payload son los datos contenidos dentro del token. Información relevante sobre
   * la entidad asociada con el token.
   * La semilla de un token es muy importante, ya que si se tiene la semilla
   * es posible obtener los datos de todos los webtoken
   */
  static async generateToken (payload:any, duration:string = '2h') {  
    return new Promise((resolve) => {
      jwt.sign(payload, JWT_SEED, {expiresIn: duration}, (err, token) => {
        if (err) return resolve (null);
        
        resolve(token);
      });
    });
  }

  static validateToken (token:string) {
    return new Promise ((resolve) => {
      
      jwt.verify(token, JWT_SEED, (err, decoded) => {
        if (err) return resolve(null);

        resolve(decoded);
      });


    });
  }
}