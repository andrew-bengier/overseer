import React from "react";
import {useIntl} from "react-intl";
import {Stack} from "@mui/material";
import ISO6391 from 'iso-639-1';
import SelectFormField from "../fields/SelectFormField";
import SplitSelectFormField from "../fields/SplitSelectFormField";

// TODO: update here with supported languages


function LanguageSettingsForm({}) {
    const {formatMessage} = useIntl();
    const [infoLanguage, setInfoLanguage] = React.useState("");
    const [uiLanguage, setUiLanguage] = React.useState("");
    const [infoLanguageOptions, setInfoLanguageOptions] = React.useState([]);
    const [uiLanguageOptions, setUiLanguageOptions] = React.useState([]);

    React.useEffect(() => {
        const languages = ISO6391.getLanguages(ISO6391.getAllCodes());
        const mappedLanguages = languages.map(language => ({
            key: language.code,
            value: language.name,
            display: language.nativeName,
        }));
        setInfoLanguageOptions(mappedLanguages);
        setUiLanguageOptions(mappedLanguages);
    }, []);

    const handleFormChange = (key, value) => {
        console.log(key + " " + value);
        switch (key) {
            case 'info':
                setInfoLanguage(value);
                break;
            case 'ui':
                setUiLanguage(value);
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
                id="infoLanguage"
                label={formatMessage({id: 'src.components.forms.language.infoLanguageSelectLabel'})}
                value={infoLanguage}
                options={infoLanguageOptions}
                multiple={false}
                helperText={""}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
            <SplitSelectFormField
                id="uiLanguage"
                label={formatMessage({id: 'src.components.forms.language.uiLanguageSelectLabel'})}
                value={uiLanguage}
                options={uiLanguageOptions}
                helperText={""}
                disabled={false}
                fullwidth={false}
                externalLabel={true}
                handleChange={handleFormChange}
            />
        </Stack>
    );
}

LanguageSettingsForm.propTypes = {}

export default LanguageSettingsForm;
