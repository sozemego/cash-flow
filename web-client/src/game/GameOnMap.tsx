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

export interface GameOnMapProps {
  height: number;
}

export function GameOnMap({ height }: GameOnMapProps) {
  const dispatch = useDispatch();

  useTruckSocket();
  useFactorySocket();
  const factories = useGetFactories();

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
      <div style={{ gridColumn: 1, gridRow: 1 }}>
        <GameMapFull height={height} />
      </div>
      <div
        style={{
          gridColumn: 1,
          gridRow: 1,
          width: "20%",
          zIndex: 1059,
          maxHeight: height,
          overflow: "scroll",
          backgroundColor: "white"
        }}
      >
        <FactoryList factories={factories} />
      </div>
    </div>
  );
}
