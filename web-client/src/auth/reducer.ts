import { AuthAction, AuthState, IUser } from "./index";
import produce from "immer";
import { USER_LOGGED_IN, USER_LOGGED_OUT } from "./actions";
import { setToken } from "../rest/client";

const initialState: AuthState = {
  user: getUser()
};

export const reducer = produce((state = initialState, action: AuthAction) => {
  switch (action.type) {
    case USER_LOGGED_IN:
      state.user = action.user;
      setToken(state.user.token);
      saveUser(action.user);
      return state;
    case USER_LOGGED_OUT:
      state.user = null;
      setToken(null);
      saveUser(null);
      return state;
    default:
      return state;
  }
});

function getUser(): IUser | null {
  const userItem = localStorage.getItem("user");
  if (userItem === null) {
    setToken(null);
    return null;
  }
  const user = JSON.parse(userItem);
  setToken(user.token);
  return user;
}

function saveUser(user: IUser | null) {
  if (user === null) {
    localStorage.removeItem("user");
  }
  localStorage.setItem("user", JSON.stringify(user));
}
