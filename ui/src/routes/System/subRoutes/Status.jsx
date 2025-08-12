import React from "react";
import {getApiInfo} from "../../../services/InfoService";
import BlockSection from "../../../components/layouts/sections/BlockSection";
import GitInfo from "../../../components/info/git/GitInfo";

function Status() {
    const [buildInfo, setBuildInfo] = React.useState();
    const [gitInfo, setGitInfo] = React.useState();

    React.useEffect(() => {
        const fetchInfo = async () => {
            await getApiInfo().then(
                response => {
                    console.log(response);
                    setBuildInfo({
                        ...response.data.buildInfo,
                        environment: response.data.environment,
                        mode: response.data.mode,
                        startTime: response.data.startTime,
                        sourceLink: response.data.sourceLink,
                    })
                    setGitInfo({
                        ...response.data.gitInfo
                    })
                }
            );
            // TODO: add disk space info here
        };

        // fetchInfo().then();
    }, []);

    // TODO: add wiki here
    return (
        <React.Fragment>
            {buildInfo && (
                <BlockSection
                    header={"About"}
                    width="90%"
                    content={<GitInfo gitData={buildInfo}/>}
                />
            )}
            {gitInfo && (
                <BlockSection
                    header={"Git Information"}
                    width="90%"
                    content={<GitInfo gitData={gitInfo}/>}
                />
            )}
        </React.Fragment>
    )
}

Status.propTypes = {}

export default Status;
