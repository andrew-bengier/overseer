import React from "react";
import {getApiInfo} from "../../../services/InfoService";

function General() {

    React.useEffect(() => {
        const fetchInfo = async () => {
            await getApiInfo().then(
                response => {
                    console.log(response);
                }
            )
        };

        fetchInfo().then();
    }, []);

    return (
        <React.Fragment>
            General
        </React.Fragment>
    )
}

General.propTypes = {}

export default General;
