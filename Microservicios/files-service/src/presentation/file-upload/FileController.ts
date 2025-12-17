import type { Response, Request } from 'express';
import type { UploadedFile } from 'express-fileupload';
import { CustomError } from '../../domain/errors/CustomError.js';
import { FileUploadService } from '../services/file-upload.service.js';
import { dirname } from 'path';
import { fileURLToPath } from 'url';
import path from 'path';
import fs from 'fs';

const __dirname = dirname( fileURLToPath( import.meta.url ) );

export class FileUploadController {
  constructor(
    private readonly fileUploadService: FileUploadService
  ) {}

  private handleError( error: unknown, res: Response ) {
    if(error instanceof CustomError) {
      return res.status(error.statusCode).json( { error: error.message } );
    }

    console.log(`${error}`);
    return res.status(500).json( { error: 'Internal server error' } );
  };

  public uploadFile = (req: Request, res: Response) => {
    const type = req.params.type;
    const file = req.body.files[0] as UploadedFile;
    const userId = req.body.user.id;

    this.fileUploadService.uploadSingle( file, userId, `uploads/${type}` )
      .then(uploaded => res.json( uploaded ))
      .catch( error => this.handleError( error, res ) 
    );
  }

  public getFiles = (req: Request, res: Response) => {
    const userId = req.body.user.id;

    this.fileUploadService.getFilesByUserId( `uploads/users`, userId)
      .then( files => res.json( files ) )
      .catch( error => this.handleError( error, res ) 
    );
  }

  public getFile = async (req: Request, res: Response) => {
    const { type, id } = req.params;
    const userId = req.body.user.id;

    try {
      const file = await this.fileUploadService.validateFile(id!, userId);

      const filePath = path.resolve(__dirname, `../../../uploads/${type}/${id}`);
      if(!fs.existsSync(filePath)) {
        return res.status(404).json({ error: 'Archivo no encontrado' });
      }
      return res.sendFile(filePath);
    } catch (error) {
      return this.handleError(error, res);
    }
  }

  public deleteFile = (req: Request, res: Response) => {
    const { type, id } = req.params;
    this.fileUploadService.deleteFile( type!, id! )
      .then( () => res.json( { message: 'Archivo eliminado correctamente' } ) )
      .catch( error => this.handleError( error, res ) 
    );
  }
}