import { ITruck } from "../truck";
import Leaflet from "leaflet";

export function getTruckIcon(truck: ITruck): Leaflet.Icon {
  return new Leaflet.Icon({
    iconUrl: `img/truck/${truck.texture}`,
    iconRetinaUrl: `img/truck/${truck.texture}`,
    iconAnchor: undefined,
    popupAnchor: undefined,
    shadowUrl: undefined,
    shadowSize: undefined,
    shadowAnchor: undefined,
    iconSize: [24, 24]
  });
}
