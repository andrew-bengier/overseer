import PropTypes from "prop-types";

export const ApiKey = {
    id: PropTypes.string,
    type: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    pin: PropTypes.string,
    key: PropTypes.string,
    url: PropTypes.string
};