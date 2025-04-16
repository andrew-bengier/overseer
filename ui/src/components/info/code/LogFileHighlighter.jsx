import React from 'react';
import PropTypes from "prop-types";
import {TextField} from "@mui/material";

function LogFileHighlighter({fileData}) {
    const [logData, setLogData] = React.useState(null);

    React.useEffect(() => {
        setLogData(fileData);
    }, [fileData]);

    return (
        <React.Fragment>
            {logData ? (
                <TextField
                    id="log-file-viewer"
                    variant="outlined"
                    disabled
                    multiline
                    fullWidth
                    maxRows={50}
                    value={fileData}
                />
            ) : null
            }
        </React.Fragment>
    );
}

LogFileHighlighter.propTypes = {
    fileData: PropTypes.string.isRequired
}

export default LogFileHighlighter;
