const axios = require('axios');

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/servers";

export async function getServers(searchParams) {
    // TODO: add searchParams to get specific server (by id)
    return await axios.get(BASE_URL);
}
