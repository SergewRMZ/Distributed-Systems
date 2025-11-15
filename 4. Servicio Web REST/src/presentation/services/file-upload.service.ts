import path, { dirname } from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

import type { UploadedFile } from 'express-fileupload';
import { Uuid } from '../../config/uuid.adapter.js';
import { CustomError } from '../../domain/errors/CustomError.js';
import { FileRepository } from '../../domain/repository/FileRepository.js';
import { envs } from '../../config/envs.js';

const __dirname = dirname( fileURLToPath( import.meta.url ) );

export class FileUploadService {
  constructor(
    private fileRepository: FileRepository,
    private readonly uuid = Uuid.v4
  ) {}
  
  private checkFolder( folderPath: string ) {
    if ( !fs.existsSync(folderPath) ) {
      fs.mkdirSync(folderPath);
    }
  }

  public async getFiles (folder: string) {
    const allFiles = await this.fileRepository.getAllFiles();

    return allFiles.map(file => ({
      ...file,
      downloadUrl: `${envs.WEBSERVICE_URL}/api/files/users/${file.uuidFileName}`
    }));
  } 

  public async deleteFile (type: string, uuidFileName: string) {
    try {
      const existFile = await this.fileRepository.getFileByUuid( uuidFileName );
      if(!existFile) {
        throw CustomError.notFound('Archivo no encontrado en la base de datos');
      }

      const filePath = path.resolve(__dirname, `../../../uploads/${type}/${uuidFileName}`);

      await this.fileRepository.deleteFile( uuidFileName );
      if(!fs.existsSync(filePath)) {
        throw CustomError.notFound('Archivo no encontrado en el sistema de archivos');
      }

      fs.unlinkSync(filePath);
    } catch (error) {
      throw error;
    }
  }

  public async uploadSingle(
    file: UploadedFile,
    folder: string = 'uploads',
    validExtensions: string[] = ['png','gif', 'jpg','jpeg', 'pdf']
  ) {

    try {
      const fileExtension = file.mimetype.split('/').at(1) ?? '';
      if ( !validExtensions.includes(fileExtension) ) {
        throw CustomError
          .badRequest(`Invalid extension: ${ fileExtension }, valid ones ${ validExtensions }`);
      }

      const destination = path.resolve( __dirname, '../../../', folder );
      this.checkFolder( destination );

      const fileName = `${ this.uuid() }.${ fileExtension }`;
      file.mv(`${destination}/${ fileName }`);

      // Registrando en la base de datos
      await this.fileRepository.createFile( fileName, file.name, file.size );
      return { fileName };
    } catch (error) {
      throw error;
    }
  }
}