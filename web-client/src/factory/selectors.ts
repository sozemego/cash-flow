import { useSelector } from "react-redux";
import { IFactory } from "./index";
import { AppState } from "../store";

export function useGetFactories(): IFactory[] {
  return useSelector((state: AppState) => {
    return state.factory.factories;
  });
}
