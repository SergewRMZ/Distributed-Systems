import type { Response, Request } from 'express';
import type { UploadedFile } from 'express-fileupload';
import { CustomError } from '../../domain/errors/CustomError.js';
import { FileUploadService } from '../services/file-upload.service.js';

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

    this.fileUploadService.uploadSingle( file, `uploads/${type}` )
      .then(uploaded => res.json( uploaded ))
      .catch( error => this.handleError( error, res ) 
    );
  }

  public getFiles = (req: Request, res: Response) => {
    this.fileUploadService.getFiles( `uploads/users` )
      .then( files => res.json( files ) )
      .catch( error => this.handleError( error, res ) 
    );
  }
}