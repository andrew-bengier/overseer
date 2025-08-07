import {all} from 'redux-saga/effects';
import library from "./library";

function* saga() {
    yield all([
        library
    ]);
}

export default saga;