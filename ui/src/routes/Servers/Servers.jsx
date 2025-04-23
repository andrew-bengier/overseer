import React from "react";
import {getServers} from "../../services/ServerService";
import {Box, Grid} from "@mui/material";
import ServerInfoCard from "../../components/info/servers/ServerInfoCard";
import Libraries from "../Libraries/Libraries";

function Servers() {
    const [servers, setServers] = React.useState([]);
    const [selectedServer, setSelectedServer] = React.useState(null);

    React.useEffect(() => {
        const fetchServers = async () => {
            const response = await getServers();
            if (response.status === 200) {
                console.log(response);
                setServers(response.data);
            }
        }

        fetchServers().then();
    }, []);

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
                    <Grid size={8} onClick={() => {
                        setSelectedServer(server);
                    }}>
                        <ServerInfoCard server={server}/>
                    </Grid>
                ))}
            </Grid>
            {selectedServer && (
                <Libraries server={selectedServer}/>
            )}
        </Box>
    )
}

Servers.propTypes = {}

export default Servers;
