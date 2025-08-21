import {UPDATE_PLEX_AUTH} from '../actions/Actions';

export default function reducer(state = null, action = {}) {
    switch (action.type) {
        case UPDATE_PLEX_AUTH: {
            console.log('Assigning auth:', action.auth);
            return Object.assign({}, state, {plexAuth: action.auth});
        }
        default:
            return state;
    }
}
