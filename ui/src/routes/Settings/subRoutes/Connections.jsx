import React from "react";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import ConnectionSettingsForm from "../../../components/forms/connection/ConnectionSettingsForm";
import {useIntl} from "react-intl";

function Connections() {
    const {formatMessage} = useIntl();
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
        </React.Fragment>
    );
}

Connections.propTypes = {}

export default Connections;
