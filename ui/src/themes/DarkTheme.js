import {createTheme} from '@mui/material/styles';
import {lightBlue, red} from '@mui/material/colors';

// Custom dark theme
const DarkTheme = createTheme({
    palette: {
        mode: 'dark',
        primary: {
            main: '#67C5AC'
        },
        secondary: {
            main: '#6780C5'
        },
        info: {
            main: lightBlue.A200,
            light: "#d9f3ff"
        },
        error: {
            main: red.A400
        },
        text: {
            // grey: '#808080'
            grey: '#91a1ab'
        },
        background: "black",
    },
    components: {
        MuiPaper: {
            styleOverrides: {
                root: {
                    backgroundColor: 'black',
                },
            },
        },
        MuiToolbar: {
            styleOverrides: {
                root: {
                    backgroundColor: '#67C5AC',
                },
            },
        }
    }
});

export default DarkTheme;