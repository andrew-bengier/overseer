// Action Types:
export const UPDATE_THEME = 'UPDATE_THEME';

export const FETCH_REQS_REQUEST = 'FETCH_REQS_REQUEST';
export const FETCH_REQS_SUCCESS = 'FETCH_REQS_SUCCESS';
export const FETCH_REQS_FAILURE = 'FETCH_REQS_FAILURE';

export const SELECT_LIBRARY = 'SELECT_LIBRARY';
export const RESET_LIBRARY = 'RESET_LIBRARY';

// Actions:
export const updateTheme = (darkMode) => {
    return {type: UPDATE_THEME, darkMode};
}

export const fetchRequirements = () => {
    return {type: FETCH_REQS_REQUEST};
}

export const fetchRequirementsSuccess = (requirements) => {
    return {type: FETCH_REQS_SUCCESS, requirements};
}

export const fetchRequirementsFailure = (error) => {
    return {type: FETCH_REQS_FAILURE, error};
}

export const selectLibrary = (library) => {
    return {type: SELECT_LIBRARY, library};
}

export const resetLibrary = () => {
    return {type: RESET_LIBRARY};
}