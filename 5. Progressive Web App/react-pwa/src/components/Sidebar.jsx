import React from 'react';
import Drawer from '@mui/material/Drawer';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import AddIcon from '@mui/icons-material/Add'; 
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import FolderIcon from '@mui/icons-material/Folder';

const drawerWidth = 280;

function Sidebar({ onNewFileClick }) {
  return (
    <Drawer
      variant="permanent" 
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: { 
          width: drawerWidth, 
          boxSizing: 'border-box',
          borderRight: '1px solid #dadce0'
        },
      }}
    >
      <Box sx={{ p: 2 }}> 
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={onNewFileClick}
          sx={{
            backgroundColor: '#2E5BFF', 
            color: '#fff', 
            borderRadius: '24px', 
            boxShadow: '0 1px 2px 0 rgba(60,64,67,0.30), 0 1px 3px 1px rgba(60,64,67,0.15)',
            '&:hover': {
              backgroundColor: '#5C7FFF',
              color: '#fff',
              boxShadow: '0 1px 3px 0 rgba(60,64,67,0.30), 0 4px 8px 3px rgba(60,64,67,0.15)',
            },
            padding: '12px 24px',
            textTransform: 'none',
            marginBottom: '20px',
          }}
        >
          Nuevo Archivo
        </Button>
      </Box>

      {/* 3. Navegación del Sidebar */}
      <List>
        <ListItemButton selected>
          <ListItemIcon>
            <FolderIcon />
          </ListItemIcon>
          <ListItemText primary="Mi Unidad" />
        </ListItemButton>
      </List>
    </Drawer>
  );
}

export default Sidebar;