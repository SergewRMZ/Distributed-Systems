// src/App.jsx
import React, { useState } from 'react';
import Box from '@mui/material/Box';
// Importamos los componentes de la aplicación que crearemos
import Sidebar from './components/Sidebar'; 
import ArchivosView from './components/ArchivosView';
import { Typography } from '@mui/material';
import FileUploadModal from './components/FileUploadModal';

function App() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [refreshFiles, setRefreshFiles] = useState(0);


  const handleNewFileClick = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleUploadSuccess = () => {
    setRefreshFiles(prev => prev + 1);
  };

  return (
    <Box sx={{ 
      display: 'flex', 
      minHeight: '100vh',
      minWidth: '100vw', 
      }}>
      
      <Sidebar onNewFileClick={handleNewFileClick} />
      
      <Box component="main"
        sx={{ 
          flexGrow: 1, 
          p: 3, 
          minWidth: 0,
          backgroundColor: '#f6f7f8',
        
        }}
      >
        <Typography 
          variant="h5"             
          component="h1"
          sx={{ 
            mb: 3, 
            textAlign: 'left',
            color: '#252525',      // Un negro suave (no #000 puro) se ve más elegante
            fontWeight: 500        // Grosor de letra (400 es normal, 500 es medio)
          }}
        >
          Mis Archivos
        </Typography>
        <ArchivosView 
          refreshTrigger={refreshFiles}
          onRefreshFiles={handleUploadSuccess}/>
        
      </Box>

      <FileUploadModal
        open={isModalOpen}
        onClose={handleCloseModal}
        onRefreshFiles={handleUploadSuccess}
        />
    </Box>
  );
}

export default App;