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
