// Action Types:
export const UPDATE_THEME = 'UPDATE_THEME';

export const SELECT_SERVER = 'SELECT_SERVER';
export const RESET_SERVER = 'RESET_SERVER';

export const SELECT_LIBRARY = 'SELECT_LIBRARY';
export const RESET_LIBRARY = 'RESET_LIBRARY';

// Actions:
export const updateTheme = (darkMode) => {
    return {type: UPDATE_THEME, darkMode};
}

export const selectServer = (server) => {
    // [TEST]
    // console.log(server);
    return {type: SELECT_SERVER, server};
}

export const resetServer = () => {
    return {type: RESET_SERVER};
}

export const selectLibrary = (library) => {
    // [TEST]
    // console.log(library);
    return {type: SELECT_LIBRARY, library};
}

export const resetLibrary = () => {
    return {type: RESET_LIBRARY};
}