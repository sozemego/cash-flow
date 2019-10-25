import { useSelector } from "react-redux";
import { IFactory } from "./index";

export function useGetFactories(): IFactory[] {
  return useSelector((state: any) => {
    return state.factory.factories;
  });
}
