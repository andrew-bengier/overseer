import React from "react";
import {Card, IconButton} from "@mui/material";
import AddBoxOutlinedIcon from '@mui/icons-material/AddBoxOutlined';
import PropTypes from "prop-types";

function AddCard({handleClick}) {
    return (
        <Card
            sx={{
                height: '140px',
                width: '290px',
                bgcolor: 'addCard.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
            }}
            onClick={handleClick}
        >
            <IconButton id={'add-button'} aria-label={'add-button'} sx={{padding: 0}}>
                <AddBoxOutlinedIcon sx={{fontSize: 120, padding: 0}}/>
            </IconButton>
        </Card>
    );
}

AddCard.propTypes = {
    handleClick: PropTypes.func.isRequired,
}

export default AddCard;
