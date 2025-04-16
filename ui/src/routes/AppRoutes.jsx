import PropTypes from "prop-types";
import SettingsIcon from '@mui/icons-material/Settings';
import BugReportIcon from '@mui/icons-material/BugReport';
import ComputerIcon from '@mui/icons-material/Computer';
import FormatListBulletedIcon from '@mui/icons-material/FormatListBulleted';
import BarChartIcon from '@mui/icons-material/BarChart';
import FeedIcon from '@mui/icons-material/Feed';
import ScheduleIcon from '@mui/icons-material/Schedule';
import DvrIcon from '@mui/icons-material/Dvr';
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined';
import ApiOutlinedIcon from '@mui/icons-material/ApiOutlined';
import DisplaySettingsOutlinedIcon from '@mui/icons-material/DisplaySettingsOutlined';
import Settings from "./Settings/Settings";
import Test from "./Test/Test";
import General from "./Settings/subRoutes/General";
import UI from "./Settings/subRoutes/UI";
import Updates from "./System/subRoutes/Updates";
import Status from "./System/subRoutes/Status";
import Tasks from "./System/subRoutes/Tasks";
import Logs from "./System/subRoutes/Logs";
import Events from "./System/subRoutes/Events";
import Apis from "./Settings/subRoutes/Apis";

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

// TODO: add option for overriding parent component with redirect to default child

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
                icon: SettingsOutlinedIcon,
                standardNav: true,
                subRoutes: []
            },
            {
                name: 'Apis',
                key: 'Apis',
                description: 'Api Connectivity',
                component: Apis,
                icon: ApiOutlinedIcon,
                standardNav: true,
                subRoutes: []
            },
            {
                name: 'UI',
                key: 'UI',
                description: 'Calendar, dates and accessibility',
                component: UI,
                icon: DisplaySettingsOutlinedIcon,
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
    },
    {
        name: 'System',
        key: 'System',
        description: 'Application Information',
        component: null,
        icon: ComputerIcon,
        standardNav: false,
        subRoutes: [
            {
                name: 'Status',
                key: 'Status',
                description: 'Status',
                component: Status,
                icon: BarChartIcon,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Tasks',
                key: 'Tasks',
                description: 'Scheduled tasks',
                component: Tasks,
                icon: ScheduleIcon,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Events',
                key: 'Events',
                description: 'Events',
                component: Events,
                icon: FormatListBulletedIcon,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Updates',
                key: 'Updates',
                description: 'Application updates',
                component: Updates,
                icon: FeedIcon,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Logs',
                key: 'Logs',
                description: 'Log files',
                component: Logs,
                icon: DvrIcon,
                standardNav: false,
                subRoutes: []
            }
        ]
    }
]

export default AppRoutes;
