import { Router } from "express";
import { FileUploadController } from "./FileController.js";
import { FileUploadService } from "../services/file-upload.service.js";
import { FileUploadMiddleware } from "../middleware/file-upload.middleware.js";
import { TypeMiddleware } from "../middleware/type.middleware.js";

export class FileUploadRoutes {
  static get routes(): Router {
    const router = Router();
    const service = new FileUploadService();
    const controller = new FileUploadController(service);

    router.use(FileUploadMiddleware.containFiles);
    router.use(TypeMiddleware.validTypes(['users']));
    router.post(`/single/:type`, controller.uploadFile)
    return router;
  }
}