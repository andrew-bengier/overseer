import {combineReducers} from 'redux';
import theme from './theme';
import server from './server';
import library from './library';

const rootReducer = combineReducers({
    theme,
    server,
    library
});

export default rootReducer;
