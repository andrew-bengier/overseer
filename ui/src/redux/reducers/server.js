import {RESET_SERVER, SELECT_SERVER} from "../actions/Actions";

export const initialState = null;

export default function reducer(state = initialState, action = {}) {
    switch (action.type) {
        case SELECT_SERVER:
            return Object.assign({}, state, {
                ...action.server
            });
        case RESET_SERVER:
            return initialState;
        default:
            return state;
    }
}