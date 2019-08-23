import React, { useState } from "react";
import styled from "styled-components";
import { useGetCities } from "../city/selectors";

const Container = styled.div`
  margin: 2px;
  padding: 12px;
  border: dotted gray 1px;
`;

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Id = styled.div`
  color: gray;
  font-size: 0.75rem;
`;

const Divider = styled.div`
  width: 25%;
  opacity: 0.25;
  margin-left: 0;
`;

const Debug = styled.div`
  cursor: pointer;
  border: 1px solid black;
`;

const FactoryName = styled.span`
  color: #eeb012;
`;

export function Truck({ truck }) {
  const [debug, setDebug] = useState(false);
  const cities = useGetCities();

  const { id, name, currentCityId } = truck;

  const city = cities[currentCityId];
  const cityName = city ? city.name : currentCityId;

  return (
    <Container>
      <Header>
        <Id>{id}</Id>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <span>
        {name} at <FactoryName>{cityName}</FactoryName>
      </span>
      <Divider />
      {debug && <div>{JSON.stringify(truck, null, 2)}</div>}
    </Container>
  );
}
