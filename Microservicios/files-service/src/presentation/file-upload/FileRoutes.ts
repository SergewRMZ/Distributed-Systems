import { Router } from "express";
import { FileUploadController } from "./FileController.js";
import { FileUploadService } from "../services/file-upload.service.js";
import { FileUploadMiddleware } from "../middleware/file-upload.middleware.js";
import { TypeMiddleware } from "../middleware/type.middleware.js";
import { FileRepository } from "../../domain/repository/FileRepository.js";

export class FileUploadRoutes {
  static get routes(): Router {
    const router = Router();
    const fileRespository = new FileRepository();
    const service = new FileUploadService(fileRespository);
    const controller = new FileUploadController(service);

    router.post(`/upload/:type`, 
      FileUploadMiddleware.containFiles, 
      TypeMiddleware.validTypes(['users']), 
      controller.uploadFile);

    router.get('/:type', 
      controller.getFiles);
    
    router.get('/:type/:id', 
      controller.getFile);

    router.delete('/:type/:id', controller.deleteFile)
    return router;
  }
}