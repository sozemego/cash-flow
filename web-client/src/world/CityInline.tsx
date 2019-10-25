import React from "react";
import { useGetCities } from "./selectors";
import Tag from "antd/lib/tag";
import { CityInlineProps } from "./index";

export function CityInline({ cityId }: CityInlineProps) {
  const cities = useGetCities();
  const city = cities[cityId || 'void'] || {};

  return <Tag color={"gold"}>{city.name}</Tag>;
}
