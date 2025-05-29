import {all} from 'redux-saga/effects';
import server from "./server";
import library from "./library";

function* saga() {
    yield all([
        server,
        library
    ]);
}

export default saga;