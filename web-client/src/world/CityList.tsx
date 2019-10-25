import React from "react";
import styled from "styled-components";
import { Typography } from "antd/lib";
import { Divider } from "antd/lib";
import { City } from "./City";
import { CityListProps } from "./index";

const Container = styled.div`
  margin-left: 12px;
`;

export function CityList({ cities }: CityListProps) {
  const cityList = Object.values(cities);

  return (
    <Container>
      <Typography>Cities</Typography>
      <Divider />
      {cityList.map(city => (
        <City key={city.id} city={city} />
      ))}
    </Container>
  );
}
