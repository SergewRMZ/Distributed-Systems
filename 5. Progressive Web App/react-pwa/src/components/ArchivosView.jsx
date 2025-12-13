import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  List,
  ListItem,
  ListItemText,
  Divider,
  Grid,
  CircularProgress,
  Tooltip,
  IconButton,
} from "@mui/material";

import InsertDriveFileIcon from "@mui/icons-material/InsertDriveFile";
import FileService from "../services/files/File";
import { Delete } from "@mui/icons-material";
import ConfirmationModal from "./ConfirmationModal";

export default function ArchivosView(props) {
  const { refreshTrigger, onRefreshFiles } = props;

  const [ isConfirmationOpen, setIsConfirmationOpen ] = useState(false);
  const [ fileToDelete, setFileToDelete ] = useState(null);
  const [ archivos, setArchivos ] = useState([]);
  const [ loading, setLoading ] = useState(true);

  useEffect(() => {
    const fetchFiles = async () => {
      try {
        const data = await FileService.getFiles();
        const formatedFiles = data.map((file) => ({
          uuid: file.uuidFileName,
          filename: file.filename,
          size: file.size + ' B',
          fecha: new Date(file.uploadedAt).toLocaleDateString(),
          url: file.downloadUrl
        }));

        setArchivos(formatedFiles);
      } catch (error) {
        console.error("Error fetching files:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchFiles();
  }, [refreshTrigger]);

  const handleOpenFile = (url) => {
    if (url) {
      window.open(url, '_blank');
    } else {
      console.warn("No URL provided for this file.");
    }
  };

  // Método que activa el modal de confirmación
  const handleRequestDelete = (uuid) => {
    setFileToDelete(uuid);
    setIsConfirmationOpen(true);
  };

  // Método que cierra el modal de confirmación
  const handleCloseModal = () => {
    setIsConfirmationOpen(false);
    setFileToDelete(null);
  };

  // Método que se ejecuta al confirmar la eliminación
  const handleConfirmDelete = async () => {
    if(!fileToDelete) return;
    await FileService.deleteFile(fileToDelete);
    onRefreshFiles();
    setIsConfirmationOpen(false);
    setFileToDelete(null);
  }

  if (loading) {
    return (
      <Box sx={{ 
        display: 'flex',
        justifyContent: 'center',
      }}>
        <CircularProgress size="60px"/>
      </Box>
    )
  }

  if (archivos.length === 0) {
    return (
      <Box sx={{ mt: 4, textAlign: "center" }}>
        <Typography variant="h6" color="text.secondary">
          No tienes archivos en tu unidad.
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ mt: 2 }}>
      <Box sx={{ px: 2, py: 1.5, borderBottom: "1px solid #dadce0" }}>
        <Grid 
          container
        > 
          <Grid size={5}>
            <Typography variant="subtitle2" fontWeight="bold" color="text.secondary">
              Nombre
            </Typography>
          </Grid>
          
          <Grid size={3}>
            <Typography variant="subtitle2" fontWeight="bold" color="text.secondary">
              Tipo
            </Typography>
          </Grid>
          
          <Grid size={3} sx={{ textAlign: "right" }}>
            <Typography variant="subtitle2" fontWeight="bold" color="text.secondary">
              Fecha de Modificación
            </Typography>
          </Grid>

          <Grid size={1}/>
        </Grid>
      </Box>

      {/* --- LISTA DE ARCHIVOS --- */}
      <List disablePadding>
        {archivos.map((archivo) => (
          <React.Fragment key={archivo.uuid}>
            <ListItem
              // component="button"
              onClick={() => handleOpenFile(archivo.url)}
              sx={{
                py: 1.5,
                px: 2,
                cursor: "pointer",
                border: "none",
                background: "none",
                textAlign: "left",
                "&:hover": { 
                  backgroundColor: "#f0f4f9",
                  "& .MuiListItemText-primary": { 
                    color: "#1976d2",
                  },
                  "& .MuiSvgIcon-root": {
                    color: "#1976d2"
                  }
                },
              }}
            >
              <Grid 
                container 
                alignItems="center" 
                sx={{ 
                  width: "100%", 
                  }}>
                
                {/* NOMBRE */}
                <Grid size={5}>
                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    <InsertDriveFileIcon color="disabled" sx={{ mr: 2 }} />
                    <ListItemText 
                      primary={archivo.filename} 
                      slotProps={{ 
                        primary: { 
                          noWrap: true, 
                          title: archivo.filename, 
                          color: 'text.primary',
                          fontWeight: 'bold' // Puedes añadir estilos extra aquí
                        } 
                      }}
                    />
                  </Box>
                </Grid>

                {/* TIPO */}
                <Grid size={3}>
                  <ListItemText 
                    primary={archivo.size} 
                    slotProps={{ 
                      primary: { 
                        noWrap: true, 
                        title: archivo.size, 
                        color: 'text.primary',
                      } 
                    }}
                  />
                </Grid>

                {/* FECHA */}
                <Grid size={3} sx={{ textAlign: "right" }}>
                  <ListItemText primary={archivo.fecha} 
                    slotProps={{ 
                      primary: { 
                        noWrap: true, 
                        title: archivo.fecha, 
                        color: 'text.primary',
                      } 
                    }}
                  />
                </Grid>

                {/* ESPACIO EXTRA */}
                <Grid size={1} sx={{textAlign: "center"}}>
                  <Tooltip title="Eliminar">
                    <IconButton
                      className="delete-button"
                      size="small"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleRequestDelete(archivo.uuid);
                      }}

                      sx={{
                        opacity: 0.8,
                        transition: 'opacity 0.3s',
                        "&:focus": { outline: "none" },
                        "&:hover": { color: "#d32f2f" }
                      }}
                    >
                      <Delete fontSize="small"/>
                    </IconButton>
                  </Tooltip>
                </Grid>

              </Grid>
            </ListItem>
            <Divider component="li" />
          </React.Fragment>
        ))}
      </List>

      <ConfirmationModal
        openModal={isConfirmationOpen}
        onCloseModal={handleCloseModal}
        onConfirm={handleConfirmDelete}  
      />
    </Box>
  );
}