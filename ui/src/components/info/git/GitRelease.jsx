import React from "react";
import {Card, CardContent, CardHeader, Divider, Grid} from "@mui/material";
import colorForGitReleaseTag from "../../../utils/colorUtils";
import PropTypes from "prop-types";
import {Release} from "../../../models/git/Release";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import GitReleaseHeader from "./GitReleaseHeader";
import {formatStringToMarkdown} from "../../../utils/stringUtils";
import GitCommit from "./GitCommit";

function GitRelease({gitData}) {
    const [data, setData] = React.useState(null);
    const [gitReleaseHeaderTitle, setGitReleaseHeaderTitle] = React.useState(null);
    const [gitReleaseHeaderTag, setGitReleaseHeaderTag] = React.useState(null);

    React.useEffect(() => {
        // console.log(gitData);

        setData(gitData);
        setGitReleaseHeaderTitle({
            title: gitData.title ? gitData.title : gitData.name,
            link: gitData.url
        });
        if ('draft' in gitData || 'prerelease' in gitData) {
            setGitReleaseHeaderTag({
                title: gitData.tag,
                link: gitData.url,
                color: colorForGitReleaseTag(('prerelease' in gitData) ? 'prerelease' : 'draft'),
                size: "small"
            });
        }

    }, [gitData]);

    return (
        <React.Fragment>
            {data ? (
                <Grid
                    container
                    spacing={2}
                >
                    <Grid size={2}>
                        <GitCommit gitData={data}/>
                    </Grid>
                    <Grid size={10}>
                        <Card
                            component="section"
                            elevation={0}
                            sx={{}}
                        >
                            <CardHeader
                                title={<GitReleaseHeader title={gitReleaseHeaderTitle} tag={gitReleaseHeaderTag}/>}
                                sx={{
                                    '& .MuiCardHeader-title': {
                                        fontSize: '1.2rem',
                                        fontWeight: 'medium',
                                        color: 'grey',
                                    }
                                }}
                            />
                            <Divider
                                sx={{
                                    paddingLeft: '10px'
                                }}
                            />
                            <CardContent
                                sx={{
                                    paddingTop: '10px',
                                    paddingLeft: '5%',
                                }}
                            >
                                {data.notes && (
                                    <ReactMarkdown
                                        children={formatStringToMarkdown(data.notes)}
                                        remarkPlugins={[remarkGfm]}
                                    />
                                )}
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            ) : null}
        </React.Fragment>
    );
}

GitRelease.propTypes = {
    gitData: PropTypes.shape(Release).isRequired
}

export default GitRelease;
