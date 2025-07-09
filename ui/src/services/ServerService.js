import {generateJsonHeaders} from "../utils/httpServiceUtils";

const axios = require('axios');

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/servers";
const SERVER_ID_URL = temp_base_url + "/api/servers/{serverId}";

// region - CREATE -
export async function addServer(server, includeLibraries = false) {
    return await axios.post(BASE_URL, server, {
        headers: generateJsonHeaders(),
        params: {
            includeLibraries
        }
    });
}

// endregion - CREATE -

// region - READ -
export async function getServers(searchParams) {
    if ('serverId' in searchParams) {
        return await axios.get(SERVER_ID_URL.replace('{serverId}', searchParams.serverId),
            {
                headers: generateJsonHeaders()
            });
    } else {
        return await axios.get(BASE_URL,
            {
                headers: generateJsonHeaders()
            });
    }
}

// endregion - READ -

// region - UPDATE -
export async function updateServer(server) {
    if ('id' in server) {
        return await axios.put(SERVER_ID_URL.replace('{serverId}', server.id),
            server,
            {
                headers: generateJsonHeaders()
            });
    } else {
        throw new Error('Invalid server - missing id');
    }
}

export async function updateServerSettings(server) {
    // TODO: move common path component to reusable location
    if ('id' in server) {
        return await axios.put(SERVER_ID_URL.replace('{serverId}', server.id) + '/settings',
            server,
            {
                headers: generateJsonHeaders()
            });
    } else {
        throw new Error('Invalid server - missing id');
    }
}

export async function updateServerActions(server) {
    // TODO: move common path component to reusable location
    if ('id' in server) {
        return await axios.put(SERVER_ID_URL.replace('{serverId}', server.id) + '/actions',
            server,
            {
                headers: generateJsonHeaders()
            });
    } else {
        throw new Error('Invalid server - missing id');
    }
}

export async function updateServerResync(serverId) {
    // TODO: move common path component to reusable location
    if (serverId != null) {
        return await axios.put(SERVER_ID_URL.replace('{serverId}', serverId) + '/resync');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

export async function updateServerActive(serverId, active = false) {
    // TODO: move common path component to reusable location
    if (serverId != null) {
        return await axios.patch(SERVER_ID_URL.replace('{serverId}', serverId) + '/active',
            null,
            {
                params: {
                    active
                }
            });
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - UPDATE -

// region - DELETE -
export async function removeServer(serverId) {
    // TODO: move common path component to reusable location
    if (serverId != null) {
        return await axios.delete(SERVER_ID_URL.replace('{serverId}', serverId));
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - DELETE -
