import React from 'react';
import {useIntl} from "react-intl";
import {Stack, Typography} from "@mui/material";

function DefaultSettingsForm() {
    const {formatMessage} = useIntl();

    // Default Settings
    // default.settings.default.Active=true
    // default.settings.default.AssetDirectory=/overseer/assets/media/
    // default.settings.default.CollectionOrderDefault=release
    // default.settings.default.Language=en-US
    // default.settings.default.RemoveBelowMinimum=false
    // default.settings.default.RemoveNotManaged=false
    // default.settings.default.RemoveNotScheduled=false
    // default.settings.default.Schedule=0 0 4 ? * SUN *
    // default.settings.default.ShowNotManaged=true
    // default.settings.default.SyncMode=append

    return (
        <Stack
            spacing={1}
            direction="column"
            justifyContent="space-between"
            useFlexGap
            sx={{flexWrap: 'wrap'}}
        >
            <Typography>Default Settings</Typography>
        </Stack>
    );
}

DefaultSettingsForm.propTypes = {};

export default DefaultSettingsForm;
