import { bcrypAdapter } from "../../config/bcrypt.js";
import { JwtAdapter } from "../../config/jwt.adapter.js";
import type { CreateUserDto } from "../../domain/dto/CreateUser.dto.js";
import type { LoginUserDto } from "../../domain/dto/LoginUser.dto.js";
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
        user: safeUser,
        token: token
      }

    } catch (error) {
      throw error;
    }
  }

  public async login (loginUserDto: LoginUserDto) {

    try {
      const user = await this.authRepository.getUserByEmail( loginUserDto.email );
      if(!user) throw CustomError.badRequest('El correo no está registrado a un usuario');

      const isPasswordValid = bcrypAdapter.compare( loginUserDto.password, user.password );

      if(!isPasswordValid) throw CustomError.badRequest('Contraseña incorrecta');
    
      const token = await JwtAdapter.generateToken({ id: user.id, email: user.email });
      if(!token) throw CustomError.internalServer('Error al generar el token de autenticación');

      const { password, ...safeUser } = user;

      return {
        user: safeUser,
        token: token
      }
    } catch (error) {
      throw CustomError.internalServer('Ha ocurrido un error en el servidor');
    }
  }
}