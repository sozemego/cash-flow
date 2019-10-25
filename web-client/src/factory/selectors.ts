import { useSelector } from "react-redux";

export function useGetFactories() {
  return useSelector((state: any) => {
    return state.factory.factories;
  });
}
