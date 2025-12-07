import { CloudUpload } from "@mui/icons-material";
import { Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, LinearProgress, Typography } from "@mui/material";
import { useState } from "react";
import FileService from "../services/files/File";

function FileUploadModal(props) {
  const { open, onClose, onUploadSuccess } = props;
  const [ selectedFile, setSelectedFile ] = useState(null);
  const [ error, setError ] = useState(null);
  const [ loading, setLoading ] = useState(false); 

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if(file) {
      setSelectedFile(file);
      setError(null);
    }
  }

  const handleUploadFile = async () => {
    if(!selectedFile) return;
    setLoading(true);

    try {
      await FileService.uploadFile(selectedFile);
      setLoading(false);
      setSelectedFile(null);
      onUploadSuccess();
      onClose();
    } catch (error) {
      console.log(error);
      setError('Error al subir el archivo. Inténtalo de nuevo.');
      setLoading(false);
    }
  };

  const handleClose = () => {
    setSelectedFile(null);
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth='sm' fullWidth>
      <DialogTitle>Subir Archivo</DialogTitle>

      <DialogContent>
        <Box>
          <input
            accept=".png, .gif, .jpg, .jpeg, .pdf"
            style={{ display: 'none' }}
            id="raised-button-file"
            type="file"
            onChange={handleFileChange}
          />

          <label htmlFor="raised-button-file">
            <Button variant="outlined" component="span" startIcon={<CloudUpload />}>
              Seleccionar Archivo
            </Button>
          </label>

          {
            selectedFile && (
              <Typography variant="body1" sx={{ mt: 2 }}>
                Archivo seleccionado: {selectedFile.name} ({(selectedFile.size / 1024).toFixed(2)} KB)
              </Typography>
            )
          }

          { loading && <LinearProgress sx={{ width: '100%' }}/> }
          { error && <Alert severity="error" sx={{ width: '100%' }}>{error}</Alert>}
        </Box>
      </DialogContent>

      <DialogActions>
        <Button onClick={handleClose}>
          Cancelar
        </Button>

        <Button
          onClick={handleUploadFile}
          disabled={!selectedFile || loading}
          variant="contained">
            { loading? 'Subiendo Archivo...' : 'Subir Archivo' }
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default FileUploadModal;