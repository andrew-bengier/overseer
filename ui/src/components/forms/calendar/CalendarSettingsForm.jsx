import React from "react";
import {Stack} from "@mui/material";
import SelectFormField from "../fields/SelectFormField";
import {useIntl} from "react-intl";
import {listSplitDateFormats} from "../../../utils/dateUtils";
import {columnDateFormats} from "../../../utils/formatUtils";
import SplitSelectFormField from "../fields/SplitSelectFormField";

function CalendarSettingsForm({}) {
    const {formatMessage} = useIntl();
    const [startOfWeek, setStartOfWeek] = React.useState("");
    const [startOfWeekOptions, setStartOfWeekOptions] = React.useState([]);
    const [columnDateFormat, setColumnDateFormat] = React.useState("");
    const [columnDateFormatOptions] = React.useState(listSplitDateFormats(columnDateFormats));

    React.useEffect(() => {
        setStartOfWeekOptions([
            formatMessage({id: 'src.components.forms.calendar.calendarStartOfWeekSelectOption_1'}),
            formatMessage({id: 'src.components.forms.calendar.calendarStartOfWeekSelectOption_2'})
        ]);
    }, []);

    const handleFormChange = (key, value) => {
        console.log(key + " " + value);
        switch (key) {
            case 'startOfWeek':
                setStartOfWeek(value);
                break;
            case 'columnDateFormat':
                setColumnDateFormat(value);
                break;
        }
        setStartOfWeek(value);
    }

    return (
        <Stack
            spacing={1}
            direction="column"
            justifyContent="space-between"
            useFlexGap
            sx={{flexWrap: 'wrap'}}
        >
            <SelectFormField
                id="startOfWeek"
                label={formatMessage({id: 'src.components.forms.calendar.calendarStartOfWeekSelectLabel'})}
                value={startOfWeek}
                options={startOfWeekOptions}
                multiple={false}
                helperText={""}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
            <SplitSelectFormField
                id="columnDateFormat"
                label={formatMessage({id: 'src.components.forms.calendar.calendarColumnDateFormatSelectLabel'})}
                value={columnDateFormat}
                options={columnDateFormatOptions}
                helperText={formatMessage({id: 'src.components.forms.calendar.calendarColumnDateFormatSelectHelperText'})}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
        </Stack>
    );
}

CalendarSettingsForm.propTypes = {}

export default CalendarSettingsForm;
