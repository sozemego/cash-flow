import { makeActionCreator } from "../store/actionCreator";

export const GAME_EVENT_INIT = "GAME_EVENT_INIT";
export const gameEventInit = makeActionCreator(GAME_EVENT_INIT);

export const GAME_EVENT = "GAME_EVENT";