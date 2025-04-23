import React from "react";
import {getServers} from "../../services/ServerService";
import {Grid} from "@mui/material";
import LibraryInfoCard from "../../components/info/libraries/LibraryInfoCard";

function Libraries({server}) {
    const [libraries, setLibraries] = React.useState([]);

    React.useEffect(() => {
        const fetchLibraries = async () => {
            const response = await getServers();
            if (response.status === 200) {
                console.log(response);
                setLibraries(response.data);
            }
        }

        if (!server.libraries || server.libraries.length === 0) {
            // fetchLibraries().then();
            console.log('NO libraries');
        } else {
            setLibraries(server.libraries);
        }
    }, [server]);

    return (
        // <Box
        //     sx={{
        //         display: 'flex',
        //         flexDirection: 'column',
        //     }}
        // >
        <Grid
            container
            spacing={2}
            sx={{
                paddingTop: '20px'
            }}
        >
            {libraries.map((library) => (
                <Grid size={8}>
                    <LibraryInfoCard library={library}/>
                </Grid>
            ))}
        </Grid>
        // </Box>
    )
}

Libraries.propTypes = {}

export default Libraries;
