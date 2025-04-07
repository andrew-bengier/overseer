const axios = require('axios');

const BASE_URL = "/api/servers";

export async function getServers(searchParams) {
    // TODO: add searchParams to get specific server (by id)
    return await axios.get(BASE_URL);
}
