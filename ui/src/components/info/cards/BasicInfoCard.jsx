import React from "react";
import {Card, Typography} from "@mui/material";
import PropTypes from "prop-types";

function BasicInfoCard({information, valid, handleClick}) {
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
            <Typography
                component="div"
                variant="h4"
                fontWeight="fontWeightBold"
                color={valid ? 'success.main' : 'error.main'}
            >
                {information.name}
            </Typography>
        </Card>
    );
}

BasicInfoCard.propTypes = {
    information: PropTypes.object.isRequired,
    handleClick: PropTypes.func.isRequired,
}

export default BasicInfoCard;
