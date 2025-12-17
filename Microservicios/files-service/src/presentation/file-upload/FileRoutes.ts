import { Router } from "express";
import { FileUploadController } from "./FileController.js";
import { FileUploadService } from "../services/file-upload.service.js";
import { FileUploadMiddleware } from "../middleware/file-upload.middleware.js";
import { TypeMiddleware } from "../middleware/type.middleware.js";
import { FileRepository } from "../../domain/repository/FileRepository.js";
import { AuthMiddleware } from "../middleware/auth.middleware.js";

export class FileUploadRoutes {
  static get routes(): Router {
    const router = Router();
    const fileRespository = new FileRepository();
    const service = new FileUploadService(fileRespository);
    const controller = new FileUploadController(service);

    router.post(`/upload/:type`, 
      AuthMiddleware.validateJWT,
      FileUploadMiddleware.containFiles, 
      TypeMiddleware.validTypes(['users']), 
      controller.uploadFile);

    router.get('/:type', 
      AuthMiddleware.validateJWT,
      controller.getFiles);
    
    router.get('/:type/:id', 
      AuthMiddleware.validateJWT,
      controller.getFile);

    router.delete('/:type/:id', 
      AuthMiddleware.validateJWT,
      controller.deleteFile)
    return router;
  }
}