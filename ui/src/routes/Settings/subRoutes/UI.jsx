import React from "react";
import CalendarSettingsForm from "../../../components/forms/calendar/CalendarSettingsForm";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import {useIntl} from "react-intl";
import DateSettingsForm from "../../../components/forms/dates/DateSettingsForm";

function UI() {
    const {formatMessage} = useIntl();

    return (
        <React.Fragment>
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.calendar.calendarSettingsSectionTitle'})}
                width="60%"
                content={<CalendarSettingsForm/>}
            />
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.dates.datesSettingsSectionTitle'})}
                width="60%"
                content={<DateSettingsForm/>}
            />
        </React.Fragment>
    )
}

UI.propTypes = {}

export default UI;
