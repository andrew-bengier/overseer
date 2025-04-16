import React from "react";
import {listSplitDateFormats} from "../../../utils/dateUtils";
import {longDateFormats, shortDateFormats, timeFormats} from "../../../utils/formatUtils";
import {useIntl} from "react-intl";
import {Stack} from "@mui/material";
import SplitSelectFormField from "../fields/SplitSelectFormField";

function DateSettingsForm({}) {
    const {formatMessage} = useIntl();
    const [shortDateFormat, setShortDateFormat] = React.useState("");
    const [longDateFormat, setLongDateFormat] = React.useState("");
    const [timeFormat, setTimeFormat] = React.useState("");
    const [shortDateFormatOptions] = React.useState(listSplitDateFormats(shortDateFormats));
    const [longDateFormatOptions] = React.useState(listSplitDateFormats(longDateFormats));
    const [timeFormatOptions] = React.useState(listSplitDateFormats(timeFormats));

    const handleFormChange = (key, value) => {
        console.log(key + " " + value);
        switch (key) {
            case 'shortDateFormat':
                setShortDateFormat(value);
                break;
            case 'longDateFormat':
                setLongDateFormat(value);
                break;
            case 'timeFormat':
                setTimeFormat(value);
                break;
        }
    }

    return (
        <Stack
            spacing={1}
            direction="column"
            justifyContent="space-between"
            useFlexGap
            sx={{flexWrap: 'wrap'}}
        >
            <SplitSelectFormField
                id="shortDateFormat"
                label={formatMessage({id: 'src.components.forms.dates.shortDateFormatSelectLabel'})}
                value={shortDateFormat}
                options={shortDateFormatOptions}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
            <SplitSelectFormField
                id="longDateFormat"
                label={formatMessage({id: 'src.components.forms.dates.longDateFormatSelectLabel'})}
                value={longDateFormat}
                options={longDateFormatOptions}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
            <SplitSelectFormField
                id="timeFormat"
                label={formatMessage({id: 'src.components.forms.dates.timeFormatSelectLabel'})}
                value={timeFormat}
                options={timeFormatOptions}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
        </Stack>
    );
}

DateSettingsForm.propTypes = {}

export default DateSettingsForm;
