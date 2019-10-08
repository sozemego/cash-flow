import { makeActionCreator } from "../store/actionCreator";

export const playerAdded = makeActionCreator("PLAYER_ADDED", "player");
export const playerCashChanged = makeActionCreator("PLAYER_CASH_CHANGED", "amount");
