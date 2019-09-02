import React, { useState, useEffect } from "react";
import styled, { css } from "styled-components";
import { CityInline } from "../city/CityInline";
import { useGetCities, useGetHighlightedCity } from "../city/selectors";
import { createTruckTravelMessage } from "./message";
import { useTruckSocket } from "./useTruckSocket";
import { ProgressBar } from "../components/progressBar/ProgressBar";
import { getFormattedTime } from "../clock/business";
import { useGameClock } from "../clock/gameClock";

const Container = styled.div`
  margin: 2px;
  padding: 12px;
  border: dotted gray 1px;
  ${props =>
    props.highlighted &&
    css`
      background: #e0e0e0;
    `}
  ${props =>
    props.next &&
    css`
      background: #ccffff;
    `}
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
  const highlightedCityId = useGetHighlightedCity();

  const { id, name, navigation } = truck;
  const { currentCityId, nextCityId } = navigation;

  return (
    <Container
      highlighted={highlightedCityId === currentCityId}
      next={nextCityId && nextCityId === highlightedCityId}
    >
      <Header>
        <Id>{id}</Id>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <span>
        {name} {nextCityId ? "travelling to " : "in "}
        <CityInline cityId={nextCityId || currentCityId} />
      </span>
      <Divider />
      {!nextCityId && <TravelTo truck={truck} />}
      {nextCityId && <Traveling truck={truck} />}
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
  const { id, speed, navigation } = truck;
  const { currentCityId, nextCityId } = navigation;
  const { socket } = useTruckSocket();

  const allCities = useGetCities();
  const [cityToTravelToId, setCityToTravelToId] = useState("");
  const citiesToTravelTo = Object.values(allCities).filter(
    city => city.id !== currentCityId
  );

  useEffect(() => {
    if (!cityToTravelToId && !nextCityId && citiesToTravelTo.length > 0) {
      setCityToTravelToId(citiesToTravelTo[0].id);
    }
  }, [cityToTravelToId, citiesToTravelTo, nextCityId]);

  const currentCity = allCities[currentCityId];
  const cityToTravelTo = allCities[cityToTravelToId];

  const distance = calculateDistance(currentCity, cityToTravelTo);
  const travelTime = (distance / speed).toFixed(1);

  return (
    <div>
      <div>Speed {speed} km/h</div>
      <span>Travel to </span>
      <select
        value={cityToTravelToId}
        onChange={e => setCityToTravelToId(e.target.value)}
      >
        {citiesToTravelTo.map(city => (
          <option
            key={city.id}
            onClick={() => setCityToTravelToId(city.id)}
            value={city.id}
          >
            {city.name}
          </option>
        ))}
      </select>
      <span>
        Distance: {distance} km. {travelTime}h
      </span>
      <button
        onClick={() => {
          socket.send(createTruckTravelMessage(id, cityToTravelToId));
        }}
        disabled={cityToTravelToId === ""}
      >
        GO
      </button>
    </div>
  );
}

function Traveling({ truck }) {
  const { navigation, speed } = truck;
  const { currentCityId, nextCityId, arrivalTime, startTime } = navigation;

  const { time } = useGameClock({ interval: 5000 });
  let travelTimePassed = time - startTime;
  const totalTime = arrivalTime - startTime;

  const cities = useGetCities();
  const distance = calculateDistance(cities[currentCityId], cities[nextCityId]);
  const distanceCovered = (distance * (travelTimePassed / totalTime)).toFixed(
    1
  );

  return (
    <div>
      <div>
        Travelling at {speed}km/h. {distanceCovered} / {distance} km
      </div>
      <div
        style={{ display: "flex", flexDirection: "row", alignItems: "center" }}
      >
        <div>
          <div>{getFormattedTime(new Date(startTime))}</div>
          <CityInline cityId={currentCityId} />
        </div>
        <ProgressBar time={totalTime} current={travelTimePassed} height={6} />
        <div>
          <div>{getFormattedTime(new Date(arrivalTime))}</div>
          <CityInline cityId={nextCityId} />
        </div>
      </div>
    </div>
  );
}
