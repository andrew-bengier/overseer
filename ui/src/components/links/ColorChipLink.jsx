import React from "react";
import {Chip} from "@mui/material";
import PropTypes from "prop-types";

function ColorChipLink({title, link, color, size}) {
    return (
        <Chip
            label={title}
            component="a"
            href={link}
            variant="outlined"
            color={color}
            size={size}
            clickable
        />
    );
}

ColorChipLink.propTypes = {
    title: PropTypes.string.isRequired,
    link: PropTypes.string.isRequired,
    color: PropTypes.string.isRequired,
    size: PropTypes.string.isRequired
}

export default ColorChipLink;
