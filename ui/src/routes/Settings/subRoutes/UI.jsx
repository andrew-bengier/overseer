import React from "react";
import CalendarSettingsForm from "../../../components/forms/calendar/CalendarSettingsForm";
import SettingsSection from "../../../components/layouts/sections/SettingsSection";
import {useIntl} from "react-intl";
import DateSettingsForm from "../../../components/forms/dates/DateSettingsForm";

function UI() {
    const {formatMessage} = useIntl();

    return (
        <React.Fragment>
            <SettingsSection
                header={formatMessage({id: 'src.routes.settings.calendar.calendarSettingsSectionTitle'})}
                content={<CalendarSettingsForm/>}
            />
            <SettingsSection
                header={formatMessage({id: 'src.routes.settings.dates.datesSettingsSectionTitle'})}
                content={<DateSettingsForm/>}
            />
        </React.Fragment>
    )
}

UI.propTypes = {}

export default UI;
