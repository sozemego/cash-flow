import { Map, Marker, TileLayer, Tooltip } from "react-leaflet";
import { CityMapTooltip } from "../world/CityMapTooltip";
import React from "react";
import { Icon, LatLngTuple } from "leaflet";
import { useGetCities } from "../world/selectors";

export interface GameMapFullProps {
  height: number;
}

export function GameMapFull({ height }: GameMapFullProps) {
  const [zoom, setZoom] = React.useState(5);
  const position: LatLngTuple = [50.6625, 17.9262];
  const cities = Object.values(useGetCities());

  return (
    <Map
      center={position}
      zoom={zoom}
      style={{ height, width: "auto" }}
      onViewportChanged={viewport => setZoom(viewport.zoom as number)}
    >
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {cities.map(city => (
        <Marker
          key={city.id}
          position={[city.latitude, city.longitude]}
          icon={cityIcon}
        >
          <Tooltip>
            <CityMapTooltip city={city} />
          </Tooltip>
        </Marker>
      ))}
    </Map>
  );
}

const cityIcon = new Icon({
    iconUrl: `img/city_icon.png`,
    iconRetinaUrl: `img/city_icon.png`,
    iconAnchor: undefined,
    popupAnchor: undefined,
    shadowUrl: undefined,
    shadowSize: undefined,
    shadowAnchor: undefined,
    iconSize: [24, 24]
});
