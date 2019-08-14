import { useSelector } from "react-redux";


export function useGetFactories() {
  const factories = useSelector(state => {
    return state.factory.factories;
  });
  return factories;
}
