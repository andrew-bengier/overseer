const axios = require('axios');
import {generateJsonHeaders} from "../utils/httpServiceUtils";

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/servers/{serverId}/libraries";
const LIBRARY_ID_URL = temp_base_url + "/api/servers/{serverId}/libraries/{libraryId}";

// region - CREATE -
// endregion - CREATE -

// region - READ -
export async function getLibraries(serverId) {
    if (serverId != null) {
        return await axios.get(BASE_URL.replace('{serverId}', serverId),
            {
                headers: generateJsonHeaders()
            });
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

export async function getLibrary(serverId, libraryId) {
    if (serverId != null && libraryId != null) {
        return await axios.get(LIBRARY_ID_URL
                .replace('{serverId}', serverId)
                .replace('{libraryId}', libraryId),
            {
                headers: generateJsonHeaders()
            });
    } else if (libraryId == null) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - READ -

// region - UPDATE -
export async function updateLibrary(library) {
    if ('serverId' in library && 'id' in library) {
        return await axios.put(LIBRARY_ID_URL
                .replace('{serverId}', library.serverId)
                .replace('{libraryId}', library.id),
            library,
            {
                headers: generateJsonHeaders()
            });
    } else if (!'id' in library) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

export async function updateLibrarySettings(library) {
    // TODO: move common path component to reusable location
    if ('serverId' in library && 'id' in library) {
        return await axios.put(LIBRARY_ID_URL
            .replace('{serverId}', library.serverId)
            .replace('{libraryId}', library.id) + '/settings',
            library,
            {
                headers: generateJsonHeaders()
            });
    } else if (!'id' in library) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

export async function updateLibraryActions(library) {
    // TODO: move common path component to reusable location
    if ('serverId' in library && 'id' in library) {
        return await axios.put(LIBRARY_ID_URL
            .replace('{serverId}', library.serverId)
            .replace('{libraryId}', library.id) + '/actions',
            library,
            {
                headers: generateJsonHeaders()
            });
    } else if (!'id' in library) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

export async function updateLibraryActive(serverId, libraryId, active = false) {
    // TODO: move common path component to reusable location
    if (serverId != null && libraryId != null) {
        return await axios.patch(LIBRARY_ID_URL
            .replace('{serverId}', serverId)
            .replace('{libraryId}', libraryId) + '/active',
            null,
            {
                params: {
                    active
                }
            });
    } else if (libraryId == null) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - UPDATE -

// region - DELETE -
export async function removeLibrary(serverId, libraryId) {
    // TODO: move common path component to reusable location
    if (serverId != null && libraryId != null) {
        return await axios.delete(LIBRARY_ID_URL
            .replace('{serverId}', serverId)
            .replace('{libraryId}', libraryId));
    } else if (libraryId == null) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - DELETE -

// region - TEST -
// TODO: check if these have been updated / need to be used
// post LIBRARY_ID_URL + /test/processCheck
// post LIBRARY_ID_URL + /test/assetsArchive
// get LIBRARY_ID_URL + /test/getMediaInfo
// get LIBRARY_ID_URL + /test/checkArchiveEligible
// post LIBRARY_ID_URL + /test/archive
// endregion - TEST -