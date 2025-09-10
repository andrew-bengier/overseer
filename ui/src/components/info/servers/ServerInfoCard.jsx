import React from "react";
import PropTypes from "prop-types";
import {Server} from "../../../models/api/Server";
import {Card, CardHeader, CardMedia, IconButton, Typography} from "@mui/material";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import {useDispatch} from "react-redux";
import {scrubRoutePath} from "../../../utils/stringUtils";
import {useNavigate} from "react-router-dom";
import {selectServer} from "../../../redux/actions/Actions";

function ServerInfoCard({server}) {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    function handleServerClick() {
        dispatch(selectServer(server));
        navigate('/Servers/' + scrubRoutePath(server.id));
    }

    return (
        <Card
            sx={{
                maxWidth: 350,
                bgcolor: 'infoCard.main',
            }}
        >
            <CardHeader
                title={
                    <Typography
                        noWrap
                        variant="h6"
                        component="h4"
                        color="infoCard.text"
                    >
                        {server.name}
                    </Typography>
                }
                // avatar={
                //     <Avatar
                //         sx={{
                //             bgColor: 'primary'
                //         }}
                //         aria-label="server"
                //     >
                //         <StorageOutlinedIcon/>
                //     </Avatar>
                // }
                action={
                    <IconButton aria-label="server-options" onClick={() => {
                        handleServerClick();
                    }}>
                        <MoreVertIcon/>
                    </IconButton>
                }
            />
            <CardMedia
                component="img"
                image="/plex-logo.png"
                alt="server-image"
                sx={{
                    objectFit: 'contain',
                    width: '100%',
                    height: '100%'
                }}
            />
            {/*  TODO: card content including server stats?   */}
        </Card>
    );
}

ServerInfoCard.propTypes = {
    server: PropTypes.shape(Server).isRequired
}

export default ServerInfoCard;
