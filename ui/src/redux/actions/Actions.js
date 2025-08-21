// Action Types:
export const UPDATE_THEME = 'UPDATE_THEME';

export const UPDATE_PLEX_AUTH = 'UPDATE_PLEX_AUTH';

export const REQS_FETCH_REQUEST = 'REQS_FETCH_REQUEST';
export const REQS_FETCH_SUCCESS = 'REQS_FETCH_SUCCESS';
export const REQS_FETCH_FAILURE = 'REQS_FETCH_FAILURE';

// Actions:
export const updateTheme = (darkMode) => {
    return {type: UPDATE_THEME, darkMode};
}

export const updatePlexAuth = (auth) => {
    return {type: UPDATE_PLEX_AUTH, auth};
}

export const fetchRequirements = () => {
    // [TEST]
    console.log("fetching requirements");
    return {type: REQS_FETCH_REQUEST};
}

export const fetchRequirementsSuccess = (requirements) => {
    return {type: REQS_FETCH_SUCCESS, payload: requirements};
}

export const fetchRequirementsFailure = (error) => {
    return {type: REQS_FETCH_FAILURE, payload: error};
}
