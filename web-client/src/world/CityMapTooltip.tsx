import React from "react";
import { CityMapTooltipProps } from "./index";
import { useGetTrucks } from "../truck/selectors";
import Tag from "antd/lib/tag";
import { TruckIcon } from "../truck/Truck";
import { useGetFactories } from "../factory/selectors";
import { FactoryIcon } from "../factory/FactoryIcon";
import { useGetSelectedTruckId } from "../game/selectors";
import { useGetCity } from "./selectors";
import { distanceTime } from "../truck/business";
import { useGameClock } from "../clock/useGameClock";

export function CityMapTooltip({ city }: CityMapTooltipProps) {
  const { id, name } = city;

  const trucks = useGetTrucks();

  const trucksInCity = Object.values(trucks).filter(
    truck => truck.navigation.currentCityId === id
  );

  const factoriesInCity = useGetFactories().filter(
    factory => factory.cityId === id
  );

  const selectedTruckId = useGetSelectedTruckId();
  const truck = trucks[selectedTruckId];
  const currentCity = useGetCity(truck ? truck.navigation.currentCityId : "");

  useGameClock({ interval: 2500 });

  return (
    <div>
      <Tag color={"red"}>{name}</Tag>
      {selectedTruckId && distanceTime(currentCity!, city, truck.speed)}
      {trucksInCity.length > 0 && (
        <div>
          Trucks:
          {trucksInCity.map(truck => (
            <TruckIcon key={truck.id} texture={truck.texture} />
          ))}
        </div>
      )}
      {factoriesInCity.length > 0 && (
        <div>
          Factories:
          {factoriesInCity.map(factory => (
            <FactoryIcon key={factory.id} texture={factory.texture} />
          ))}
        </div>
      )}
    </div>
  );
}
