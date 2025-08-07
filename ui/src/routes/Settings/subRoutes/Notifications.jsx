import React from "react";
import {Grid} from "@mui/material";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import {useIntl} from "react-intl";
import AddCard from "../../../components/actions/add/AddCard";
import AddNotification from "../../../components/modals/notifications/AddNotification";

export default function Notifications() {
    const {formatMessage} = useIntl();
    const notifications = [];
    // const [selectedNotification, setSelectedNotification] = React.useState(null);
    const [addEditNotificationOpen, setAddEditNotificationOpen] = React.useState(false);

    const handleAddEditClose = () => {
        setAddEditNotificationOpen(false);
    }

    return (
        <React.Fragment>
            <BlockSection
                header={formatMessage({id: 'src.routes.notifications.notificationsSettingsSectionTitle'})}
                width="60%"
                content={
                    <Grid
                        container
                        spacing={2}
                        sx={{
                            paddingTop: '20px'
                        }}
                    >
                        {/*{notifications.map((notification) => (*/}
                        {/*    <Grid size={8} onClick={() => handleSelect(notifications)}>*/}
                        {/*        <ServerInfoCard server={server}/>*/}
                        {/*    </Grid>*/}
                        {/*))}*/}
                        <Grid size={8}>
                            <AddCard handleClick={() => setAddEditNotificationOpen(true)}/>
                        </Grid>
                    </Grid>
                }
            />
            <AddNotification isOpen={addEditNotificationOpen} handleClose={handleAddEditClose}/>
        </React.Fragment>
    );
}