import api from '../api';

const FileService = (() => {
  const deleteFile = async (uuid) => {
    const response = await api.delete(`/files/users/${uuid}`);
    return response.data;
  }

  const getFiles = async () => {
    try {
      const response = await api.get(`/files/users`);
      return response.data;
    } catch (error) {
      if(error.response?.data) {
        return error.response.data;
      }

      return { status: 'error', message: 'Error al conectar con el servidor'};
    }
  }

  const uploadFile = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await api.post(`/files/upload/users`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response;
  }

  return {
    getFiles,
    uploadFile,
    deleteFile
  }
})();

export default FileService;
