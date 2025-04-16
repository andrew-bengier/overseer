export function generateFileHeaders() {
    return {
        'Accept': '*',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
        'Content-Type': 'application/json',
        'Response-Type': 'blob',
        'X-User-ID': 'overseer-ui'
    };
}

export function generateJsonHeaders() {
    return {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
        'Content-Type': 'application/json',
        'X-User-ID': 'overseer-ui'
    };
}

export function generateTextHeaders() {
    return {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
        'Content-Type': 'text/plain',
        'X-User-ID': 'overseer-ui'
    };
}