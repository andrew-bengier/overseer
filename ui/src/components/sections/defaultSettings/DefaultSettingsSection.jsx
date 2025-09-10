import React from 'react';
import TabPanel, {tabPanelA11yProps} from "../../layouts/tabs/TabPanel";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import {Box} from "@mui/material";
import PropTypes from "prop-types";
import DefaultSettingsForm from "../../forms/settings/DefaultSettingsForm";

function DefaultSettingsSection({initTabs = []}) {
    const [currentTab, setCurrentTab] = React.useState(0);
    const [tabs, setTabs] = React.useState(initTabs);

    const handleTabChange = (event, updated) => {
        setCurrentTab(updated);
    };

    function showTabs() {
        return (
            <Box sx={{width: '100%'}}>
                <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
                    <Tabs
                        value={currentTab}
                        onChange={handleTabChange}
                        textColor="primary"
                        indicatorColor="primary"
                        aria-label="Default Settings Tabs">
                        <Tab label="Item One" {...tabPanelA11yProps(0)} />
                        <Tab label="Item Two" {...tabPanelA11yProps(1)} />
                        <Tab label="Item Three" {...tabPanelA11yProps(2)} />
                    </Tabs>
                </Box>
                <TabPanel index={0} current={currentTab}>
                    <DefaultSettingsForm/>
                </TabPanel>
                <TabPanel index={1} current={currentTab}>
                    Item Two
                </TabPanel>
                <TabPanel index={2} current={currentTab}>
                    Item Three
                </TabPanel>
            </Box>
        );
    }

    if (tabs.length > 0) {
        return showTabs();
    } else {
        return null;
    }
}

DefaultSettingsSection.propTypes = {
    initTabs: PropTypes.array.isRequired,
}

export default DefaultSettingsSection;