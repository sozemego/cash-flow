import { useSelector } from "react-redux";

export function useGetPlayer() {
  return useSelector(state => state.player.player);
}
