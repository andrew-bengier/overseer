import React from "react";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import LoggingSettingsForm from "../../../components/forms/logging/LoggingSettingsForm";
import {useIntl} from "react-intl";
import DefaultSettingsSection from "../../../components/sections/defaultSettings/DefaultSettingsSection";

function General() {
    const {formatMessage} = useIntl();

    // Host
    // - Port Number
    // Security
    // - Authentication
    // - Authentication Required
    // - Username/password
    // - API key
    // Logging
    // - level
    // Backups
    // - folder
    // - interval
    // - retention
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
        <React.Fragment>
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.logging.loggingSettingsSectionTitle'})}
                width="60%"
                content={<LoggingSettingsForm/>}
            />
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.defaults.defaultSettingsSectionTitle'})}
                width="60%"
                content={<DefaultSettingsSection initTabs={["test"]}/>}
            />
        </React.Fragment>
    )
}

General.propTypes = {}

export default General;
