import React from "react";
import {Avatar, Card, CardHeader, IconButton, Typography} from "@mui/material";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import TheatersOutlinedIcon from '@mui/icons-material/TheatersOutlined'
import PersonalVideoOutlinedIcon from '@mui/icons-material/PersonalVideoOutlined';
import LibraryMusicOutlinedIcon from '@mui/icons-material/LibraryMusicOutlined';
import PhotoCameraOutlinedIcon from '@mui/icons-material/PhotoCameraOutlined';
import VideocamOutlinedIcon from '@mui/icons-material/VideocamOutlined';

function LibraryInfoCard({library}) {

    function renderLibraryIcon() {
        switch (library.type) {
            case 'movie':
                return (<TheatersOutlinedIcon/>);
            case 'show':
            case 'series':
            case 'tv':
            case 'tv series':
            case 'tv show':
                return (<PersonalVideoOutlinedIcon/>);
            case 'artist':
            case 'music':
                return (<LibraryMusicOutlinedIcon/>);
            case 'photos':
                return (<PhotoCameraOutlinedIcon/>);
            case 'other':
                return (<VideocamOutlinedIcon/>);
        }
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
                        {library.name}
                    </Typography>
                }
                avatar={
                    <Avatar
                        sx={{
                            bgcolor: 'secondary.light'
                        }}
                        aria-label="server"
                    >
                        {renderLibraryIcon()}
                    </Avatar>
                }
                action={
                    <IconButton aria-label="server-options">
                        <MoreVertIcon/>
                    </IconButton>
                }
            />
            {/*  TODO: card content including server stats?   */}
        </Card>
    );
}

LibraryInfoCard.propTypes = {
    // library: PropTypes.shape(Server).isRequired
}

export default LibraryInfoCard;
