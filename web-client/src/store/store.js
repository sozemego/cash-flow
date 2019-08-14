import { createStore as _createStore, compose, combineReducers } from "redux";

import { reducer as factory } from "../factory/reducer";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const enhancer = composeEnhancers();

const reducer = combineReducers({ factory });

export const store = createStore();

function createStore() {
  return _createStore(reducer, enhancer);
}

export default createStore;