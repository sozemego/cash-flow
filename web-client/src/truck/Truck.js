import React, { useState, useEffect } from "react";
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

const Divider = styled.hr`
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

  const { id, name, currentCityId } = truck;

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
      <TravelTo truck={truck} />
      {debug && <div>{JSON.stringify(truck, null, 2)}</div>}
    </Container>
  );
}

function calculateDistance(from, to) {
  if (!from || !to) {
    return -1;
  }
  const R = 6371e3; // metres
  const { latitude: fromLat, longitude: fromLong } = from;
  const { latitude: toLat, longitude: toLong } = to;
  const φ1 = (fromLat * Math.PI) / 180;
  const φ2 = (toLat * Math.PI) / 180;
  const Δφ = ((toLat - fromLat) * Math.PI) / 180;
  const Δλ = ((toLong - fromLong) * Math.PI) / 180;

  const a =
    Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
    Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  const d = R * c;
  return Number((d / 1000).toFixed(0));
}

export function TravelTo({ truck }) {
  const { id, name, speed, currentCityId } = truck;
  const allCities = useGetCities();
  const [cityToTravelToId, setCityToTravelToId] = useState("null");

  useEffect(() => {
    const cityArray = Object.values(allCities);
    if (cityArray.length > 0) {
      setCityToTravelToId(cityArray[0].id);
    }
  }, [allCities]);

  const cities = Object.values(allCities).filter(
    city => city.id !== currentCityId
  );
  const currentCity = allCities[currentCityId];
  const cityToTravelTo = allCities[cityToTravelToId];

  const distance = calculateDistance(currentCity, cityToTravelTo);
  const travelTime = distance / speed;

  return (
    <div>
      <div>Speed {speed} km/h</div>
      <span>Travel to </span>
      <select
        value={cityToTravelToId}
        onChange={e => setCityToTravelToId(e.target.value)}
      >
        {cities.map(city => (
          <option
            key={city.id}
            onClick={() => setCityToTravelToId(city.id)}
            value={city.id}
          >
            {city.name}
          </option>
        ))}
      </select>
      <span>Distance: {distance} km. {travelTime}h</span>
      <button>GO</button>
    </div>
  );
}
