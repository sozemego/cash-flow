import React from "react";
import { Map, TileLayer } from "react-leaflet";
import { LatLngTuple } from "leaflet";

export interface GameOnMapProps {
  height: number;
}

export function GameOnMap({ height }: GameOnMapProps) {
  if (height <= 0) {
    return null;
  }

  const position: LatLngTuple = [50.6625, 17.9262];
  const zoom = 5;
  return (
    <Map center={position} zoom={zoom} style={{ height, width: "auto" }}>
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
    </Map>
  );
}
