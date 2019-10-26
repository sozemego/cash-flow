import { Dispatch, Middleware, MiddlewareAPI } from "redux";
import { Action, AppState } from "../store";
import { GameEventAction } from "./index";
import { getGameEventText } from "./business";

export function gameEventMiddleware(): Middleware<void, AppState, Dispatch> {
  return function middleware(api: MiddlewareAPI<Dispatch, AppState>) {
    return function next(next: Dispatch<Action>) {
      return function action(action: Action) {
        if (action.type === "GAME_EVENT") {
          const gameEventAction = action as GameEventAction;
          gameEventAction.text = getGameEventText(gameEventAction, api);
          next(gameEventAction);
        } else {
          next(action);
        }
      };
    };
  };
}
