import React from "react";
import {Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import PropTypes from "prop-types";

function ViewCard({name, url = null, handleClick}) {
    return (
        <Card
            sx={{
                height: '140px',
                width: '380px',
                bgcolor: 'addCard.main',
            }}
            onClick={handleClick}
        >
            <CardContent
                sx={{
                    paddingTop: '20px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}
            >
                {/*<Typography component="div" variant="h4" fontWeight="fontWeightBold">*/}
                <Typography component="div" variant="h4">
                    {name}
                </Typography>
            </CardContent>
            <CardActions
                sx={{
                    paddingTop: '20px',
                    display: 'flex',
                    alignItems: 'flex-end',
                    justifyContent: 'flex-end',
                }}
            >
                {url &&
                    <Button variant='outlined' size='small' href={url} target={'_blank'}>More Info</Button>
                }
            </CardActions>
        </Card>
    );
}

ViewCard.propTypes = {
    name: PropTypes.string.isRequired,
    url: PropTypes.string,
    handleClick: PropTypes.func.isRequired,
}

export default ViewCard;
