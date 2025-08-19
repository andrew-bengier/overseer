const axios = require('axios');

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/apikeys";

export const mediaServerTypes = [
    {name: 'Plex'},
    {name: 'Jellyfin'},
    {name: 'Emby'},
    {name: 'Kodi'},
];

// region - CREATE -
export async function addApiKey(apiKey) {
    return null;
}

// endregion - CREATE -

// region - READ -
export async function getApiKeys(searchParams) {
    // return await axios.get(BASE_URL,
    //     {
    //         headers: generateJsonHeaders(),
    //         params: {
    //             requestParams: searchParams
    //         }
    //     });
    return {data: null, status: 200};
}

// endregion - READ -

// region - TEST -
export async function testApiCredentials(apiKey) {
//    const response = await axios.get(apiKey.url, )
    return {};
}
// endregion - TEST -
