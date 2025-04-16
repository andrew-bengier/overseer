import React from "react";
import {Box} from "@mui/material";

function InfoBanner({children}) {
    return (
        <Box
            sx={{
                width: '100%',
                height: '100%',
                padding: '10px',
                borderRadius: 1,
                // border: '1px solid blue',
                bgcolor: 'info.light',
            }}
        >
            {children}
        </Box>
    );
}

export default InfoBanner;
