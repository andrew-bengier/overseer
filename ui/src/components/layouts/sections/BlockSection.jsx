import React from "react";
import {Card, CardContent, CardHeader, Divider} from "@mui/material";
import PropTypes from "prop-types";

function BlockSection({header, content, width = '100%'}) {
    return (
        <Card
            component="section"
            elevation={0}
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
                    width: width
                }}
            >
                {content}
            </CardContent>
        </Card>
    );
}

BlockSection.propTypes = {
    header: PropTypes.string.isRequired,
    content: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.element),
        PropTypes.element
    ]).isRequired
}

export default BlockSection;
