import { USER_LOGGED_IN } from "./actions";

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

export type AuthAction = UserLoggedIn;
