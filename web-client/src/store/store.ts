import { combineReducers } from "redux";
import { configureStore } from "@reduxjs/toolkit";

import { reducer as factory } from "../factory/reducer";
import { reducer as truck } from "../truck/reducer";
import { reducer as world } from "../world/reducer";
import { reducer as clock } from "../clock/reducer";
import { reducer as player } from "../player/reducer";
import { reducer as gameEvent } from "../game-event/reducer";
import { reducer as game } from "../game/reducer";
import { reducer as auth } from "../auth/reducer";

import { truckMiddleware } from "../truck/truckMiddleware";

//@ts-ignore
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

//@ts-ignore
export const store = configureStore({ reducer, middleware: [truckMiddleware()] });

function createStore() {
  return configureStore({ reducer });
}

export default createStore;
