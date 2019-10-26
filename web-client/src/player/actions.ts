import { makeActionCreator } from "../store/actionCreator";

export const PLAYER_INIT = "PLAYER_INIT";
export const playerInit = makeActionCreator(PLAYER_INIT);

export const PLAYER_ADDED = "PLAYER_ADDED";
export const playerAdded = makeActionCreator(PLAYER_ADDED, "player");

export const PLAYER_CASH_CHANGED = "PLAYER_CASH_CHANGED";
export const playerCashChanged = makeActionCreator(
  PLAYER_CASH_CHANGED,
  "amount"
);
