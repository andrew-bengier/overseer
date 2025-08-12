import React from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@mui/material";
import PropTypes from "prop-types";
import ConnectionSettingsForm from "../../forms/connection/ConnectionSettingsForm";

function AddEditConnection({isOpen, handleClose}) {
    return (
        <Dialog
            fullWidth
            maxWidth={'md'}
            open={isOpen}
            onClose={handleClose}
        >
            <DialogTitle id="form-dialog-title">Add Connection</DialogTitle>
            <DialogContent dividers>
                <ConnectionSettingsForm type={"plex"}/>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}

AddEditConnection.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
}

export default AddEditConnection;