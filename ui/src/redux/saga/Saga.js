import {all, fork} from 'redux-saga/effects';
import requirements from './requirementsSaga';

function* saga() {
    yield all([
        fork(requirements)
    ]);
}

export default saga;