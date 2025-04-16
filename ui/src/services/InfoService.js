const axios = require('axios');
import {generateFileHeaders, generateJsonHeaders} from "../utils/httpServiceUtils";

const temp_base_url = 'http://localhost:8080'

export async function getApiInfo() {
    // return await axios.get('/api/info');
    return await axios.get(temp_base_url + '/api/info', {headers: generateJsonHeaders()});
}

export async function getGitInfo() {
    return await axios.get(temp_base_url + '/api/info/git', {headers: generateJsonHeaders()});
}

export async function getLogFiles() {
    return await axios.get(temp_base_url + '/api/info/logFiles', {headers: generateJsonHeaders()});
}

export async function getLogFile(filePath) {
    return await axios.post(temp_base_url + '/api/info/logFiles/file', filePath, {
        headers: generateFileHeaders()
    });
}