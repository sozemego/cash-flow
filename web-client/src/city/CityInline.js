import React from "react";
import { useGetCities } from "./selectors";
import styled from "styled-components";

const FactoryName = styled.span`
  color: #eeb012;
`;

export function CityInline({ cityId }) {
  const cities = useGetCities();
  const city = cities[cityId] || {};

  return <FactoryName>{city.name}</FactoryName>;
}
