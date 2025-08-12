import {REQS_FETCH_FAILURE, REQS_FETCH_REQUEST, REQS_FETCH_SUCCESS} from '../actions/Actions';

export const initialState = {
    requirements: {
        apiKeys: [],
        mediaServers: []
    }
}

export default function reducer(state = initialState, action = {}) {
    switch (action.type) {
        case REQS_FETCH_REQUEST:
            return Object.assign({}, state, {
                requirements: {...state.requirements, loading: true}
            });
        case REQS_FETCH_SUCCESS:
            return Object.assign({}, state, {
                requirements: {...action.payload, loading: false}
            });
        case REQS_FETCH_FAILURE:
            return Object.assign({}, state, {
                requirements: {...state.requirements, loading: false, error: action.payload},
                error: true
            });
        default:
            return state;
    }
}