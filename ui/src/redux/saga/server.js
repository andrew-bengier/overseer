import {put, takeLatest} from 'redux-saga/effects';
import {RESET_SERVER, SELECT_SERVER} from '../actions/Actions';

function* setServer(action) {
    const {server} = action;

    if (server != null) {
        yield put({type: SELECT_SERVER, server: server});
    } else {
        yield put({type: RESET_SERVER});
    }
}

function* serverWatcher() {
    yield takeLatest(SELECT_SERVER, setServer);
}

export default serverWatcher;
