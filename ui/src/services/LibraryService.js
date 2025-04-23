const axios = require('axios');
import {generateJsonHeaders} from "../utils/httpServiceUtils";

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/servers/{serverId}/libraries";

export async function getLibraries(serverId) {
    return await axios.get(BASE_URL.replace('{serverId}', serverId), {headers: generateJsonHeaders()});
}
