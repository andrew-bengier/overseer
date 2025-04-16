import React from "react";
import {Grid, Typography} from "@mui/material";
import PropTypes from "prop-types";
import Uptime from "../time/Uptime";
import {formatDate} from "../../../utils/dateUtils";

function GitInfo({gitData}) {
    const data = Object.entries(gitData);

    function renderValues(key, value) {
        switch (key) {
            case 'startTime':
                return (<Uptime time={value}/>);
            case 'commitTime':
                return (
                    <Typography align="left">
                        {formatDate('EEEE MMMM dd yyyy HH:mm:sss', new Date(value), true)}
                    </Typography>
                );
            case 'sourceLink':
                return (
                    <Typography
                        align="left"
                        component="a"
                        href={value}
                    >
                        {value}
                    </Typography>
                );
            default:
                return (<Typography align="left">{value}</Typography>);
        }
    }

    return (
        <Grid
            container
            spacing={0}
            direction="column"
        >
            {data.map(([key, value], index) => (
                <Grid item size={12} key={index}>
                    {/*<Paper*/}
                    {/*    elevation={3}*/}
                    {/*    sx={{*/}
                    {/*        paddingX: '15px',*/}
                    {/*        paddingY: '10px'*/}
                    {/*    }}*/}
                    {/*>*/}
                    <Grid container spacing={0.5}>
                        <Grid item size={{xs: 6, sm: 4, md: 2}} sx={{overflow: 'hidden'}}>
                            <Typography
                                align="right"
                                fontWeight="fontWeightMedium"
                            >
                                {key}:
                            </Typography>
                        </Grid>
                        <Grid item size={{xs: 8}}
                              sx={{justifyContent: "flex-start", paddingLeft: '10px', overflow: 'hidden'}}>
                            {renderValues(key, value)}
                        </Grid>
                    </Grid>
                    {/*</Paper>*/}
                </Grid>
            ))}

        </Grid>
    )
        ;
}

GitInfo.propTypes = {
    gitData: PropTypes.any.isRequired
}

export default GitInfo;
