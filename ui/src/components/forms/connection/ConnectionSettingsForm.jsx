import React from "react";
import {Button, InputLabel, MenuItem, Select, Stack, TextField} from "@mui/material";
import PropTypes from "prop-types";
import {FormattedMessage, useIntl} from "react-intl";
import {useSelector} from "react-redux";
import {plexLogin} from "../../../services/MediaServerConnectionService";

function ConnectionSettingsForm({type, data}) {
    const test = useSelector((state) => state);
    // const token = useSelector((state) => state.token?.plexToken);
    const {formatMessage} = useIntl();
    const [connectionData, setConnectionData] = React.useState(data);
    const [typeOptions,] = React.useState(["Plex"]);

    // React.useEffect(() => {
    //     console.log('State:', test);
    //     console.log('Token:', token);
    //     if (token) {
    //         setConnectionData({...connectionData, key: token});
    //     }
    // }, [token]);

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

    const handleTest = async () => {
        console.log(connectionData);

        plexLogin();
        // let authUrl = await plexLogin(true);
        // console.log('Auth url:', authUrl);
        // setAuthUrl(authUrl);

        // return await plexLogin(true).then(url => {
        //     setAuthUrl(url);
        //     setExternalPopup(true);
        //     return false;
        // });
    }

    function renderPlexConnectionForm() {
        const isFormValid = () => {
            return connectionData.name != null && connectionData.url != null;
        }

        // if (!token) {
        //     return (<PlexLoginComponent/>);
        // } else {
        return (
            <Stack
                spacing={1}
                direction="column"
                justifyContent="space-between"
                useFlexGap
                sx={{flexWrap: 'wrap'}}
            >
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
                        validators={['required']}
                        errorMessages={['Connection Name is required']}
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
                        validators={['required']}
                        errorMessages={['Url is required']}
                    />
                </React.Fragment>
                {connectionData.key &&
                    <React.Fragment id={'api-key-field-container'}>
                        <InputLabel id={'connectionApiKeyFormFieldLabel'} sx={{paddingTop: '7px'}}>
                            <FormattedMessage
                                id={'src.components.forms.connection.connectionApiKeyFormFieldLabel'}/>
                        </InputLabel>
                        <TextField
                            labelId={'connectionApiKeyFormFieldLabel'}
                            id={'connectionApiKeyFormFieldLabel-input'}
                            value={connectionData.key}
                            disabled
                            variant="outlined"
                            sx={{
                                ...({
                                    width: '60%',
                                    paddingLeft: '10px'
                                })
                            }}
                        />
                    </React.Fragment>
                }
                {/*<OauthPopup*/}
                {/*    url={authUrl}*/}
                {/*    onCode={onCode}*/}
                {/*    onClose={onClose}*/}
                {/*>*/}
                <Button
                    onClick={() => handleTest()}
                    disabled={isFormValid}
                >
                    {connectionData.key ? 'Get Plex Token' : 'Refresh Plex Token'}
                </Button>
                {/*</OauthPopup>*/}
            </Stack>
        )
        // }
    }

    function renderOtherConnectionForm() {
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

    if (type === 'plex') {
        return renderPlexConnectionForm();
    } else {
        return renderOtherConnectionForm();
    }
}

ConnectionSettingsForm.propTypes = {
    type: PropTypes.string.isRequired,
    data:
    PropTypes.object.isRequired
}

export default ConnectionSettingsForm;
