import { useSelector } from "react-redux";

export function useGetPlayer() {
  return useSelector((state: any) => state.player.player);
}
