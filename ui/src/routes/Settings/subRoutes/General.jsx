import React from "react";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import LoggingSettingsForm from "../../../components/forms/logging/LoggingSettingsForm";
import {useIntl} from "react-intl";

function General() {
    const {formatMessage} = useIntl();

    return (
        <React.Fragment>
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.logging.loggingSettingsSectionTitle'})}
                width="60%"
                content={<LoggingSettingsForm/>}
            />
        </React.Fragment>
    )
}

General.propTypes = {}

export default General;
