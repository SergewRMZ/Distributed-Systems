import type { NextFunction, Request, Response } from "express";
import { JwtAdapter } from "../../config/jwt.adapter.js";

export class AuthMiddleware {
  static async validateJWT (req: Request, res: Response, next:NextFunction) {
    const authorization = req.header('Authorization');
    if (!authorization) return res.status(401).json({ error: 'No se ha proporcionado el token'});
    if(!authorization.startsWith(`Bearer`)) return res.status(401).json({ error: 'Invalid Bearer Token'});

    const token = authorization.split(' ').at(1) || '';

    try {
      const payload = await JwtAdapter.validateToken <{ id: string }>(token);
      if (!payload) return res.status(401).json({ error: 'Invalid Token' });

      if(!req.body) {
        req.body = {}
      }

      req.body.user = {
        id: payload.id
      };

      next();
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: 'Internal Server Error' });
    }
  }
}