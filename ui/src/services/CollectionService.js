import {generateJsonHeaders} from "../utils/httpServiceUtils";

const axios = require('axios');

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/servers/{serverId}/libraries/{libraryId}/collections";
const COLLECTION_ID_URL = temp_base_url + "/api/servers/{serverId}/libraries/{libraryId}/collections/{collectionId}";

// region - CREATE -
export async function addCollection(library, collection, process = false) {
    if ('serverId' in library && 'libraryId' in collection) {
        return await axios.post(BASE_URL
                .replace('{serverId}', library.serverId)
                .replace('{libraryId}', collection.libraryId),
            collection, {
                headers: generateJsonHeaders(),
                params: {
                    process
                }
            });
    }
}

// endregion - CREATE -

// region - READ -
export async function getCollections(serverId, libraryId, searchParams) {
    if (serverId != null && libraryId != null) {
        return await axios.get(BASE_URL
                .replace('{serverId}', serverId)
                .replace('{libraryId}', libraryId),
            {
                headers: generateJsonHeaders(),
                params: {
                    options: searchParams
                }
            });
    } else if (serverId != null) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

export async function getCollection(serverId, libraryId, collectionId) {
    if (serverId != null && libraryId != null) {
        return await axios.get(COLLECTION_ID_URL
                .replace('{serverId}', serverId)
                .replace('{libraryId}', libraryId)
                .replace('{collectionId}', collectionId),
            {
                headers: generateJsonHeaders()
            });
    } else if (collectionId == null) {
        throw new Error('Invalid request - missing collectionId');
    } else if (libraryId == null) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - READ -

// region - UPDATE -
export async function processCollection(serverId, libraryId, collectionId) {
    // TODO: move common path component to reusable location
    if (serverId != null && libraryId != null) {
        return await axios.put(COLLECTION_ID_URL
            .replace('{serverId}', serverId)
            .replace('{libraryId}', libraryId)
            .replace('{collectionId}', collectionId) + '/process',
            {
                headers: generateJsonHeaders()
            });
    } else if (collectionId == null) {
        throw new Error('Invalid request - missing collectionId');
    } else if (libraryId == null) {
        throw new Error('Invalid request - missing libraryId');
    } else {
        throw new Error('Invalid request - missing serverId');
    }
}

// endregion - UPDATE -

// region - DELETE -
// endregion - DELETE -

// region - TEST -
// TODO: check if these have been updated / need to be used
// get COLLECTION_ID_URL + /test
// endregion - TEST -
