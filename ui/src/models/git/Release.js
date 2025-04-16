import PropTypes from "prop-types";

export const Release = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    description: PropTypes.string,
    url: PropTypes.string.isRequired,
    author: PropTypes.string.isRequired,
    authorUrl: PropTypes.string.isRequired,
    authorAvatarUrl: PropTypes.string,
    tag: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    branch: PropTypes.string.isRequired,
    created: PropTypes.string.isRequired,
    published: PropTypes.string.isRequired,
    notes: PropTypes.string,
    sha: PropTypes.string.isRequired,
    commitUrl: PropTypes.string.isRequired
};
