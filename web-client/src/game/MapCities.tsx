import React from "react";
import { Marker, Tooltip } from "react-leaflet";
import { createTruckTravelMessage } from "../truck/message";
import { citySelected, truckSelected } from "./actions";
import { CityMapTooltip } from "../world/CityMapTooltip";
import { useGetCities } from "../world/selectors";
import { Icon } from "leaflet";
import { useGetSelectedTruckId } from "./selectors";
import { useTruckSocket } from "../truck/useTruckSocket";
import { useDispatch } from "react-redux";

export function MapCities() {
  const dispatch = useDispatch();
  const cities = useGetCities();
  const selectedTruckId = useGetSelectedTruckId();
  const { socket: truckSocket } = useTruckSocket();

  return (
    <>
      {Object.values(cities).map(city => (
        <Marker
          key={city.id}
          position={[city.latitude, city.longitude]}
          icon={cityIcon}
          onClick={() => {
            if (selectedTruckId) {
              truckSocket.send(
                createTruckTravelMessage(selectedTruckId, city.id)
              );
              dispatch(truckSelected(""));
            } else {
              dispatch(citySelected(city.id));
              dispatch(truckSelected(""));
            }
          }}
        >
          <Tooltip>
            <CityMapTooltip city={city} />
          </Tooltip>
        </Marker>
      ))}
    </>
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
  iconSize: [32, 32]
});
