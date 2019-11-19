import { ITruck } from "../truck";
import Leaflet from "leaflet";

export function getTruckIcon(truck: ITruck, own: boolean): Leaflet.Icon {
  const texture = truck.texture;
  const parts = texture.split(".");
  return new Leaflet.Icon({
    iconUrl: `/img/truck/${parts[0]}${own ? "_own." : "."}${parts[1]}`,
    iconRetinaUrl: `/img/truck/${parts[0]}${own ? "_own." : "."}${parts[1]}`,
    iconAnchor: undefined,
    popupAnchor: undefined,
    shadowUrl: undefined,
    shadowSize: undefined,
    shadowAnchor: undefined,
    iconSize: [24, 24]
  });
}
