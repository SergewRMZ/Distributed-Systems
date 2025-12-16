import { CreateUserDto } from "../../domain/dto/CreateUser.dto.js";
import { CustomError } from "../../domain/errors/CustomError.js";
import type { Request, Response } from "express";
import type { AuthService } from "../services/auth.service.js";

export class AuthController {
  constructor(
    private readonly authService: AuthService
  ) {}

  private handleError = (error: unknown, res: Response) =>{
    if (error instanceof CustomError) {
      return res.status(error.statusCode).json({ error: error.message});
    }

    return res.status(500).json({ error: 'Internal server error' });  
  }

  public registerUser = (req: Request, res: Response) => {
    const [ error, createUserDto ] = CreateUserDto.create(req.body);
    if (error) return res.status(400).json({ error });

    this.authService.createUser( createUserDto! )
      .then((response) => res.json(response))
      .catch( error => this.handleError(error, res));
  }
}