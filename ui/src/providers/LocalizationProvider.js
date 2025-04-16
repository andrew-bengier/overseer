import React from "react";
import PropTypes from "prop-types";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {enUS} from "date-fns/locale/en-US";

const LocalProvider = ({children}) => {
    return (
        <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={enUS}>
            {children}
        </LocalizationProvider>
    );
};

LocalProvider.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.element),
        PropTypes.element
    ]).isRequired
};

export default LocalProvider;
