import { Router } from "express";
import { AuthService } from "../services/auth.service.js";
import { AuthRepository } from "../../domain/repository/AuthRepository.js";
import { AuthController } from "./AuthController.js";

export class AuthRoutes {
  static get routes(): Router {
    const router = Router();
    const authRepository = new AuthRepository();
    const authService = new AuthService(authRepository);
    const authController = new AuthController(authService);
    
    router.post('/register', authController.registerUser);
    return router;
  }
}