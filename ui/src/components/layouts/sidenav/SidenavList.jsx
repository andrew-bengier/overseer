import React from "react";
import {useNavigate} from "react-router-dom";
import {ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import {scrubRoutePath} from "../../../utils/stringUtils";

export default function SidenavList({route, handleRouteClick, checkCurrent}) {
    const navigate = useNavigate();

    return (
        <React.Fragment>
            <ListItemButton key={route.name} onClick={() => navigate('/' + scrubRoutePath(route.name))}
                            selected={checkCurrent(route)}>
                <ListItemIcon>
                    {route.icon ? <route.icon/> : null}
                </ListItemIcon>
                <ListItemText primary={route.name}/>
            </ListItemButton>
        </React.Fragment>
    )
}
