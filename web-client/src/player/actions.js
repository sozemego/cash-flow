import { makeActionCreator } from "../store/actionCreator";

export const playerAdded = makeActionCreator("PLAYER_ADDED", "player");
console.log(playerAdded);
