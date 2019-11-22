import React from "react";
import { Marker, Tooltip } from "react-leaflet";
import { createTruckTravelMessage } from "../truck/message";
import { citySelected, truckSelected } from "../game/actions";
import { CityMapTooltip } from "./CityMapTooltip";
import { useGetCities } from "./selectors";
import { Icon } from "leaflet";
import { useGetSelectedTruckId } from "../game/selectors";
import { useTruckSocket } from "../truck/useTruckSocket";
import { useDispatch } from "react-redux";
import { MapCitiesProps } from "../game";
import { useGetTrucks } from "../truck/selectors";

export function MapCities({ onCityTooltip }: MapCitiesProps) {
  const dispatch = useDispatch();
  const cities = useGetCities();
  const selectedTruckId = useGetSelectedTruckId();
  const { socket: truckSocket } = useTruckSocket();
  const trucks = useGetTrucks();

  return (
    <>
      {Object.values(cities).map(city => (
        <Marker
          key={city.id}
          position={[city.latitude, city.longitude]}
          icon={cityIcon}
          onClick={() => {
            if (selectedTruckId) {
              const truck = trucks[selectedTruckId];
              if (truck && truck.own) {
                truckSocket.send(
                  createTruckTravelMessage(selectedTruckId, city.id)
                );
                dispatch(truckSelected(""));
              }
            } else {
              dispatch(citySelected(city.id));
              dispatch(truckSelected(""));
            }
          }}
          onTooltipOpen={() => onCityTooltip(city.id)}
          onMouseOut={() => onCityTooltip(null)}
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
