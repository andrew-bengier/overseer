import React from "react";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import ConnectionSettingsForm from "../../../components/forms/connection/ConnectionSettingsForm";
import {useIntl} from "react-intl";
import AddEditConnection from "../../../components/modals/connections/AddEditConnection";
import {useLocation} from "react-router-dom";

function Connections() {
    const location = useLocation();
    const {state} = location;
    const {formatMessage} = useIntl();
    const [addEditConnectionOpen, setAddEditConnectionOpen] = React.useState(state?.openAddEditConnectionModal || false);

    React.useEffect(() => {
        console.log(state);
    }, [state])

    const handleAddEditClose = () => {
        setAddEditConnectionOpen(false);
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
            Connections (Trackers and Builders)
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.connections.buildersSettingsSectionTitle'})}
                width="60%"
                content={<ConnectionSettingsForm/>}
            />
            <BlockSection
                header={formatMessage({id: 'src.routes.settings.connections.trackersSettingsSectionTitle'})}
                width="60%"
                content={<ConnectionSettingsForm/>}
            />
            <AddEditConnection isOpen={addEditConnectionOpen} handleClose={handleAddEditClose}/>
        </React.Fragment>
    );
}

Connections.propTypes = {}

export default Connections;
