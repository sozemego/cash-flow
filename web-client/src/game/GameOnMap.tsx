import React, { useEffect } from "react";
import {
  WORLD_SERVICE_CITIES_URL,
  WORLD_SERVICE_RESOURCES_URL
} from "../config/urls";
import { ICity } from "../world";
import { cityAdded, resourcesAdded } from "../world/actions";
import { useDispatch } from "react-redux";
import { GameMapFull } from "./GameMapFull";
import { useTruckSocket } from "../truck/useTruckSocket";
import { useFactorySocket } from "../factory/useFactorySocket";
import { FactoryList } from "../factory/FactoryGroup";
import { useGetFactories } from "../factory/selectors";
import { CityList } from "../world/CityList";
import { useGetCities } from "../world/selectors";
import { TruckList } from "../truck/TruckList";
import { useGetTrucks } from "../truck/selectors";

export interface GameOnMapProps {
  height: number;
}

export function GameOnMap({ height }: GameOnMapProps) {
  const dispatch = useDispatch();

  useTruckSocket();
  useFactorySocket();
  const factories = useGetFactories();
  const cities = useGetCities();
  const trucks = Object.values(useGetTrucks());

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

  return (
    <div style={{ display: "grid" }}>
      <div style={{ gridColumn: 1, gridRow: 1, width: "100%" }}>
        <GameMapFull height={height} />
      </div>
      <div
        style={{
          gridColumn: 1,
          gridRow: 1,
          zIndex: 1059,
          maxHeight: height,
          width: "100%",
          overflow: "scroll",
          backgroundColor: "transparent",
          pointerEvents:"none"
        }}
      >
        <div style={{ display: "flex", justifyContent: "space-between"}}>
          <div style={{ width: "15%", background: "white", pointerEvents: "all" }}>
            <FactoryList factories={factories} />
          </div>
          <div></div>
          <div style={{ width: "25%", background: "white", pointerEvents: "all" }}>
            <TruckList trucks={trucks} />
          </div>
        </div>
      </div>
    </div>
  );
}
