import {createTheme} from '@mui/material/styles';
import {red} from '@mui/material/colors';

// Custom light theme
const LightTheme = createTheme({
    palette: {
        primary: {
            main: '#67C5AC'
        },
        secondary: {
            main: '#6780C5'
        },
        error: {
            main: red.A400
        },
        background: "white"
    },
    components: {
        MuiPaper: {
            styleOverrides: {
                root: {
                    backgroundColor: 'white',
                },
            },
        }
    }
});

export default LightTheme;
