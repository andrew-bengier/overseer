import React from 'react';
import {Box, List, ListItem, ListItemButton, ListItemText} from '@mui/material';
import {useNavigate} from "react-router-dom";
import {routePaths} from "../AppRoutes";

export default function Settings({route}) {
    const navigate = useNavigate();

    function handleLinkClick(link) {
        navigate(routePaths[route.name] + routePaths.separator + link.replace(/\s+/g, ''));
    }

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                // paddingTop: '200px'
            }}
        >
            {route.subRoutes?.map((link) => (
                <List>
                    <ListItem
                        key={link.name}
                        disablePadding
                    >
                        <ListItemButton onClick={() => handleLinkClick(link.name)}>
                            <ListItemText
                                primary={link.name}
                                secondary={link.description}
                            />
                        </ListItemButton>
                    </ListItem>
                </List>
            ))}
        </Box>
    );
}