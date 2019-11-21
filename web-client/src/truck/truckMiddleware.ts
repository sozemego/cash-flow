import { Dispatch, Middleware, MiddlewareAPI } from "redux";
import { Action, AppState } from "../store";
import { TRUCK_ADDED } from "./actions";
import { getPlayer } from "../player/selectors";

export function truckMiddleware(): Middleware<void, AppState, Dispatch> {
    return function middleware(api: MiddlewareAPI<Dispatch, AppState>) {
        return function next(next: Dispatch<Action>) {
            return function action(action: Action) {
                if (action.type === TRUCK_ADDED) {
                    const { truck, playerId } = action;
                    const { id } = getPlayer(api.getState());
                    truck.own = playerId === id;
                    next(action);
                } else {
                    next(action);
                }
            };
        };
    };
}