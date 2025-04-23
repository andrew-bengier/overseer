const axios = require('axios');
import {generateFileHeaders, generateJsonHeaders} from "../utils/httpServiceUtils";

const temp_base_url = 'http://localhost:8080'
const BASE_URL = temp_base_url + "/api/info";

export async function getApiInfo() {
    // return await axios.get('/api/info');
    return await axios.get(BASE_URL, {headers: generateJsonHeaders()});
}

export async function getGitInfo() {
    return await axios.get(BASE_URL + '/git', {headers: generateJsonHeaders()});
}

export async function getLogFiles() {
    return await axios.get(BASE_URL + '/logFiles', {headers: generateJsonHeaders()});
}

export async function getLogFile(filePath) {
    return await axios.post(BASE_URL + '/logFiles/file', filePath, {
        headers: generateFileHeaders()
    });
}