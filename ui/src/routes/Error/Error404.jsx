import React from 'react';
import {Box, Typography} from '@mui/material';
import error404 from '../../../public/error404.png';

import messages from './messages';
import {FormattedMessage} from 'react-intl';

export default function Error404() {
    return (
        <Box
            sx={{
                display: 'flex',
                justifiedContent: 'center',
                alignItems: 'center',
                flexDirection: 'column',
                paddingTop: '200px'
            }}
        >
            <Box
                sx={{
                    display: 'flex',
                    justifiedContent: 'center',
                    alignItems: 'center',
                    flexDirection: 'column',
                    height: '300px',
                    width: '600px',
                    backgroundColor: 'black',
                    paddingTop: '20px'
                }}
            >
                <img src={error404} height={200} alt="Error 404"/>
                <Typography variant="h5" style={{color: 'darkgray'}}>
                    <FormattedMessage {...messages.error404Title} />
                </Typography>
            </Box>
        </Box>
    );
}
