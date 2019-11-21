import { combineReducers, compose, createStore as _createStore, applyMiddleware } from "redux";

import { reducer as factory } from "../factory/reducer";
import { reducer as truck } from "../truck/reducer";
import { reducer as world } from "../world/reducer";
import { reducer as clock } from "../clock/reducer";
import { reducer as player } from "../player/reducer";
import { reducer as gameEvent } from "../game-event/reducer";
import { reducer as game } from "../game/reducer";
import { reducer as auth } from "../auth/reducer";

import { truckMiddleware } from "../truck/truckMiddleware";

declare global {
  interface Window {
    __REDUX_DEVTOOLS_EXTENSION_COMPOSE__?: typeof compose;
  }
}

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const enhancer = composeEnhancers(applyMiddleware(truckMiddleware()));

const reducer = combineReducers({
  factory,
  truck,
  world,
  clock,
  player,
  gameEvent,
  game,
  auth
});

export const store = createStore();

function createStore() {
  return _createStore(reducer, enhancer);
}

export default createStore;
