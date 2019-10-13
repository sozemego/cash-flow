export interface Resource {
  name: string;
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
