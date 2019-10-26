import { combineReducers, compose, applyMiddleware, createStore as _createStore } from "redux";

import { reducer as factory } from "../factory/reducer";
import { reducer as truck } from "../truck/reducer";
import { reducer as world } from "../world/reducer";
import { reducer as clock } from "../clock/reducer";
import { reducer as player } from "../player/reducer";
import { reducer as gameEvent } from "../game-event/reducer";
import { gameEventMiddleware } from "../game-event/gameEventMiddleware";

declare global {
  interface Window {
    __REDUX_DEVTOOLS_EXTENSION_COMPOSE__?: typeof compose;
  }
}

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const enhancer = composeEnhancers(applyMiddleware(gameEventMiddleware()));

const reducer = combineReducers({ factory, truck, world, clock, player, gameEvent });

export const store = createStore();

function createStore() {
  return _createStore(reducer, enhancer);
}

export default createStore;
