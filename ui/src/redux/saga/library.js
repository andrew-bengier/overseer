import {put, takeLatest} from 'redux-saga/effects';
import {RESET_LIBRARY, SELECT_LIBRARY} from '../actions/Actions';

function* setLibrary(action) {
    const {library} = action;

    if (library != null) {
        yield put({type: SELECT_LIBRARY, library: library});
    } else {
        yield put({type: RESET_LIBRARY});
    }
}

function* libraryWatcher() {
    yield takeLatest(SELECT_LIBRARY, setLibrary);
}

export default libraryWatcher;
