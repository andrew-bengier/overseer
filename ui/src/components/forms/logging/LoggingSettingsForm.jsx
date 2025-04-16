import React from "react";
import {useIntl} from "react-intl";
import SelectFormField from "../fields/SelectFormField";
import {Stack} from "@mui/material";

function LoggingSettingsForm({currentLoggingLevel = 'info'}) {
    const {formatMessage} = useIntl();
    const [options, setOptions] = React.useState([]);
    const [loggingLevel, setLoggingLevel] = React.useState(currentLoggingLevel);

    React.useEffect(() => {
        setOptions([
            {value: 'info', display: formatMessage({id: 'src.component.forms.logging.loggingLevelOptionInfo'})},
            {value: 'debug', display: formatMessage({id: 'src.component.forms.logging.loggingLevelOptionDebug'})},
            {value: 'trace', display: formatMessage({id: 'src.component.forms.logging.loggingLevelOptionTrace'})}
        ]);
    }, []);

    const handleFormChange = (key, value) => {
        console.log(key + " " + value);
        switch (key) {
            case 'loggingLevel':
                setLoggingLevel(value);
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
            <SelectFormField
                id="loggingLevel"
                label={formatMessage({id: 'src.component.forms.logging.loggingLevelSelectLabel'})}
                value={loggingLevel}
                options={options}
                multiple={false}
                helperText={""}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
        </Stack>
    );
}

LoggingSettingsForm.propTypes = {}

export default LoggingSettingsForm;
