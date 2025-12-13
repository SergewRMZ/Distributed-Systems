import { 
  Box, 
  Button, 
  Dialog, 
  DialogActions, 
  DialogContent, 
  DialogTitle, 
  Typography } from "@mui/material";

function ConfirmationModal(props) {
  const { openModal, onCloseModal, onConfirm } = props;

  return (
    <Dialog open={openModal} onClose={onCloseModal} maxWidth='xs' fullWidth>
      <DialogTitle>Eliminar Archivo</DialogTitle>

      <DialogContent>
        <Box sx={{ mt: 1}}>
          <Typography variant="body1" color="text.secondary">
            ¿Estás seguro de que deseas eliminar este archivo?
          </Typography>
        </Box>
      </DialogContent>

      <DialogActions sx={{ pb:2, px:3}}>
        <Button onClick={onCloseModal} color="primary">
          Cancelar
        </Button>

        <Button
          onClick={onConfirm}
          color="error"
          variant="contained"
          disableElevation
        >
          Eliminar
        </Button>
      </DialogActions>
    </Dialog>
  );
}

export default ConfirmationModal;