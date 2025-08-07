import {combineReducers} from 'redux';
import theme from './theme';
import library from './library';

const rootReducer = combineReducers({
    theme,
    library
});

export default rootReducer;
