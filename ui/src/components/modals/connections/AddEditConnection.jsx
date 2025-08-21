import React from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@mui/material";
import PropTypes from "prop-types";
import PlexConnectionForm from "../../forms/connection/PlexConnectionForm";

function AddEditConnection({isOpen, type, connectionData, handleClose}) {
    const updateValues = (values) => {
        connectionData = values;
        console.log('Updated connectionData:', connectionData);
    }

    const closeModal = () => {
        console.log('Closing connection edit:', connectionData);
        handleClose(connectionData);
    }

    return (
        <Dialog
            fullWidth
            maxWidth={'md'}
            open={isOpen}
            onClose={handleClose}
        >
            <DialogTitle id="form-dialog-title">Add Connection</DialogTitle>
            <DialogContent dividers>
                {/*<ConnectionSettingsForm type={"plex"} data={connectionData}/>*/}
                {type === 'plex' &&
                    <PlexConnectionForm data={connectionData} handleSave={updateValues}/>
                }
            </DialogContent>
            <DialogActions>
                <Button onClick={closeModal}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}

AddEditConnection.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    type: PropTypes.string.isRequired,
    connectionData: PropTypes.object.isRequired,
    handleClose: PropTypes.func.isRequired,
}

export default AddEditConnection;