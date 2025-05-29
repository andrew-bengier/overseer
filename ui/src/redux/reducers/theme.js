import {UPDATE_THEME} from '../actions/Actions';
import ThemeSwitcher from '../../themes/ThemeSwitcher.jsx';

export const initialState = {
    darkMode: false,
    theme: ThemeSwitcher(false)
};

export default function reducer(state = initialState, action = {}) {
    switch (action.type) {
        case UPDATE_THEME: {
            const theme = ThemeSwitcher(action.darkMode);
            return Object.assign({}, state, {
                darkMode: theme.palette.mode === 'dark',
                theme: theme
            });
        }
        default:
            return state;
    }
}
