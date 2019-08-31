import { combineReducers, compose, createStore as _createStore } from "redux";

import { reducer as factory } from "../factory/reducer";
import { reducer as truck } from "../truck/reducer";
import { reducer as city } from "../city/reducer";
import { reducer as clock } from "../clock/reducer";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const enhancer = composeEnhancers();

const reducer = combineReducers({ factory, truck, city, clock });

export const store = createStore();

function createStore() {
	return _createStore(reducer, enhancer);
}

export default createStore;
