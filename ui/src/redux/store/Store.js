import {configureStore} from '@reduxjs/toolkit';
import createSagaMiddleware from 'redux-saga';
import rootReducer from '../reducers';
import saga from '../saga/Saga';

const sagaMiddleware = createSagaMiddleware();

const Store = configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: false
        }).concat(
            [sagaMiddleware]
        )
});

sagaMiddleware.run(saga);

export default Store;
