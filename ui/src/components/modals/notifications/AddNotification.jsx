import React from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Grid} from "@mui/material";
import PropTypes from "prop-types";
import {notificationTypes} from "../../../services/NotificationService";
import ViewCard from "../../actions/view/ViewCard";

function AddNotification({isOpen, handleClose}) {
    const types = notificationTypes;

    return (
        <Dialog
            fullWidth
            maxWidth={'md'}
            open={isOpen}
            onClose={handleClose}
        >
            <DialogTitle id="form-dialog-title">Add Notification</DialogTitle>
            <DialogContent dividers>
                <Grid
                    container
                    spacing={2}
                    sx={{
                        paddingLeft: '15px'
                    }}
                >
                    {types.map((type) => (
                        <Grid item xs={3} key={type.name}>
                            <ViewCard name={type.name} url={type.url} handleClick={() => {
                                console.log(type.name)
                            }}/>
                        </Grid>
                    ))}
                </Grid>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}

AddNotification.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
}

export default AddNotification;