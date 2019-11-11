import { USER_LOGGED_IN, USER_LOGGED_OUT } from "./actions";

export interface AuthState {
  user: IUser | null;
}

export interface IUser {
  id: string;
  name: string;
  token: string;
}

export interface UserLoggedIn {
  type: typeof USER_LOGGED_IN;
  user: IUser;
}

export interface UserLoggedOut {
  type: typeof USER_LOGGED_OUT;
}

export type AuthAction = UserLoggedIn | UserLoggedOut;
