import React from 'react';
import './App.css';
import {Box, ThemeProvider} from "@mui/material";
import {BrowserRouter, Navigate, Outlet, Route, Routes} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import Header from "./components/layouts/header/Header";
import ThemeSwitcher from "./themes/ThemeSwitcher";
import Sidenav from "./components/layouts/sidenav/Sidenav";
import useScreenSize from "./hooks/useScreenSize";
import AppRoutes, {routePaths} from "./routes/AppRoutes";
import {scrubRoutePath} from "./utils/stringUtils";
import Error404 from "./routes/Error/Error404";

const navRoutes = AppRoutes;

function App() {
    // const screenSize = useScreenSize();
    // const {theme} = useTheme();
    // const screenSize = useScreenSize();
    const isMobile = useScreenSize(window.width);
    const [darkMode, setDarkMode] = React.useState(false);
    const [sidenavOpen, setSidenavOpen] = React.useState(!isMobile);
    // TODO: change default sidenav open based on screen size

    // React.useEffect(() => {
    // console.log("loaded app")
    // getApiInfo()
    //   .then(response => console.log(response.data))
    //   .catch(error => console.log(error));
    // }, []);

    React.useEffect(() => {
        setSidenavOpen(!isMobile);
    }, [isMobile]);

    function handleThemeToggle(toggle) {
        setDarkMode(toggle);
    }

    function handleSidenavToggle() {
        setSidenavOpen(!sidenavOpen);
    }

    return (
        <ThemeProvider theme={darkMode ? ThemeSwitcher(1) : ThemeSwitcher(0)}>
            <ToastContainer
                position="bottom-right"
                autoClose={2000}
                closeOnClick={true}
                pauseOnFocusLoss={false}
                theme={darkMode ? "dark" : "colored"}
            />
            <BrowserRouter>
                <Box
                    id="app-container"
                    sx={{
                        display: 'flex',
                        width: '100%',
                        height: '100%'
                    }}
                >
                    <Header theme={darkMode ? ThemeSwitcher(1) : ThemeSwitcher(0)} toggleTheme={handleThemeToggle}
                            toggleSidenav={handleSidenavToggle}/>
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
                        <Sidenav open={sidenavOpen} navRoutes={navRoutes} toggleSidenav={handleSidenavToggle}/>
                        <Box
                            sx={{
                                marginLeft: !isMobile ? '250px' : 2,
                                marginRight: !isMobile ? '10px' : 2,
                                paddingTop: '20px'
                            }}
                        >
                            <Routes>
                                <Route element={<Outlet/>}>
                                    <Route index element={<Navigate replace to="/Settings"/>}/>
                                    {/*<Route path="/Settings" element={<Settings/>}/>*/}
                                    {/*<Route path="/Test" element={<Test/>}/>*/}
                                    {navRoutes.filter(route => route.component != null).map((route) => (
                                        route.subRoutes ? (
                                                <Route key={scrubRoutePath(route.key) + '_parent'}>
                                                    <Route
                                                        key={scrubRoutePath(route.key)}
                                                        path={routePaths.separator + scrubRoutePath(route.name) + routePaths.all}
                                                        element={<route.component key={scrubRoutePath(route.key)}
                                                                                  route={route}/>}
                                                    />
                                                    {route.subRoutes.filter(subRoute => subRoute.component != null).map((subRoute) => (
                                                        <Route
                                                            key={scrubRoutePath(route.key) + routePaths.separator + scrubRoutePath(subRoute.key)}
                                                            path={routePaths.separator + scrubRoutePath(route.name) + routePaths.separator + scrubRoutePath(subRoute.name)}
                                                            element={<subRoute.component
                                                                key={scrubRoutePath(subRoute.key)}
                                                                route={subRoute}/>}
                                                        />
                                                    ))}
                                                </Route>
                                            )
                                            : (
                                                <Route
                                                    key={scrubRoutePath(route.key)}
                                                    path={scrubRoutePath(route.name) + routePaths.all}
                                                    element={<route.component key={scrubRoutePath(route.key)}
                                                                              route={route}/>}
                                                />
                                            )
                                    ))}
                                    <Route path="*" element={<Error404/>}/>
                                </Route>
                            </Routes>
                        </Box>
                    </Box>
                </Box>
            </BrowserRouter>
        </ThemeProvider>
    );
}

export default App;
