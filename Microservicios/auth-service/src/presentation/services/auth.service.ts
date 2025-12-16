import { bcrypAdapter } from "../../config/bcrypt.js";
import { JwtAdapter } from "../../config/jwt.adapter.js";
import type { CreateUserDto } from "../../domain/dto/CreateUser.dto.js";
import { CustomError } from "../../domain/errors/CustomError.js";
import type { AuthRepository } from "../../domain/repository/AuthRepository.js";

export class AuthService {
  constructor(
    private authRepository: AuthRepository
  ) {}

  public async createUser (createUserDto: CreateUserDto) {
    const existUser = await this.authRepository.getUserByEmail( createUserDto.email );
    if(existUser) throw CustomError.badRequest('El correo ya está registrado');

    const hashedPassword = bcrypAdapter.hash( createUserDto.password);
    createUserDto.password = hashedPassword;
    try {
      const user = await this.authRepository.createUser( createUserDto );
      const token = await JwtAdapter.generateToken({ id: user.id, email: user.email });

      if(!token) throw CustomError.internalServer('Error al generar el token de autenticación');
      const { password, ...safeUser } = user;
      return {
        safeUser,
        token
      }

    } catch (error) {
      throw error;
    }
  }
}