import React from "react";
import { useGetCities } from "./selectors";
import Tag from "antd/lib/tag";

export function CityInline({ cityId }) {
  const cities = useGetCities();
  const city = cities[cityId] || {};

  return <Tag color={"gold"}>{city.name}</Tag>;
}
