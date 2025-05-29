import {RESET_LIBRARY, SELECT_LIBRARY} from "../actions/Actions";

export const initialState = null;

export default function reducer(state = initialState, action = {}) {
    switch (action.type) {
        case SELECT_LIBRARY:
            return Object.assign({}, state, {
                ...action.library
            });
        case RESET_LIBRARY:
            return initialState;
        default:
            return state;
    }
}