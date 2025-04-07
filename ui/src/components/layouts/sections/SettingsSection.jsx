import React from "react";
import {Card, CardContent, CardHeader, Divider} from "@mui/material";
import PropTypes from "prop-types";

function SettingsSection({header, content}) {
    return (
        <Card
            component="section"
            elevation={0}
            sx={{}}
        >
            <CardHeader
                title={header}
                sx={{
                    '& .MuiCardHeader-title': {
                        fontSize: '1.2rem',
                        fontWeight: 'medium',
                        color: 'grey',
                    }
                }}
            />
            <Divider sx={{width: '80%'}}/>
            <CardContent
                sx={{
                    paddingTop: '10px',
                    paddingLeft: '5%',
                    width: '60%'
                }}
            >
                {content}
            </CardContent>
        </Card>
    );
}

SettingsSection.propTypes = {
    header: PropTypes.string.isRequired,
    content: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.element),
        PropTypes.element
    ]).isRequired
}

export default SettingsSection;
