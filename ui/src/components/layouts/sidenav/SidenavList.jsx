import React from "react";
import {useNavigate} from "react-router-dom";
import {List, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import {scrubRoutePath} from "../../../utils/stringUtils";

export default function SidenavList({route, handleRouteClick, checkCurrent}) {
    const navigate = useNavigate();

    // TODO: update checkCurrent to collapse if route or subRoute not selected
    return (
        <React.Fragment>
            {route.subRoutes ? (
                <React.Fragment>
                    {route.component != null ? (
                        <ListItemButton key={route.name} onClick={() => navigate('/' + scrubRoutePath(route.name))}
                                        selected={checkCurrent(route)}>
                            <ListItemIcon>
                                {route.icon ? <route.icon/> : null}
                            </ListItemIcon>
                            <ListItemText primary={route.name}/>
                        </ListItemButton>
                    ) : (
                        <ListItemButton key={route.name}>
                            <ListItemIcon>
                                {route.icon ? <route.icon/> : null}
                            </ListItemIcon>
                            <ListItemText primary={route.name}/>
                        </ListItemButton>
                    )}
                    <List component="div" disablePadding>
                        {route.subRoutes.map((subRoute) => (
                            <ListItemButton key={subRoute.name}
                                            onClick={() => navigate('/' + scrubRoutePath(route.name) + '/' + scrubRoutePath(subRoute.name))}
                                            selected={checkCurrent(subRoute)}
                                            sx={{pl: 4}}
                            >
                                <ListItemIcon>
                                    {subRoute.icon ? <subRoute.icon/> : null}
                                </ListItemIcon>
                                <ListItemText primary={subRoute.name}/>
                            </ListItemButton>
                        ))}
                    </List>
                </React.Fragment>
            ) : (
                <ListItemButton key={route.name} onClick={() => navigate('/' + scrubRoutePath(route.name))}
                                selected={checkCurrent(route)}>
                    <ListItemIcon>
                        {route.icon ? <route.icon/> : null}
                    </ListItemIcon>
                    <ListItemText primary={route.name}/>
                </ListItemButton>
            )}
        </React.Fragment>
    )
}
