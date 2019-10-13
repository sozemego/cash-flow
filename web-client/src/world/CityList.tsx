import React from "react";
import styled from "styled-components";
import { Typography } from "antd/lib";
import { Divider } from "antd/lib";
import {ICity} from "./index.d";
import {City} from "./City";

const Container = styled.div`
  margin-left: 12px;
`;

export interface CityListProps {
    cities: ICity[]
}

export function CityList({ cities }: CityListProps) {
  const cityList = Object.values(cities);

  return (
    <Container>
      <Typography>Cities</Typography>
      <Divider/>
      {cityList.map(city => (
        <City key={city.id} city={city} />
      ))}
    </Container>
  );
}
