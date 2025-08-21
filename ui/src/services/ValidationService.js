import {toast} from 'react-toastify';
// import {generateJsonHeaders} from '../utils/httpServiceUtils';

// const axios = require('axios');

// const temp_base_url = 'http://localhost:8080'

export async function getAppRequirements() {
    // Check base application requirements:
    // - apikey -> tmdb
    // - at least one media server -> plex
    // -
    // return await axios.post(temp_base_url + '/api/apikeys/requirements', {
    //         headers: generateJsonHeaders(),
    //     });
    return {
        data: {
            apiKeys: [
                // {
                //     id: '01989a89-6fe8-7c99-ad0b-ca0fe909964a',
                //     name: 'TMDB',
                //     key: '1234567890',
                //     url: 'https://www.themoviedb.org/'
                // },
            ],
            mediaServers: [
                {
                    id: '01989a89-9491-7e8a-8740-cd595339cef2',
                    name: 'PLEX',
                    key: '1234567890',
                    url: 'http://192.168.1.185:32400/web/index.html'
                },
            ]
        }, status: 200
    };
}

export function checkAppRequirements(requirements, navigate) {
    if (requirements) {
        if (requirements.apiKeys?.length === 0) {
            toast.error('No api keys found. Please add at the required api keys.');
            // navigate('/Settings/Connections', {state: {openAddEditConnectionModal: true}});
            navigate('/Settings/Connections');
        }

        if (requirements.mediaServers?.length === 0) {
            toast.error('No media servers found. Please add at least one media server.');
            navigate('/Settings/MediaManagement');
        }
    }

    return null;
}
