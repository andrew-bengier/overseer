import React from 'react';
import {Box, Stack} from '@mui/material';
import './HeaderLogo.css';

import logo from '../../../../public/icon.png';
import logo_text from '../../../../public/logo_text.png';

function HeaderLogo() {
    return (
        <Box
            id='header-logo'
            sx={{
                display: 'flex',
                justifiedContent: 'center',
                alightItems: 'center',
                flexDirection: 'column',
                height: '50px', //TODO: update this to be dynamic
                width: '200px',
                paddingX: '20px',
                paddingY: '10px',
            }}
        >
            <Stack direction='row' spacing={0}>
                <img src={logo} alt='logo image' height='50px'/>
                <img src={logo_text} alt='logo text' height='20px' className={'imagePadding'}/>
            </Stack>
        </Box>
    );
}

export default HeaderLogo;