import React from "react";
import styled from "styled-components";
import { City } from "./City";
import { Typography } from "antd";
import { Divider } from "antd/es";

const Container = styled.div`
  margin-left: 12px;
`;

export function CityList({ cities }) {
  const cityList = Object.values(cities);

  return (
    <Container>
      <Typography level={3}>CITIES</Typography>
      <Divider/>
      {cityList.map(city => (
        <City key={city.id} city={city} />
      ))}
    </Container>
  );
}
