import { makeActionCreator } from "../store/actionCreator";

export const CLOCK_FETCHED = "CLOCK_FETCHED";
export const clockFetched = makeActionCreator(CLOCK_FETCHED, "clock");
