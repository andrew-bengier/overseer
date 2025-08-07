import {FETCH_REQS_FAILURE, FETCH_REQS_REQUEST, FETCH_REQS_SUCCESS} from '../actions/Actions';

export const initialState = {
    apiKeys: [],
    mediaServers: [],
}

export default function reducer(state = initialState, action = {}) {
    switch (action.type) {
        case FETCH_REQS_REQUEST:
            
        case FETCH_REQS_SUCCESS:
        case FETCH_REQS_FAILURE:
        default:
            return state;
    }
}