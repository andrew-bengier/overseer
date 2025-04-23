const axios = require('axios');
import {generateJsonHeaders} from "../utils/httpServiceUtils";

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/lookups";

// region - Settings -
export async function getDefaultSettings(level) {
    return await axios.get(BASE_URL + '/settings/defaults',
        {
            headers: generateJsonHeaders(),
            params: {level: level}
        });
}

export async function getSettingLevels() {
    return await axios.get(BASE_URL + '/settings/levels', {headers: generateJsonHeaders()});
}

export async function getSettingTypes() {
    return await axios.get(BASE_URL + '/settings/types', {headers: generateJsonHeaders()});
}

// endregion - Settings (Defaults) -

// region - Actions -
export async function getActionCategories() {
    return await axios.get(BASE_URL + '/actions/categories', {headers: generateJsonHeaders()});
}

export async function getActionTypes() {
    return await axios.get(BASE_URL + '/actions/types', {headers: generateJsonHeaders()});
}

// endregion - Actions -

// region - ApiKeys -
export async function getApiKeyTypes() {
    return await axios.get(BASE_URL + '/apikeys/types', {headers: generateJsonHeaders()});
}

// endregion - ApiKeys -

// region - Builders -
export async function getBuilderOptions() {
    return await axios.get(BASE_URL + '/builders/options', {headers: generateJsonHeaders()});
}

export async function getBuilderCategories() {
    return await axios.get(BASE_URL + '/builders/categories', {headers: generateJsonHeaders()});
}

export async function getBuilderTypes() {
    return await axios.get(BASE_URL + '/builders/types', {headers: generateJsonHeaders()});
}

// endregion - Builders -

// region - Collections -
export async function getCollectionTrackingTypes() {
    return await axios.get(BASE_URL + '/collections/trackingTypes', {headers: generateJsonHeaders()});
}

// endregion - Collections -

// region - Media -
export async function getMediaTypes() {
    return await axios.get(BASE_URL + '/media/types', {headers: generateJsonHeaders()});
}

export async function getMediaIdTypes() {
    return await axios.get(BASE_URL + '/mediaIds/types', {headers: generateJsonHeaders()});
}

export async function getMediaImageTypes() {
    return await axios.get(BASE_URL + '/mediaImages/types', {headers: generateJsonHeaders()});
}

// endregion - Media -
