import React from "react";
import { useGetCities } from "../world/selectors";
import { useGetSelectedCityId } from "./selectors";
import { Tag } from "antd";

export function GameHeader() {
  const cities = useGetCities();
  const selectedCityId = useGetSelectedCityId();

  const city = selectedCityId ? cities[selectedCityId] : null;

  return (
    <div style={{minHeight: "32px", display: "flex", justifyContent: "center"}}>
      {city && (
        <div>
          <Tag color={"red"} style={{fontSize: "1.2rem"}}>{city.name}</Tag>
        </div>
      )}
    </div>
  );
}
