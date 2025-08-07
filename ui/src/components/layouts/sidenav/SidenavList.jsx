import React from "react";
import {Box, Collapse, List, ListItemButton, ListItemIcon, ListItemText, Stack} from "@mui/material";

import './SidenavList.css';

export default function SidenavList({route, openRoute, isCurrentLocation, handleRouteClick}) {
    return (
        <React.Fragment>
            {route.subRoutes ? (
                <React.Fragment>
                    <Stack direction="row" spacing={0}>
                        <Box
                            sx={{
                                width: '5px',
                                bgcolor: openRoute === route.name ? 'primary.main' : null,
                            }}
                        />
                        {route.component != null ? (
                            <ListItemButton
                                key={route.name}
                                onClick={() => handleRouteClick(route)}
                                selected={isCurrentLocation(route)}
                            >
                                <ListItemIcon className="routeIconPadding">
                                    {route.icon ? <route.icon/> : null}
                                </ListItemIcon>
                                <ListItemText primary={route.name}/>
                            </ListItemButton>
                        ) : (
                            <ListItemButton
                                key={route.name}
                                onClick={() => handleRouteClick(route, route.subRoutes[0])}
                                selected={isCurrentLocation(route)}
                            >
                                <ListItemIcon className="routeIconPadding">
                                    {route.icon ? <route.icon/> : null}
                                </ListItemIcon>
                                <ListItemText primary={route.name}/>
                            </ListItemButton>
                        )}
                    </Stack>
                    <Collapse in={openRoute === route.name} timeout="auto" unmountOnExit>
                        <Stack direction="row" spacing={0}>
                            <Box
                                sx={{
                                    width: '5px',
                                    bgcolor: openRoute === route.name ? 'primary.main' : null,
                                }}
                            />
                            <List component="div" disablePadding sx={{width: '240px'}}>
                                {route.subRoutes.filter((subRoute) => subRoute.displayNav).map((subRoute) => (
                                    <ListItemButton
                                        key={subRoute.name}
                                        onClick={() => handleRouteClick(route, subRoute)}
                                        selected={isCurrentLocation(subRoute)}
                                    >
                                        <ListItemIcon className="subRouteIconPadding">
                                            {subRoute.icon ? <subRoute.icon/> : null}
                                        </ListItemIcon>
                                        <ListItemText primary={subRoute.name}/>
                                    </ListItemButton>
                                ))}
                            </List>
                        </Stack>
                    </Collapse>
                </React.Fragment>
            ) : (
                <Stack direction="row" spacing={0}>
                    <Box
                        sx={{
                            width: '5px',
                            bgcolor: openRoute === route.name ? 'primary.main' : null,
                        }}
                    />
                    <ListItemButton
                        key={route.name}
                        onClick={() => handleRouteClick(route)}
                        selected={isCurrentLocation(route)}
                    >
                        <ListItemIcon className="routeIconPadding">
                            {route.icon ? <route.icon/> : null}
                        </ListItemIcon>
                        <ListItemText primary={route.name}/>
                    </ListItemButton>
                </Stack>
            )}
        </React.Fragment>
    )
}
