import React from "react";
import { ICity } from "./index";
import { useGetTrucks } from "../truck/selectors";
import Tag from "antd/lib/tag";
import { TruckIcon } from "../truck/Truck";
import { useGetFactories } from "../factory/selectors";
import { FactoryIcon } from "../factory/FactoryIcon";

export interface CityMapTooltipProps {
  city: ICity;
}

export function CityMapTooltip({ city }: CityMapTooltipProps) {
  const { id, name } = city;

  const trucksInCity = Object.values(useGetTrucks()).filter(
    truck => truck.navigation.currentCityId === id
  );

  const factoriesInCity = useGetFactories().filter(
    factory => factory.cityId === id
  );

  return (
    <div>
      <Tag color={"red"}>{name}</Tag>
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
