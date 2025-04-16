import React from "react";
import {Avatar, Box, Button, Typography} from "@mui/material";
import SellOutlinedIcon from '@mui/icons-material/SellOutlined';
import CommitOutlinedIcon from '@mui/icons-material/CommitOutlined';
import {shortenSha} from "../../../utils/stringUtils";
import {formatDate} from "../../../utils/dateUtils";

function GitCommit({gitData}) {
    return (
        <Box
            sx={{
                paddingTop: "90px",
                paddingLeft: "25px",
                paddingRight: "10px"
            }}
        >
            <Typography variant="h6">{formatDate("MMM dd, yyyy", new Date(gitData.created), true)}</Typography>
            <Button variant="text" component="a" href={gitData.authorUrl}
                    startIcon={<Avatar src={gitData.authorAvatarUrl}/>}>{gitData.author}</Button>
            <Button variant="text" component="a" href={gitData.url}
                    startIcon={<SellOutlinedIcon/>}>{gitData.tag}</Button>
            <Button variant="text" component="a" href={gitData.commitUrl}
                    startIcon={<CommitOutlinedIcon/>}>{shortenSha(gitData.sha)}</Button>
        </Box>
    );
}

GitCommit.propTypes = {}

export default GitCommit;
