import React from "react";
import {Button, InputLabel, MenuItem, Select, Stack, TextField} from "@mui/material";
import PropTypes from "prop-types";
import {FormattedMessage, useIntl} from "react-intl";
import {testPlexAccess} from '../../../services/MediaServerConnectionService.js';

function ConnectionSettingsForm({type}) {
    const {formatMessage} = useIntl();
    const [connectionData, setConnectionData] = React.useState({});
    const [typeOptions,] = React.useState(["Plex"]);

    const handleFormChange = (key, value) => {
        // console.log(key + " " + value);
        switch (key) {
            case 'connectionType':
                setConnectionData({...connectionData, type: value});
                break;
            case 'connectionName':
                setConnectionData({...connectionData, name: value});
                break;
            case 'connectionApiKey':
                setConnectionData({...connectionData, key: value});
                break;
            case 'connectionUrl':
                setConnectionData({...connectionData, url: value});
                break;
        }
    }

    const handleTest = () => {
        console.log(connectionData);

        testPlexAccess();
    }

    return (
        <Stack
            spacing={1}
            direction="column"
            justifyContent="space-between"
            useFlexGap
            sx={{flexWrap: 'wrap'}}
        >
            {/* TODO:
            - Title (type): plex
            - id (if not new)
            - name: server name
            - api key: key
            - url: http://localhost:32400
            - information icon to wiki/instructions
         */}
            <React.Fragment id={'type-field-container'}>
                <InputLabel id={'connectionTypeFormFieldLabel'} sx={{paddingTop: '7px'}}>
                    <FormattedMessage id={'src.components.forms.connection.connectionTypeFormFieldLabel'}/>
                </InputLabel>
                <Select
                    labelId={'connectionTypeFormFieldLabel'}
                    id={'connectionTypeFormFieldLabel-input'}
                    value={connectionData.type || ""}
                    // label={formatMessage({id: 'src.components.forms.connection.connectionTypeFormFieldLabel'})}
                    onChange={(event) => handleFormChange('connectionType', event.target.value)}
                    variant="outlined"
                    sx={{
                        ...({
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                >
                    {typeOptions.map((option) => (
                        <MenuItem key={option}>
                            {option}
                        </MenuItem>
                    ))}
                </Select>
            </React.Fragment>
            <React.Fragment id={'name-field-container'}>
                <InputLabel id={'connectionNameFormFieldLabel'} sx={{paddingTop: '7px'}}>
                    <FormattedMessage id={'src.components.forms.connection.connectionNameFormFieldLabel'}/>
                </InputLabel>
                <TextField
                    labelId={'connectionNameFormFieldLabel'}
                    id={'connectionNameFormFieldLabel-input'}
                    value={connectionData.name || ""}
                    // label={formatMessage({id: 'src.components.forms.connection.connectionNameFormFieldLabel'})}
                    onChange={(event) => handleFormChange('connectionName', event.target.value)}
                    variant="outlined"
                    sx={{
                        ...({
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                />
            </React.Fragment>
            <React.Fragment id={'api-key-field-container'}>
                <InputLabel id={'connectionApiKeyFormFieldLabel'} sx={{paddingTop: '7px'}}>
                    <FormattedMessage id={'src.components.forms.connection.connectionApiKeyFormFieldLabel'}/>
                </InputLabel>
                <TextField
                    labelId={'connectionApiKeyFormFieldLabel'}
                    id={'connectionApiKeyFormFieldLabel-input'}
                    value={connectionData.key || ""}
                    // label={formatMessage({id: 'src.components.forms.connection.connectionApiKeyFormFieldLabel'})}
                    onChange={(event) => handleFormChange('connectionApiKey', event.target.value)}
                    variant="outlined"
                    sx={{
                        ...({
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                />
            </React.Fragment>
            <React.Fragment id={'url-field-container'}>
                <InputLabel id={'connectionUrlFormFieldLabel'} sx={{paddingTop: '7px'}}>
                    <FormattedMessage id={'src.components.forms.connection.connectionUrlFormFieldLabel'}/>
                </InputLabel>
                <TextField
                    labelId={'connectionUrlFormFieldLabel'}
                    id={'connectionUrlFormFieldLabel-input'}
                    value={connectionData.url || ""}
                    // label={formatMessage({id: 'src.components.forms.connection.connectionUrlFormFieldLabel'})}
                    onChange={(event) => handleFormChange('connectionUrl', event.target.value)}
                    variant="outlined"
                    sx={{
                        ...({
                            width: '60%',
                            paddingLeft: '10px'
                        })
                    }}
                />
            </React.Fragment>
            <Button onClick={handleTest}>Test</Button>
        </Stack>
    );
}

ConnectionSettingsForm.propTypes = {
    type: PropTypes.string.isRequired,
}

export default ConnectionSettingsForm;
