import { PrismaClient } from "@prisma/client"
import type { User } from "@prisma/client"
import type { CreateUserDto } from "../dto/CreateUser.dto.js";
export class AuthRepository {
  async createUser(createUserDto: CreateUserDto): Promise<User> {
    const prisma = new PrismaClient();
    return await prisma.user.create({
      data: {
        name: createUserDto.name,
        email: createUserDto.email,
        password: createUserDto.password
      }
    }); 
  }

  async getUserByEmail(email: string): Promise<User | null> {
    const prisma = new PrismaClient();

    return await prisma.user.findUnique({
      where: {
        email
      }
    });
  }
}