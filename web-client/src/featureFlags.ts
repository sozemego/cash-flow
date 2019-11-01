export const GAME_ON_MAP = "GAME_ON_MAP";

export type Flag = typeof GAME_ON_MAP;

export const FLAGS: Record<Flag, boolean> = {
  GAME_ON_MAP: process.env.REACT_APP_GAME_ON_MAP as unknown as boolean
};
