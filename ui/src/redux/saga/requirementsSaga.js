import {call, fork, put, takeLatest} from 'redux-saga/effects';
import {REQS_FETCH_FAILURE, REQS_FETCH_REQUEST, REQS_FETCH_SUCCESS} from "../actions/Actions";
import {getAppRequirements} from "../../services/ValidationService";

export function* requestRequirements() {
    try {
        const response = yield call(getAppRequirements);

        yield put({type: REQS_FETCH_SUCCESS, payload: response.data});
    } catch (error) {
        yield put({type: REQS_FETCH_FAILURE, payload: error.message});
    }
}

export function* watchFetchRequirements() {
    yield takeLatest(REQS_FETCH_REQUEST, requestRequirements);
}

export default function* rootSaga() {
    yield fork(watchFetchRequirements);
}