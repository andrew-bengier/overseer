import React from "react";
import {Box, Divider, Drawer, List} from "@mui/material";
import ClickAwayListener from '@mui/material/ClickAwayListener';
import SidenavList from "./SidenavList";
import PropTypes from "prop-types";
import {navRoute} from "../../../routes/AppRoutes";
import useScreenSize from "../../../hooks/useScreenSize";
import {useLocation, useNavigate} from "react-router-dom";
import {scrubRoutePath} from "../../../utils/stringUtils";

function Sidenav({open, navRoutes, toggleSidenav}) {
    const isMobile = useScreenSize(window.width);
    const navigate = useNavigate();
    const location = useLocation();
    const [visible, setVisible] = React.useState(open);
    const [openRoute, setOpenRoute] = React.useState('');

    React.useEffect(() => {
        setVisible(open);
    }, [open]);

    const handleSidenavToggle = () => {
        if (isMobile && visible) {
            setVisible(false);
            toggleSidenav();
        }
    }

    const handleRouteClick = (route, subRoute) => {
        if (subRoute === undefined) {
            setOpenRoute(route.name);
            navigate('/' + scrubRoutePath(route.name));
        } else {
            setOpenRoute(route.name);
            navigate('/' + scrubRoutePath(route.name) + '/' + scrubRoutePath(subRoute.name))
        }
    }

    const isCurrentLocation = (route) => {
        if (route !== null) {
            return location.pathname.toLowerCase().includes('/' + scrubRoutePath(route.name).toLowerCase()) || location.pathname.toLowerCase().includes('/' + scrubRoutePath(route.key).toLowerCase())
        } else {
            return false;
        }
    }

    return (
        <ClickAwayListener onClickAway={handleSidenavToggle}>
            <Drawer
                id="sidenav-drawer"
                anchor="left"
                variant="persistent"
                open={visible}
                sx={{
                    width: 240,
                    flexShrink: 0,
                    "& .MuiDrawer-paper": {
                        marginTop: '64px'
                    }
                }}
            >
                <Box sx={{paddingTop: 0}}>
                    <List sx={{width: '240px'}}>
                        {navRoutes.filter(route => route.displayNav && route.standardNav).map((route) => (
                            <SidenavList key={route.key} route={route} openRoute={openRoute}
                                         isCurrentLocation={isCurrentLocation} handleRouteClick={handleRouteClick}/>
                        ))}
                    </List>
                    <Divider/>
                    <List sx={{width: '240px'}}>
                        {navRoutes.filter(route => route.displayNav && !route.standardNav).map((route) => (
                            <SidenavList key={route.key} route={route} openRoute={openRoute}
                                         isCurrentLocation={isCurrentLocation} handleRouteClick={handleRouteClick}/>
                        ))}
                    </List>
                </Box>
            </Drawer>
        </ClickAwayListener>
    );
}

Sidenav.propTypes = {
    open: PropTypes.bool.isRequired,
    navRoutes: PropTypes.arrayOf(PropTypes.shape({navRoute})).isRequired,
    toggleSidenav: PropTypes.func.isRequired
}

export default Sidenav;
