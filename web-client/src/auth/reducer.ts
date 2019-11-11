import { AuthAction, AuthState } from "./index";
import produce from "immer";
import { USER_LOGGED_IN } from "./actions";

const initialState: AuthState = {
  user: null
};

export const reducer = produce((state = initialState, action: AuthAction) => {
  switch (action.type) {
    case USER_LOGGED_IN:
      state.user = action.user;
      return state;
    default:
      return state;
  }
});
