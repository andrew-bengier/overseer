import {combineReducers} from 'redux';
import theme from './theme';
import plexAuth from './plexAuth';
import requirements from './requirements';

const rootReducer = combineReducers({
    theme,
    plexAuth,
    requirements
});

export default rootReducer;
