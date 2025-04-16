import React from "react";
import {formatUptime} from "../../../utils/dateUtils";

function Uptime({time}) {
    const [uptime, setUptime] = React.useState(0);
    const [startTime, setStartTime] = React.useState(new Date(time));

    React.useEffect(() => {
        const interval = setInterval(() => {
            setUptime(Date.now() - startTime);
        }, 1000);

        return () => clearInterval(interval);
    }, [startTime]);

    return (
        <React.Fragment>
            {formatUptime(uptime)}
        </React.Fragment>
    );
}

export default Uptime;
