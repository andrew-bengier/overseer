import React from 'react';
import './App.css';
import {Box, ThemeProvider} from "@mui/material";
import {Navigate, Outlet, Route, Routes, useNavigate} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import Header from "./components/layouts/header/Header";
import Sidenav from "./components/layouts/sidenav/Sidenav";
import useScreenSize from "./hooks/useScreenSize";
import AppRoutes, {routePaths} from "./routes/AppRoutes";
import {scrubRoutePath} from "./utils/stringUtils";
import Error404 from "./routes/Error/Error404";
import {useDispatch, useSelector} from "react-redux";
import {fetchRequirements} from "./redux/actions/Actions";
import {checkAppRequirements} from "./services/ValidationService";

const navRoutes = AppRoutes;

function App() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    // const screenSize = useScreenSize();
    // const {theme} = useTheme();
    // const screenSize = useScreenSize();
    const isMobile = useScreenSize(window.width);
    const theme = useSelector((state) => state.theme);
    const appRequirements = useSelector((state) => state.requirements);

    // const currentServer = useSelector((state) => state.server);
    const [sidenavOpen, setSidenavOpen] = React.useState(!isMobile);
    // TODO: change default sidenav open based on screen size

    React.useEffect(() => {
        dispatch(fetchRequirements());
    }, []);

    React.useEffect(() => {
        console.log(appRequirements);
        if (appRequirements.requirements.loading === false && !appRequirements.requirements.error) {
            checkAppRequirements(appRequirements.requirements, navigate);
        }
        // getApiInfo()
        //   .then(response => console.log(response.data))
        //   .catch(error => console.log(error));
    }, [appRequirements]);

    // [TEST]
    // React.useEffect(() => {
    //     console.log('Check init');
    //     checkAppRequirements().then(
    //         response => {
    //             console.log(response);
    //             // if (!response) {
    //             //     navigate('/Error/Error404');
    //             // }
    //         }
    //     );
    // }, [navRoutes]);


    React.useEffect(() => {
        setSidenavOpen(!isMobile);
    }, [isMobile]);

    // React.useEffect(() => {
    //     console.log(currentServer);
    // }, [currentServer]);

    function handleSidenavToggle() {
        setSidenavOpen(!sidenavOpen);
    }

    return (
        <ThemeProvider theme={theme.theme}>
            <ToastContainer
                position="bottom-right"
                autoClose={2000}
                closeOnClick={true}
                pauseOnFocusLoss={false}
                theme={theme.theme.darkMode ? "dark" : "colored"}
            />
            <Box
                id="app-container"
                sx={{
                    display: 'flex',
                    width: '100%',
                    height: '100%'
                }}
            >
                <Header toggleSidenav={handleSidenavToggle}/>
                <Box
                    id="main-container"
                    component="main"
                    sx={{
                        flexGrow: 1,
                        // width: 0,
                        backgroundColor: 'background.default',
                        paddingTop: '64px'
                    }}
                >
                    <Sidenav open={sidenavOpen} navRoutes={navRoutes.filter(route => route.displayNav)}
                             toggleSidenav={handleSidenavToggle}/>
                    <Box
                        sx={{
                            marginLeft: !isMobile ? '250px' : 2,
                            marginRight: !isMobile ? '10px' : 2,
                            paddingTop: '20px'
                        }}
                    >
                        <Routes>
                            <Route element={<Outlet/>}>
                                {navRoutes.map((route) => (
                                    route.subRoutes ? (
                                        <Route key={scrubRoutePath(route.key) + '_parent'}>
                                            <Route
                                                key={scrubRoutePath(route.key)}
                                                path={routePaths.separator + scrubRoutePath(route.name) + routePaths.all}
                                                element={route.component != null ?
                                                    <route.component key={scrubRoutePath(route.key)}
                                                                     route={route}/> : null}
                                            />
                                            {route.subRoutes.filter(subRoute => subRoute.component != null).map((subRoute) => (
                                                subRoute.nested ? (
                                                    <Route
                                                        key={scrubRoutePath(route.key) + routePaths.nested}
                                                        path={routePaths.separator + scrubRoutePath(route.name) + routePaths.nested}
                                                        element={<subRoute.component
                                                            key={scrubRoutePath(subRoute.key)}
                                                            route={subRoute}/>}
                                                    />
                                                ) : (
                                                    <Route
                                                        key={scrubRoutePath(route.key) + routePaths.separator + scrubRoutePath(subRoute.key)}
                                                        path={routePaths.separator + scrubRoutePath(route.name) + routePaths.separator + scrubRoutePath(subRoute.name)}
                                                        element={<subRoute.component
                                                            key={scrubRoutePath(subRoute.key)}
                                                            route={subRoute}/>}
                                                    />
                                                )
                                            ))}
                                        </Route>
                                    ) : (
                                        route.component != null && (
                                            <Route
                                                key={scrubRoutePath(route.key)}
                                                path={scrubRoutePath(route.name) + routePaths.all}
                                                element={<route.component key={scrubRoutePath(route.key)}
                                                                          route={route}/>}
                                            />
                                        )
                                    )
                                ))}
                                <Route path="indx.html" element={<Navigate replace to="/Dashboard"/>}/>
                                <Route path="*" element={<Error404/>}/>
                            </Route>
                        </Routes>
                    </Box>
                </Box>
            </Box>
        </ThemeProvider>
    );
}

export default App;
