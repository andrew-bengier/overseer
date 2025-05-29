import React from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Box, Divider, Drawer, List} from "@mui/material";
import ClickAwayListener from '@mui/material/ClickAwayListener';
import {scrubRoutePath} from "../../../utils/stringUtils";
import SidenavList from "./SidenavList";
import PropTypes from "prop-types";
import {navRoute} from "../../../routes/AppRoutes";
import useScreenSize from "../../../hooks/useScreenSize";

function Sidenav({open, navRoutes, toggleSidenav}) {
    const isMobile = useScreenSize(window.width);
    const navigate = useNavigate();
    const location = useLocation();
    const [visible, setVisible] = React.useState(open);
    const [subRoutesOpen, setSubRoutesOpen] = React.useState([]);

    React.useEffect(() => {
        setVisible(open);
    }, [open]);

    const handleRouteClick = (route) => {
        let updatedRoutes = navRoutes.map((navRoute) => {
            if (navRoute.name === route.name) {
                return {name: navRoute.name, open: true};
            } else {
                return {name: navRoute.name, open: false};
            }
        });

        setSubRoutesOpen(updatedRoutes);

        navigate('/' + scrubRoutePath(route.name));
    }

    const isCurrentLocation = (route) => {
        if (route !== null) {
            return location.pathname.toLowerCase().includes('/' + scrubRoutePath(route.name).toLowerCase()) || location.pathname.toLowerCase().includes('/' + scrubRoutePath(route.key).toLowerCase())
        } else {
            return false;
        }
    }

    const handleSidenavToggle = () => {
        if (isMobile && visible) {
            setVisible(false);
            toggleSidenav();
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
                        // height: "100%"
                    }
                }}
            >
                {/*<Toolbar/>*/}
                <Box sx={{paddingTop: 0}}>
                    <List sx={{width: '240px'}}>
                        {navRoutes.filter(route => route.displayNav && route.standardNav).map((route) => (
                            <SidenavList key={route.key} route={route} handleRouteClick={handleRouteClick}
                                         checkCurrent={isCurrentLocation}/>
                        ))}
                    </List>
                    <Divider/>
                    <List sx={{width: '240px'}}>
                        {navRoutes.filter(route => route.displayNav && !route.standardNav).map((route) => (
                            <SidenavList key={route.key} route={route} handleRouteClick={handleRouteClick}
                                         checkCurrent={isCurrentLocation}/>
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
