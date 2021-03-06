import { Map, TileLayer } from "react-leaflet";
import React from "react";
import { LatLngTuple } from "leaflet";
import { useGameClock } from "../clock/useGameClock";
import { GameMapFullProps } from "./index";
import { MapCities } from "../world/MapCities";
import { TravellingTrucks } from "../truck/TravellingTrucks";
import { CityTrucks, TooltipTravelLine } from "../truck/CityTrucks";

export function GameMapFull({ height }: GameMapFullProps) {
  let [zoom, setZoom] = React.useState(5);
  zoom = zoom > 7 ? 7 : zoom;
  zoom = zoom < 3 ? 3 : zoom;
  const position: LatLngTuple = [50.6625, 17.9262];
  const [cityTooltipId, setCityTooltipId] = React.useState<string | null>(null);

  useGameClock({ interval: 2500 });

  const mapRef = React.useRef<Map>();

  React.useLayoutEffect(() => {
    mapRef.current!.leafletElement.setMinZoom(3);
    mapRef.current!.leafletElement.setMaxZoom(7);
  });

  return (
    <Map
      center={position}
      zoom={zoom}
      style={{ height, width: "auto" }}
      onViewportChanged={viewport => setZoom(viewport.zoom as number)}
      zoomControl={false}
      //@ts-ignore
      ref={mapRef}
    >
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <MapCities onCityTooltip={setCityTooltipId} />
      <TravellingTrucks />
      <CityTrucks zoom={zoom} />
      <TooltipTravelLine cityId={cityTooltipId}/>
    </Map>
  );
}
