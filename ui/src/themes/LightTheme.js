import {createTheme} from '@mui/material/styles';
import {lightBlue, red} from '@mui/material/colors';

// Custom light theme
const LightTheme = createTheme({
    palette: {
        primary: {
            main: '#67C5AC'
        },
        secondary: {
            main: '#6780C5'
        },
        info: {
            main: lightBlue.A100,
            light: "#d9f3ff"
        },
        error: {
            main: red.A400
        },
        text: {
            grey: '#91a1ab'
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
