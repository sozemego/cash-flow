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

export interface GameOnMapProps {
  height: number;
}

export function GameOnMap({ height }: GameOnMapProps) {
  const dispatch = useDispatch();

  useTruckSocket();
  useFactorySocket();

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

  return <GameMapFull height={height} />;
}
