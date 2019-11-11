import { makeActionCreator } from "../store/actionCreator";

export const USER_LOGGED_IN = "USER_LOGGED_IN";
export const userLoggedIn = makeActionCreator(USER_LOGGED_IN, "user");
