import {combineReducers} from 'redux';
import theme from './theme';
import requirements from './requirements';

const rootReducer = combineReducers({
    theme,
    requirements
});

export default rootReducer;
