import { ITruck } from "../truck";
import Leaflet from "leaflet";
import { getTruckTexture } from "../truck/business";

export function getTruckIcon(truck: ITruck): Leaflet.Icon {
  const truckTexture = getTruckTexture(truck);
  return new Leaflet.Icon({
    iconUrl: truckTexture,
    iconRetinaUrl: truckTexture,
    iconAnchor: undefined,
    popupAnchor: undefined,
    shadowUrl: undefined,
    shadowSize: undefined,
    shadowAnchor: undefined,
    iconSize: [24, 24]
  });
}
