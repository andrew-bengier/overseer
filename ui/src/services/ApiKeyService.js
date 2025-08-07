const axios = require('axios');

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/apikeys";

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
