import React, { useEffect } from "react";
import { FactoryGroup } from "../factory/FactoryGroup";
import { useGetFactories } from "../factory/selectors";
import { TruckList } from "../truck/TruckList";
import { useGetTrucks } from "../truck/selectors";
import { CityList } from "../world/CityList";
import { useGetCities } from "../world/selectors";
import { useDispatch } from "react-redux";
import { cityAdded, resourcesAdded } from "../world/actions";
import {
  WORLD_SERVICE_CITIES_URL,
  WORLD_SERVICE_RESOURCES_URL
} from "../config/urls";
import { GameEventList } from "../game-event/GameEventList";

export function Game() {
  const factories = useGetFactories();
  const trucks = useGetTrucks();
  const cities = useGetCities();
  const dispatch = useDispatch();

  useEffect(() => {
    fetch(WORLD_SERVICE_CITIES_URL)
      .then(res => res.json())
      .then(cities => cities.forEach(city => dispatch(cityAdded(city))));
  }, [dispatch]);

  useEffect(() => {
    fetch(WORLD_SERVICE_RESOURCES_URL)
      .then(res => res.json())
      .then(resources => dispatch(resourcesAdded(resources)));
  }, [dispatch]);

  return (
    <div style={{ display: "flex", flexDirection: "row" }}>
      <div style={{ width: "25%" }}>
        <FactoryGroup factories={factories} />
      </div>
      <div style={{ width: "25%" }}>
        <TruckList trucks={Object.values(trucks)} />
      </div>
      <div style={{ width: "25%" }}>
        <CityList cities={cities} />
      </div>
      <div style={{ width: "25%" }}>
        <GameEventList events={[]} />
      </div>
    </div>
  );
}
