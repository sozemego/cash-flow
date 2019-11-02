import { useSelector } from "react-redux";
import { IFactory } from "./index";
import { AppState } from "../store";

export function useGetFactories(): IFactory[];
export function useGetFactories(cityId: string): IFactory[];

export function useGetFactories(cityId?: string): IFactory[] {
  const factories = useSelector((state: AppState) => {
    return state.factory.factories;
  });
  if (!cityId) {
    return factories;
  }
  return factories.filter(factory => factory.cityId === cityId);
}
