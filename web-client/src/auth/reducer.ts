import { AuthAction, AuthState } from "./index";
import produce from "immer";
import { USER_LOGGED_IN, USER_LOGGED_OUT } from "./actions";
import { setToken } from "../rest/client";

const initialState: AuthState = {
  user: null
};

export const reducer = produce((state = initialState, action: AuthAction) => {
  switch (action.type) {
    case USER_LOGGED_IN:
      state.user = action.user;
      setToken(state.user.token);
      return state;
    case USER_LOGGED_OUT:
      state.user = null;
      setToken(null);
      return state;
    default:
      return state;
  }
});
