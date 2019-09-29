import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { CityInline } from "../city/CityInline";
import { useGetCities, useGetHighlightedCity } from "../city/selectors";
import {
  createBuyResourceRequestMessage,
  createTruckTravelMessage
} from "./message";
import { useTruckSocket } from "./useTruckSocket";
import { ProgressBar } from "../components/progressBar/ProgressBar";
import { getFormattedTime } from "../clock/business";
import { useGameClock } from "../clock/gameClock";
import { calculateCapacityTaken, Storage } from "../components/Storage";
import { useGetFactories } from "../factory/selectors";
import { ResourceIcon } from "../components/ResourceIcon";
import { useGetPlayer } from "../player/selectors";
import Card from "antd/lib/card";
import Divider from "antd/lib/divider";
import Tag from "antd/lib/tag";
import Icon from "antd/lib/icon";
import { Tooltip } from "antd";
import { Debug } from "../components/Debug";

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

const Id = styled.div`
  color: gray;
  font-size: 0.75rem;
`;

export function Truck({ truck }) {
  const highlightedCityId = useGetHighlightedCity();

  const { id, name, navigation, storage } = truck;
  const { currentCityId, nextCityId } = navigation;

  const cardStyle = Object.assign(
    {},
    highlightedCityId === currentCityId && { background: "#e0e0e0" },
    nextCityId && nextCityId === highlightedCityId && { background: "#ccffff" }
  );

  return (
    <>
      <Header>
        <div>
          <Tag color={"brown"}>{name}</Tag>
          <Tag color={"gray"}>{id}</Tag>
        </div>
        <Tooltip title={<Debug obj={truck} />}>
          <Icon type={"question-circle"} />
        </Tooltip>
      </Header>
      <Card bodyStyle={cardStyle}>
        <span>
          {nextCityId ? "Travelling to " : "In "}
          <CityInline cityId={nextCityId || currentCityId} />
        </span>
        <Divider style={{ margin: "4px" }} />
        <Storage storage={storage} />
        <Divider />
        {!nextCityId && (
          <>
            <Buy truck={truck} cityId={currentCityId} />
            <Divider />
          </>
        )}
        {!nextCityId && <TravelTo truck={truck} />}
        {nextCityId && <Traveling truck={truck} />}
      </Card>
    </>
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

  const { time } = useGameClock({ interval: 1000 });
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

function getResourceList(factories) {
  const resources = [];
  factories.forEach(factory => {
    const { storage } = factory;
    Object.entries(storage.resources).forEach(([resource, count]) => {
      resources.push({ factoryId: factory.id, resource, count });
    });
  });
  return resources;
}

const BuyContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const BuyableResourceContainer = styled.div`
  display: flex;
  align-items: center;
  border-right: 1px dashed gray;
`;

export function Buy({ truck, cityId }) {
  const allFactories = Object.values(useGetFactories());
  const factoriesInCity = allFactories.filter(
    factory => factory.cityId === cityId
  );

  const resources = getResourceList(factoriesInCity);

  return (
    <BuyContainer>
      {resources.map(({ factoryId, resource, count }) => {
        return (
          <FactoryResource
            key={factoryId}
            factoryId={factoryId}
            resource={resource}
            truck={truck}
            count={count}
          />
        );
      })}
    </BuyContainer>
  );
}

export function FactoryResource({ truck, resource, count, factoryId }) {
  const { socket } = useTruckSocket();
  const [selectedCount, setSelectedCount] = useState(0);
  const { cash } = useGetPlayer();

  const capacity = truck.storage.capacity;
  const capacityTaken = calculateCapacityTaken(truck.storage);
  const canAffordAmount = Number((cash / 5).toFixed(0));

  return (
    <BuyableResourceContainer>
      <span>{count}</span>
      <ResourceIcon resource={resource} />
      <input
        type={"number"}
        max={Math.min(
          canAffordAmount,
          Math.min(capacity - capacityTaken, count)
        )}
        min={0}
        style={{ width: "48px" }}
        value={selectedCount}
        onChange={e => setSelectedCount(e.target.value)}
      />
      <button
        onClick={() =>
          socket.send(
            createBuyResourceRequestMessage(
              truck.id,
              factoryId,
              resource,
              selectedCount
            )
          )
        }
      >
        BUY!
      </button>
    </BuyableResourceContainer>
  );
}
