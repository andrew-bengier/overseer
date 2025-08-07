import {createTheme} from '@mui/material/styles';
import {lightBlue, red} from '@mui/material/colors';

// Custom dark theme
const DarkTheme = createTheme({
    palette: {
        mode: 'dark',
        primary: {
            main: '#67C5AC',
            light: '#CBEBE3'
        },
        secondary: {
            main: '#6780C5',
            light: '#98b1eb'
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
        infoCard: {
            main: '#d1d1d1',
            text: "black"
        },
        background: "#2d2d2d",
    },
    components: {
        MuiListItemIcon: {
            styleOverrides: {
                root: {
                    minWidth: 24,
                }
            }
        },
        MuiPaper: {
            styleOverrides: {
                root: {
                    backgroundColor: '#2d2d2d',
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