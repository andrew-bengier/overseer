import React from "react";
import {getServers} from "../../services/ServerService";
import {Box, Grid} from "@mui/material";
import ServerInfoCard from "../../components/info/servers/ServerInfoCard";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import {scrubRoutePath} from "../../utils/stringUtils";

function Servers() {
    const navigate = useNavigate();
    const [servers, setServers] = React.useState([]);
    const [selectedServer, setSelectedServer] = React.useState(null);

    React.useEffect(() => {
        const fetchServers = async () => {
            const response = await getServers();
            if (response.status === 200) {
                // console.log(response);
                setServers(response.data);
                toast.success('Retrieved servers');
            } else {
                toast.error(response.message);
            }
        }

        fetchServers().then();
        // .then(response => {
        //     if (response.ok) {
        //         setServers(response.data);
        //         toast.success('Retrieved servers');
        //     } else {
        //         toast.warn(response.message);
        //     }
        // })
        // .catch(error => {
        //     toast.error('Error fetching servers: ' + error.message);
        // });

    }, []);

    const handleServerSelect = (server) => {
        // setSelectedServer(server);
        navigate('/Servers/' + scrubRoutePath(server.id));
    }

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                // paddingTop: '200px'
            }}
        >
            Servers
            <Grid
                container
                spacing={2}
                sx={{
                    paddingTop: '20px'
                }}
            >
                {servers.map((server) => (
                    <Grid size={8} onClick={() => handleServerSelect(server)}>
                        <ServerInfoCard server={server}/>
                    </Grid>
                ))}
            </Grid>
            {/*{selectedServer && (*/}
            {/*    <Libraries server={selectedServer}/>*/}
            {/*)}*/}
        </Box>
    )
}

Servers.propTypes = {}

export default Servers;
