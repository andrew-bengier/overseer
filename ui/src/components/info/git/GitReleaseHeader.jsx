import React from "react";
import PropTypes from "prop-types";
import {Chip, Typography} from "@mui/material";

function GitReleaseHeader({title, tag}) {
    return (
        <React.Fragment>
            <Typography
                variant="h3"
                component="a"
                href={title.link}
            >
                {title.title}
            </Typography>
            {/*{tag != null && <ColorChipLink title={tag.title} link={tag.link} color={color} size={tag.size}/>}*/}
            {tag && (
                <Chip
                    label={tag.title}
                    component="a"
                    href={tag.link}
                    variant="outlined"
                    color={tag.color}
                    size={tag.size}
                    clickable
                />
            )}
        </React.Fragment>
    );
}

GitReleaseHeader.propTypes = {
    title: PropTypes.shape({
        title: PropTypes.string.isRequired,
        link: PropTypes.string.isRequired,
    }).isRequired,
    tag: PropTypes.shape({
        title: PropTypes.string.isRequired,
        link: PropTypes.string.isRequired,
        color: PropTypes.string.isRequired,
        size: PropTypes.string.isRequired
    })
}

export default GitReleaseHeader;
