import React from 'react';
import {useParams} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {selectLibrary} from "../../redux/actions/Actions";
import Libraries from "../Libraries/Libraries";

function Server() {
    const {id} = useParams();
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const server = useSelector((state) => state.server);

    const [selectedLibrary, setSelectedLibrary] = React.useState(null);

    function handleLibraryClick() {
        dispatch(selectLibrary(selectedLibrary));
        // navigate('/Servers/' + scrubRoutePath(server.id));
    }

    return (
        <React.Fragment>
            Test - Server {id}
            <Libraries server={server}/>
        </React.Fragment>
    );
}

Server.propTypes = {}

export default Server;
