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
import StorageOutlinedIcon from '@mui/icons-material/StorageOutlined';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import LibraryBooksIcon from '@mui/icons-material/LibraryBooks';
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
import Servers from "./Servers/Servers";
import Collections from "./Collections/Collections";
import Server from "./Servers/Server";
import Collection from "./Collections/Collection";
import Libraries from "./Libraries/Libraries";
import Library from "./Libraries/Library";

export const navRoute = PropTypes.shape({
    name: PropTypes.string.isRequired,
    key: PropTypes.string.isRequired,
    description: PropTypes.string,
    component: PropTypes.element.isRequired,
    icon: PropTypes.element | null,
    displayNav: PropTypes.bool.isRequired,
    standardNav: PropTypes.bool.isRequired,
    nested: PropTypes.bool,
    subRoutes: PropTypes.array
});

export const routePaths = {
    all: '/*',
    separator: '/',
    nested: '/:id',
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
        displayNav: true,
        standardNav: true,
        subRoutes: []
    },
    {
        name: 'Servers',
        key: 'Servers',
        description: 'Servers',
        component: Servers,
        icon: StorageOutlinedIcon,
        displayNav: true,
        standardNav: true,
        subRoutes: [
            {
                name: 'Server',
                key: 'Server',
                description: 'Server',
                component: Server,
                icon: StorageOutlinedIcon,
                displayNav: false,
                standardNav: true,
                nested: true,
                subRoutes: []
            }
        ]
    },
    {
        name: 'Libraries',
        key: 'Libraries',
        description: 'Libraries',
        component: Libraries,
        icon: LibraryBooksIcon,
        displayNav: false,
        standardNav: true,
        subRoutes: [
            {
                name: 'Library',
                key: 'Library',
                description: 'Library',
                component: Library,
                icon: LibraryBooksIcon,
                displayNav: false,
                standardNav: true,
                nested: true,
                subRoutes: []
            }
        ]
    },
    {
        name: 'Collections',
        key: 'Collections',
        description: 'Collections',
        component: Collections,
        icon: ContentCopyIcon,
        displayNav: false,
        standardNav: true,
        subRoutes: [
            {
                name: 'Collection',
                key: 'Collection',
                description: 'Collection',
                component: Collection,
                icon: ContentCopyIcon,
                displayNav: false,
                standardNav: true,
                nested: true,
                subRoutes: []
            }
        ]
    },
    {
        name: 'Settings',
        key: 'Settings',
        description: 'Application settings',
        component: Settings,
        icon: SettingsIcon,
        displayNav: true,
        standardNav: false,
        subRoutes: [
            {
                name: 'General',
                key: 'General',
                description: 'Analytics and updates',
                component: General,
                icon: SettingsOutlinedIcon,
                displayNav: true,
                standardNav: true,
                subRoutes: []
            },
            {
                name: 'Apis',
                key: 'Apis',
                description: 'Api Connectivity',
                component: Apis,
                icon: ApiOutlinedIcon,
                displayNav: true,
                standardNav: true,
                subRoutes: []
            },
            {
                name: 'UI',
                key: 'UI',
                description: 'Calendar, dates and accessibility',
                component: UI,
                icon: DisplaySettingsOutlinedIcon,
                displayNav: true,
                standardNav: true,
                subRoutes: []
            }
        ]
    },
    {
        name: 'System',
        key: 'System',
        description: 'Application Information',
        component: null,
        icon: ComputerIcon,
        displayNav: true,
        standardNav: false,
        subRoutes: [
            {
                name: 'Status',
                key: 'Status',
                description: 'Status',
                component: Status,
                icon: BarChartIcon,
                displayNav: true,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Tasks',
                key: 'Tasks',
                description: 'Scheduled tasks',
                component: Tasks,
                icon: ScheduleIcon,
                displayNav: true,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Events',
                key: 'Events',
                description: 'Events',
                component: Events,
                icon: FormatListBulletedIcon,
                displayNav: true,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Updates',
                key: 'Updates',
                description: 'Application updates',
                component: Updates,
                icon: FeedIcon,
                displayNav: true,
                standardNav: false,
                subRoutes: []
            },
            {
                name: 'Logs',
                key: 'Logs',
                description: 'Log files',
                component: Logs,
                icon: DvrIcon,
                displayNav: true,
                standardNav: false,
                subRoutes: []
            }
        ]
    }
]

export default AppRoutes;
