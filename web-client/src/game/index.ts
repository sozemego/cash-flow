import { SECTION_DISPLAY_SELECTED } from "./actions";

export interface ResourceDatas {
  [key: string]: ResourceData;
}

export interface ResourceData {
  count: number;
  averagePrice: number;
  saturation: number;
  factories: number;
}

export enum Section {
  FACTORY = "FACTORY",
  TRUCK = "TRUCK",
  CITY = "CITY",
  GAME_EVENT = "GAME_EVENT"
}

export type SectionName = Record<Section, string>;

export type SectionSelection = Record<Section, boolean>;

export interface GameMapProps {
  showMap: boolean;
}

export interface GameState {
  selectedSections: SectionSelection;
}

export interface SectionDisplaySelected {
  type: typeof SECTION_DISPLAY_SELECTED;
  selections: SectionSelection;
}

export type GameAction = SectionDisplaySelected;
