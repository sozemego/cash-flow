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
import { ICity } from "../world";
import { useGetEvents } from "../game-event/selectors";
import { useGetSelectedSections } from "./selectors";
import { Section } from "./index";

export function Game() {
  const factories = useGetFactories();
  const trucks = useGetTrucks();
  const cities = useGetCities();
  const events = useGetEvents();
  const selectedSections = useGetSelectedSections();
  const dispatch = useDispatch();

  useEffect(() => {
    fetch(WORLD_SERVICE_CITIES_URL)
      .then<ICity[]>(res => res.json())
      .then(cities => cities.forEach(city => dispatch(cityAdded(city))));
  }, [dispatch]);

  useEffect(() => {
    fetch(WORLD_SERVICE_RESOURCES_URL)
      .then(res => res.json())
      .then(resources => dispatch(resourcesAdded(resources)));
  }, [dispatch]);

  const numOfSections = Object.values(selectedSections).filter(Boolean).length;
  const width = 100 / numOfSections;

  return (
    <div style={{ display: "flex", flexDirection: "row" }}>
      {selectedSections[Section.FACTORY] && (
        <div style={{ width: `${width}%` }}>
          <FactoryGroup factories={factories} />
        </div>
      )}
      {selectedSections[Section.TRUCK] && (
        <div style={{ width: `${width}%` }}>
          <TruckList trucks={Object.values(trucks)} />
        </div>
      )}
      {selectedSections[Section.CITY] && (
        <div style={{ width: `${width}%` }}>
          <CityList cities={cities} />
        </div>
      )}
      {selectedSections[Section.GAME_EVENT] && (
        <div style={{ width: `${width}%` }}>
          <GameEventList events={events} />
        </div>
      )}
    </div>
  );
}
