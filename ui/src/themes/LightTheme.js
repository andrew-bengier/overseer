import {createTheme} from '@mui/material/styles';
import {lightBlue, red} from '@mui/material/colors';

// Custom light theme
const LightTheme = createTheme({
    palette: {
        primary: {
            main: '#67C5AC',
            light: '#CBEBE3'
        },
        secondary: {
            main: '#6780C5',
            light: '#98b1eb'
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
        infoCard: {
            main: '#d1d1d1',
            text: "black"
        },
        background: "white"
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
                    backgroundColor: 'white',
                },
            },
        }
    }
});

export default LightTheme;
