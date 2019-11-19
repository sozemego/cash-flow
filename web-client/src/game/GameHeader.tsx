import React from "react";
import { useGetCities } from "../world/selectors";
import { useGetSelectedCityId, useGetSelectedTruckId } from "./selectors";
import { Tag } from "antd";
import { useGetTrucks } from "../truck/selectors";
import { useGetFactories } from "../factory/selectors";
import { FactoryIcon } from "../factory/FactoryIcon";
import { TruckIcon } from "../truck/Truck";

export function GameHeader() {
  const cities = useGetCities();
  const selectedCityId = useGetSelectedCityId();
  const selectedTruckId = useGetSelectedTruckId();
  let trucks = useGetTrucks(selectedCityId);
  let factories = useGetFactories(selectedCityId);

  const city = selectedCityId ? cities[selectedCityId] : null;

  if (!city) {
    factories = [];
    trucks = {};
  }

  const truck = trucks[selectedTruckId];

  return (
    <div
      style={{
        minHeight: "32px",
        display: "flex",
        justifyContent: "space-between"
      }}
    >
      <div>
        {factories.map(factory => (
          <FactoryIcon key={factory.id} texture={factory.texture} />
        ))}
      </div>
      <div style={{ display: "flex" }}>
        {truck && (
          <div style={{ display: "flex", flexDirection: "row" }}>
            <TruckIcon truck={truck} />
            <Tag color={"red"} style={{ fontSize: "1.2rem" }}>
              {truck.name}
            </Tag>
          </div>
        )}
        {city && (
          <div>
            <Tag color={"red"} style={{ fontSize: "1.2rem" }}>
              {city.name}
            </Tag>
          </div>
        )}
      </div>
      <div>
        {Object.values(trucks).map(truck => (
          <TruckIcon key={truck.id} truck={truck} />
        ))}
      </div>
    </div>
  );
}
