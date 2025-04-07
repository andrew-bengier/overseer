import React from 'react';
import {FormattedMessage, useIntl} from 'react-intl';
import {useNavigate} from "react-router-dom";
import {AppBar, Box, IconButton, Menu, MenuItem, Switch, Toolbar, Tooltip, Typography} from "@mui/material";
import PersonIcon from '@mui/icons-material/Person';
import MenuIcon from '@mui/icons-material/Menu';
import messages from "./messages";
import useScreenSize from "../../../hooks/useScreenSize";

function Header({theme, toggleTheme, toggleSidenav}) {
    // const screenSize = useScreenSize();
    const isMobile = useScreenSize(window.width);
    const navigate = useNavigate();
    const {formatMessage} = useIntl();
    const [anchorElUser, setAnchorElUser] = React.useState(null);
    const [darkMode, setDarkMode] = React.useState(theme.palette.mode === 'dark');

    // React.useEffect(() => {
    //     console.log("loaded header")
    // }, []);

    const toggleDarkMode = (dark) => {
        setDarkMode(dark);
        toggleTheme(dark);
    };

    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleSidenavToggle = () => {
        toggleSidenav();
    }


    const handleCloseUserMenu = (setting) => {
        setAnchorElUser(null);

        if (setting === 'Dark Mode') {
            toggleDarkMode(!darkMode);
        }
    };

    const handleThemeChange = (event) => {
        toggleDarkMode(event.target.checked);
        setAnchorElUser(null);
    };

    const handleAuthLogIn = () => {
    }

    const handleAuthLogout = () => {
    }

    return (
        <React.Fragment>
            <AppBar
                id="header-appbar"
                position="fixed"
                sx={{
                    width: '100%',
                    ml: '240px',
                    zIndex: (theme) => theme.zIndex.drawer + 1
                }}
            >
                <Toolbar
                    id="appbar-toolbar"
                    disableGutters
                >
                    {!isMobile &&
                        <Box
                            sx={{
                                flexGrow: 1,
                            }}
                        />
                    }
                    <Box
                        sx={{
                            flexGrow: 0,
                            marginRight: {xs: 'none', md: '20px'},
                            marginLeft: {xs: '20px', md: 'none'},
                        }}>
                        <Tooltip title={formatMessage({id: 'src.components.layouts.header.settingsOpenTooltip'})}>
                            <IconButton
                                id="appbar-settings-button"
                                sx={{p: 0}}
                                onClick={handleOpenUserMenu}
                            >
                                <PersonIcon/>
                            </IconButton>
                        </Tooltip>
                        <Menu
                            id="appbar-menu"
                            sx={{mt: '45px'}}
                            anchorEl={anchorElUser}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            <MenuItem key="Dark Mode" onClick={() => handleCloseUserMenu('Dark Mode')}>
                                <Switch
                                    id="appbar-dark-mode-switch"
                                    checked={darkMode}
                                    onChange={handleThemeChange}
                                    size="small"
                                />
                                <Typography textAlign="center">
                                    <FormattedMessage {...messages.settingsDarkModeTitle} />
                                </Typography>
                            </MenuItem>
                        </Menu>
                    </Box>
                    {isMobile &&
                        <Box
                            sx={{
                                flexGrow: 0,
                                marginLeft: {xs: '20px'},
                                display: {md: 'none'}
                            }}
                        >
                            <Tooltip title={formatMessage({id: 'src.components.layouts.header.settingsOpenTooltip'})}>
                                <IconButton
                                    id="appbar-settings-button"
                                    sx={{p: 0}}
                                    onClick={handleSidenavToggle}
                                >
                                    <MenuIcon/>
                                </IconButton>
                            </Tooltip>
                        </Box>
                    }
                </Toolbar>
            </AppBar>
        </React.Fragment>
    );
}

Header.propTypes = {}

export default Header;
