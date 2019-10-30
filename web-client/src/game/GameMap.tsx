import React from "react";
import { Icon } from "antd";
import Modal from "antd/lib/modal";
import { Marker, Popup, TileLayer, Map, Tooltip } from "react-leaflet";
import { LatLngTuple } from "leaflet";
import { useGetCities } from "../world/selectors";
import { ICity } from "../world";

export function GameMap() {
  const position: LatLngTuple = [50.6625, 17.9262];
  const zoom = 5;

  const cities: ICity[] = Object.values(useGetCities());

  return (
    <Map
      center={position}
      zoom={zoom}
      style={{ height: "620px", width: "auto" }}
    >
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {cities.map(city => (
        <Marker key={city.id} position={[city.latitude, city.longitude]}>
          <Tooltip>
            {city.name}
          </Tooltip>
        </Marker>
      ))}
    </Map>
  );
}

export function GameMapIcon() {
  const [showMap, setShowMap] = React.useState<boolean>(false);

  return (
    <>
      <Icon type="global" onClick={() => setShowMap(!showMap)} />
      <Modal title="World map" visible={showMap} width={"750px"}>
        <GameMap />
      </Modal>
    </>
  );
}
