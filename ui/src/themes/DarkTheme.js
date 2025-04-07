import {createTheme} from '@mui/material/styles';
import {red} from '@mui/material/colors';

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
        error: {
            main: red.A400
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