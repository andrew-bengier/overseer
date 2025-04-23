import PropTypes from "prop-types";

export const Server = {
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    // TODO: move apikey to another shape
    apiKey: PropTypes.object,
    // id: PropTypes.string.isRequired,
    // name: PropTypes.string.isRequired,
    // key: PropTypes.string,
    // url: PropTypes.string,
    // TODO: move settings (individual) to another shape
    settings: PropTypes.array,
    // id: PropTypes.string.isRequired,
    // type: PropTypes.string.isRequired,
    // name: PropTypes.string.isRequired,
    // val: PropTypes.string.isRequired,
    // TODO: move actions (individual) to another shape
    actions: PropTypes.array,
    libraries: PropTypes.array
};