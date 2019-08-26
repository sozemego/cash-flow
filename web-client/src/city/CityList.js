import React from "react";
import styled from "styled-components";
import { City } from "./City";

const Container = styled.div`
  margin-left: 12px;
`;

const Header = styled.div`
  min-height: 50px;
`;

export function CityList({ cities }) {
  const cityList = Object.values(cities);

  return (
    <Container>
      <Header>Cities</Header>
      <hr />
      {cityList.map(city => (
        <City key={city.id} city={city} />
      ))}
    </Container>
  );
}
