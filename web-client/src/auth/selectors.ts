import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetUser() {
    return useSelector((state: AppState) => state.auth.user);
}