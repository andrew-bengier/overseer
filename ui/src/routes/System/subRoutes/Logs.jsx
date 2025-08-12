import React from "react";
import InfoBanner from "../../../components/info/banners/InfoBanner";
import {Button, Container, Grid, Typography} from "@mui/material";
import {formatDate} from "../../../utils/dateUtils";
import FileDownloadTable from "../../../components/info/tables/FileDownloadTable";
import LogFileHighlighter from "../../../components/info/code/LogFileHighlighter";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import {getLogFile, getLogFiles} from "../../../services/InfoService";
import {isFileViewable} from "../../../utils/stringUtils";

// TODO: messages and translate for info banner

const columns = [
    {
        id: 'fileName',
        label: 'Filename',
        minWidth: 300,
        align: 'left'
    },
    {
        id: 'lastUpdated',
        label: 'Last Updated',
        minWidth: 150,
        align: 'right',
        // format: (value) => value.toLocaleString('en-US'),
        format: (value) => formatDate("yyyy/MM/dd HH:mm:sss", new Date(value), true),
    },
    {
        id: 'view',
        label: '',
        minWidth: 100,
        align: 'right',
        function: true,
        functionType: 'selection',
        format: (value, func) => (
            isFileViewable(value.fileName) ? (
                <Button onClick={() => {
                    func(value)
                }} variant="contained" color="primary">
                    View
                </Button>
            ) : null
        )
        //     TODO: intl here
    },
    {
        id: 'download',
        label: '',
        minWidth: 100,
        align: 'right',
        function: true,
        functionType: 'download',
        format: (value, func) => (
            <Button onClick={() => {
                func(value)
            }} variant="contained" color="primary">
                Download
            </Button>
        )
        //     TODO: intl here
    }
];

const data = [
    {
        id: '01963a1d-94fa-789d-a899-73bca219013c',
        fileName: 'File_01.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7775-bd4e-be4b7eecb3f9',
        fileName: 'File_02.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-73a8-8321-207a64251d16',
        fileName: 'File_03.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7b2f-9db1-82f462408aa4',
        fileName: 'File_04.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7a6c-8c13-cf5090b0bbab',
        fileName: 'File_05.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7df6-848c-5fcebc6e7203',
        fileName: 'File_06.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-73d6-acd5-59ba543c7ee7',
        fileName: 'File_07.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7512-9d41-bbce225e7e6f',
        fileName: 'File_08.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-751f-9e70-141dca41af7b',
        fileName: 'File_09.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7829-ba13-58cf6681964a',
        fileName: 'File_10.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7f5d-8af8-9648d2b20559',
        fileName: 'File_11.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-75da-a816-96c5a8c8d0a8',
        fileName: 'File_12.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7321-8852-ab0c53cae9d1',
        fileName: 'File_13.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7040-a397-83da00036fe8',
        fileName: 'File_14.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7f85-a0ff-8f83705f2c10',
        fileName: 'File_15.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7042-87a7-9ebd7eedf048',
        fileName: 'File_16.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7f8f-831d-2babe210d847',
        fileName: 'File_17.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7bf0-99ae-190e84d4cb9e',
        fileName: 'File_18.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7385-ac93-97c5655e40c3',
        fileName: 'File_19.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-79f5-b8d5-fd8875a63fa8',
        fileName: 'File_20.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7ddf-a8fd-ace56c9d494b',
        fileName: 'File_21.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7570-839c-db88cbf5bd8c',
        fileName: 'File_22.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7192-8a21-7d5cca8d4530',
        fileName: 'File_23.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-7770-92de-23b9475d0351',
        fileName: 'File_24.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    },
    {
        id: '01963a1d-94fa-777c-8a64-b2a2acbf010e',
        fileName: 'File_25.json',
        updated: '2025-04-14T20:41:18Z',
        download: 'https://www.google.com'
    }
];

export default function Logs() {
    const [logFile, setLogFile] = React.useState(null);
    const [files, setFiles] = React.useState([]);

    React.useEffect(() => {
        const fetchLogFiles = async () => {
            const response = await getLogFiles();
            if (response.status === 200) {
                console.log(response);
                setFiles(response.data);
            }
        };

        // fetchLogFiles().then();
    }, []);

    const handleLogFileSelection = (log) => {
        // console.log('selected:', log);
        const fetchLogFile = async () => {
            const response = await getLogFile(log.path);
            // console.log(response);
            if (response.status === 200) {
                setLogFile({
                    name: log.fileName,
                    data: response.data
                });
            }
        }

        fetchLogFile().then();
    };

    const handleLogFileDownload = (log) => {
        // console.log('download:', log);

        const fetchLogFile = async () => {
            const response = await getLogFile(log.path);
            // console.log(response);
            if (response.status === 200) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement("a");
                link.href = url;
                link.setAttribute("download", log.fileName);
                link.click();
                setTimeout(() => window.URL.revokeObjectURL(url), 0);
            }
        };

        fetchLogFile().then();
    };

    function bannerInfo() {
        return (
            <Grid
                container
                direction="column"
                sx={{
                    paddingTop: '10px',
                    paddingLeft: '20px',
                    justifyContent: 'center',
                    alignItems: 'stretch'
                }}
            >
                <Typography variant="h6" color="text.grey">Log files are located here: </Typography>
                <Typography variant="h6" color="text.grey">
                    The log level defaults to 'Info' for all and can be changed here:
                    <Typography
                        variant="h6"
                        color="secondary.main"
                        component="a"
                        href="/Settings/General"
                        sx={{
                            paddingLeft: '5px',
                            textDecoration: 'none'
                        }}
                    >
                        General Settings
                    </Typography>
                </Typography>
            </Grid>
        );
    }

    return (
        <Container
            sx={{
                paddingTop: '20px',
                width: '100%',
                height: '100px'
            }}
        >
            <InfoBanner
                children={bannerInfo()}
            />
            {files.length > 0 && (
                <FileDownloadTable
                    columns={columns}
                    data={files}
                    tableLabel={"log-files"}
                    handleSelection={handleLogFileSelection}
                    handleDownload={handleLogFileDownload}
                />
            )}
            {logFile !== null && (
                <BlockSection
                    header={logFile.name}
                    width="90%"
                    content={<LogFileHighlighter fileData={logFile.data}/>}
                />
            )}
        </Container>
    );
}
