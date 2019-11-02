import {
  CITIES_ADDED,
  CITY_ADDED,
  CITY_HIGHLIGHTED,
  RESOURCES_ADDED
} from "./actions";

export enum ResourceName {
  WOOD = "WOOD",
  STONE = "STONE"
}

export interface Resource {
  name: ResourceName;
  minPrice: number;
  maxPrice: number;
}

export interface ICity {
  id: string;
  name: string;
  factorySlots: number;
  latitude: number;
  longitude: number;
}

export interface CityMap {
  [id: string]: ICity;
}

export interface ResourceMap {
  [name: string]: Resource;
}

export interface WorldState {
  cities: Record<string, ICity>;
  highlightedCity: string | null;
  resources: ResourceMap;
}

export interface CityListProps {
  cities: CityMap;
}

export interface CityProps {
  city: ICity;
}

export interface CityInlineProps {
  cityId: string | null;
}

export interface CityAddedAction {
  type: typeof CITY_ADDED;
  city: ICity;
}

export interface CitiesAddedAction {
  type: typeof CITIES_ADDED;
  cities: ICity[];
}

export interface CityHighlightedAction {
  type: typeof CITY_HIGHLIGHTED;
  cityId: string;
}

export interface ResourcesAddedAction {
  type: typeof RESOURCES_ADDED;
  resources: Resource[];
}

export type WorldAction =
  | CityAddedAction
  | CityHighlightedAction
  | ResourcesAddedAction
  | CitiesAddedAction;

export interface CityMapTooltipProps {
  city: ICity;
}
