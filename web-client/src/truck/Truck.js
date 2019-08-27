import React, { useState } from "react";
import styled from "styled-components";
import { CityInline } from "../city/CityInline";
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

export function Truck({ truck }) {
  const [debug, setDebug] = useState(false);
  const [cityToTravelTo, setCityToTravelTo] = useState(null);

  const { id, name, currentCityId } = truck;
  const allCities = useGetCities();
  const cities = Object.values(allCities).filter(
    city => city.id !== currentCityId
  );

  return (
    <Container>
      <Header>
        <Id>{id}</Id>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <span>
        {name} at <CityInline cityId={currentCityId} />
      </span>
      <Divider />
      <div>
        Travel to{" "}
        <select>
          {cities.map(city => (
            <option
              key={city.id}
              onClick={() => setCityToTravelTo(city.id)}
              value={city.id}
              selected={city.id === cityToTravelTo}
            >
              {city.name}
            </option>
          ))}
        </select>
      </div>
      {debug && <div>{JSON.stringify(truck, null, 2)}</div>}
    </Container>
  );
}
