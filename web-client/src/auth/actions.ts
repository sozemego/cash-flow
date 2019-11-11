import { makeActionCreator } from "../store/actionCreator";

export const USER_LOGGED_IN = "USER_LOGGED_IN";
export const userLoggedIn = makeActionCreator(USER_LOGGED_IN, "user");

export const USER_LOGGED_OUT = "USER_LOGGED_OUT";
export const userLoggedOut = makeActionCreator(USER_LOGGED_OUT);
