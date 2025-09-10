import React from 'react';
import PropTypes from "prop-types";
import {Box} from "@mui/material";

export function tabPanelA11yProps(index) {
    return {
        id: `tab-${index}`,
        'aria-controls': `tabpanel-${index}`,
    };
}

function TabPanel({children, index, current}) {
    return (
        <div
            role="tabpanel"
            hidden={current !== index}
            id={`tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
        >
            {current === index &&
                <Box sx={{p: 3}}>
                    {children}
                </Box>
            }
        </div>
    );
}

TabPanel.propTypes = {
    children: PropTypes.node.isRequired,
    index: PropTypes.number.isRequired,
    current: PropTypes.number.isRequired,
}

export default TabPanel;
