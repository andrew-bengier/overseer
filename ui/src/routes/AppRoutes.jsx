import PropTypes from "prop-types";
import SettingsIcon from '@mui/icons-material/Settings';
import BugReportIcon from '@mui/icons-material/BugReport';
import Settings from "./Settings/Settings";
import Test from "./Test/Test";
import General from "./Settings/subRoutes/General";
import UI from "./Settings/subRoutes/UI";

export const navRoute = PropTypes.shape({
    name: PropTypes.string.isRequired,
    key: PropTypes.string.isRequired,
    description: PropTypes.string,
    component: PropTypes.element.isRequired,
    icon: PropTypes.element | null,
    standardNav: PropTypes.bool.isRequired,
    subRoutes: PropTypes.array
});

export const routePaths = {
    all: '/*',
    separator: '/',
    Settings: '/Settings'
};

const AppRoutes = [
    {
        name: 'Test',
        key: 'Test',
        description: 'Test',
        component: Test,
        icon: BugReportIcon,
        standardNav: true,
        subRoutes: []
    },
    {
        name: 'Settings',
        key: 'Settings',
        description: 'Application settings',
        component: Settings,
        icon: SettingsIcon,
        standardNav: false,
        subRoutes: [
            {
                name: 'General',
                key: 'General',
                description: 'Analytics and updates',
                component: General,
                icon: null,
                standardNav: true,
                subRoutes: []
            },
            {
                name: 'UI',
                key: 'UI',
                description: 'Calendar, dates and accessibility',
                component: UI,
                icon: null,
                standardNav: true,
                subRoutes: []
            }
            //     Media Management
            //     Profiles
            //     Quality
            //     Custom Formats
            //     Indexers
            //     Download Clients
            //     Import Lists
            //     Connect
            //     Metadata
            //     Metadata Sources
            //     Tags
            //     General
            //          Host
            //          Security
            //          Logging
            //          Updates
            //          Backups
            //     UI
            //          Calendar
            //          Dates
            //          Style
            //          Language (for ui)
        ]
    }
]

export default AppRoutes;
