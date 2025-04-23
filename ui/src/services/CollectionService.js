import {generateJsonHeaders} from "../utils/httpServiceUtils";

const axios = require('axios');

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/servers/{serverId}/libraries/{libraryId}/collections";

export async function getCollections(serverId, libraryId, options) {
    let config = {headers: generateJsonHeaders()};

    if (options) {
        config = {
            ...config, params: {
                options: options
            }
        };
    }

    return await axios.get(
        BASE_URL.replace('{serverId}', serverId)
            .replace('{libraryId}', libraryId),
        config);
}
