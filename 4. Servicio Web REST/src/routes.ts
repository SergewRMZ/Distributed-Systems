import { Router } from 'express';
import { FileUploadRoutes } from './presentation/file-upload/FileRoutes.js';

export class AppRoutes {
  static get routes(): Router {
    const router = Router();
    router.use('/api/files', FileUploadRoutes.routes);
    return router;
  }
};