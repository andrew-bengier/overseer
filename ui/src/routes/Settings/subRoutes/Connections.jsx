import React from "react";
import {useIntl} from "react-intl";
import {useLocation, useSearchParams} from "react-router-dom";
import {handlePlexCallback, plexLogin} from "../../../services/MediaServerConnectionService";
import {Button} from "@mui/material";
import {useDispatch, useSelector} from "react-redux";

function Connections() {
    const location = useLocation();
    const dispatch = useDispatch();
    const {state} = location;
    const {formatMessage} = useIntl();
    const [searchParams, setSearchParams] = useSearchParams();

    const requirements = useSelector((state) => state.requirements);
    // const plexConnection = useSelector((state) => state.plexAuth);
    const [addEditConnectionOpen, setAddEditConnectionOpen] = React.useState(state?.openAddEditConnectionModal || false);
    const [connectionData, setConnectionData] = React.useState();

    const [loginWindow, setLoginWindow] = React.useState(null);

    const openLoginWindow = async () => {
        const url = await plexLogin(null, true);
        const newWindow = window.open(url, '_blank', 'width=500,height=700');
        setLoginWindow(newWindow);
    }

    // // [TEST]
    // React.useEffect(() => {
    //     console.log('Collections state:', state);
    // }, [state]);

    React.useEffect(() => {
        console.log('Requirements state:', requirements);
    }, [requirements]);

    React.useEffect(() => {
        const plexPin = searchParams.get('plexPin');
        // const plexName = searchParams.get('plexName');
        // const plexUrl = searchParams.get('plexUrl');

        if (plexPin) {
            if (loginWindow && !loginWindow.closed) {
                loginWindow.close();
                window.close()
                setLoginWindow(null);
            }
            if (window.opener) {
                opener.location.href = '?plexPin=' + plexPin;
                window.close();
            }

            // let plexAuth = {
            //     name: plexName,
            //     url: plexUrl,
            //     key: plexPin,
            // };
            // console.log('Plex auth:', plexAuth);

            handlePlexCallback(plexPin).then(response => {
                // console.log('Plex auth Token:', response);
                // update requirements here via api
                // dispatch(updateToken(response));

                setConnectionData({type: 'plex', pin: plexPin, key: response});
                // setAddEditConnectionOpen(true);
            });
        }
    }, [searchParams]);

    const handleAddEditOpen = (type) => {
        setAddEditConnectionOpen(true);
        setConnectionData({...connectionData, type: type});
    }

    const handleAddEditClose = (data) => {
        setAddEditConnectionOpen(false);
        console.log("Before connection data clearing: ", data);
        console.log("clearing connection data: ", connectionData);
        setConnectionData({});
    }

    // Trackers:
    // - Radarr
    // - Sonarr
    // Buidlers:
    // - Tmdb
    // - Imdb
    // - Trakt
    // - Plex
    // - Tvdb
    // - Tautulli
    // - Anidb
    // - more...
    //- Custom

    return (
        <React.Fragment>
            Connections
            <Button onClick={openLoginWindow}>
                Click me
            </Button>

            {connectionData &&
                <pre>{JSON.stringify(connectionData, null, 2)}</pre>
            }
            {/*<BlockSection*/}
            {/*    header={formatMessage({id: 'src.routes.settings.connections.requiredSettingsSectionTitle'})}*/}
            {/*    width="60%"*/}
            {/*    content={*/}
            {/*        <Stack direction="row" spacing={2}>*/}
            {/*            <BasicInfoCard information={{name: "Plex"}}*/}
            {/*                           handleClick={() => handleAddEditOpen('plex')}/>*/}
            {/*            <BasicInfoCard information={{name: "TMDB"}}*/}
            {/*                           handleClick={() => handleAddEditOpen('tmdb')}/>*/}
            {/*            <AddCard handleClick={() => handleAddEditOpen(null)}/>*/}
            {/*        </Stack>*/}
            {/*    }*/}
            {/*/>*/}
            {/*<BlockSection*/}
            {/*    header={formatMessage({id: 'src.routes.settings.connections.buildersSettingsSectionTitle'})}*/}
            {/*    width="60%"*/}
            {/*    // content={*/}
            {/*    //     // <ConnectionSettingsForm/>*/}
            {/*    // }*/}
            {/*/>*/}
            {/*<BlockSection*/}
            {/*    header={formatMessage({id: 'src.routes.settings.connections.trackersSettingsSectionTitle'})}*/}
            {/*    width="60%"*/}
            {/*    // content={*/}
            {/*    //     // <ConnectionSettingsForm/>*/}
            {/*    // }*/}
            {/*/>*/}
            {/*<AddEditConnection isOpen={addEditConnectionOpen} type={connectionData.type} connectionData={connectionData}*/}
            {/*                   handleClose={handleAddEditClose}/>*/}
        </React.Fragment>
    );
}

Connections.propTypes = {}

export default Connections;
