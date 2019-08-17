import React from "react";
import { FactoryList } from "../factory/FactoryList";
import { useGetFactories } from "../factory/selectors";
import { TruckList } from "../truck/TruckList";
import { useGetTrucks } from "../truck/selectors";

export function Game() {
  const factories = useGetFactories();
  const trucks = useGetTrucks();

  return (
    <div style={{ display: "flex", flexDirection: "row" }}>
      <div style={{ width: "25%" }}>
        <FactoryList factories={factories} />
      </div>
      <div style={{ width: "25%" }}>
        <TruckList trucks={trucks} />
      </div>
    </div>
  );
}
