import type { File } from '@prisma/client';
import { PrismaClient } from '@prisma/client';

export class FileRepository {
  private prisma = new PrismaClient();

  async createFile(uuidFileName: string, userId: string, filename: string, size: number): Promise <File | null> {
    return await this.prisma.file.create({
      data: {
        uuidFileName,
        userId,
        filename,
        size
      }
    });
  }

  async getFilesByUserId(userId: string): Promise <File[]> {
    return await this.prisma.file.findMany(
      {
        where: {
          userId
        },
        orderBy: {
          uploadedAt: "desc"
        }
      }
    );
  }

  async getFileByUuidAndUserId (uuidFileName: string, userId: string): Promise<File | null> {
    return await this.prisma.file.findFirst({
      where: {
        uuidFileName,
        userId
      },
    });
  }

  async getFileByUuid(uuidFileName: string): Promise < File | null> {
    return await this.prisma.file.findUnique({
      where: {
        uuidFileName
      }
    })
  }

  async deleteFile(uuidFileName: string): Promise <void> {
    try {
      await this.prisma.file.delete({
        where: {
          uuidFileName
        }
      });
    } catch (error) {
      throw error;
    }
  }
}