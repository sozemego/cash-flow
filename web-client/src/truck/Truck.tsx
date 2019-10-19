import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { CityInline } from "../world/CityInline";
import { useGetCities, useGetHighlightedCity } from "../world/selectors";
import {
  createBuyResourceRequestMessage,
  createTruckTravelMessage,
  dump
} from "./message";
import { useTruckSocket } from "./useTruckSocket";
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
import InputNumber from "antd/lib/input-number";
import Button from "antd/lib/button";
import Progress from "antd/lib/progress";
import Select from "antd/lib/select";
import { IFactory } from "../factory/index.d";
import { PointerEventsProperty } from "csstype";

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

export function Truck({ truck }) {
  const highlightedCityId = useGetHighlightedCity();
  const { socket } = useTruckSocket();

  const { id, name, navigation, storage } = truck;
  const { currentCityId, nextCityId } = navigation;

  const cardStyle = Object.assign(
    {},
    highlightedCityId === currentCityId ? { background: "#e0e0e0" } : {},
    nextCityId && nextCityId === highlightedCityId
      ? { background: "#ccffff" }
      : {}
  );

  const buyStyle = Object.assign(
    {},
    nextCityId
      ? { opacity: 0.5, pointerEvents: "none" as PointerEventsProperty }
      : { opacity: 1, pointerEvents: "all" as PointerEventsProperty }
  );

  return (
    <>
      <Header>
        <div>
          <Tag color={"brown"}>{name}</Tag>
          <Tag color={"gray"}>{id}</Tag>
        </div>
        <div>
          <Icon
            type="delete"
            onClick={() => {
              socket.send(dump(id));
            }}
          />
          <Tooltip title={<Debug obj={truck} />}>
            <Icon type={"question-circle"} />
          </Tooltip>
        </div>
      </Header>
      <Card bodyStyle={cardStyle}>
        <span>
          {nextCityId ? "Travelling to " : "In "}
          <CityInline cityId={nextCityId || currentCityId} />
        </span>
        <Divider style={{ margin: "4px" }} />
        <Storage storage={storage} />
        <Divider style={{ margin: "4px" }} />
        <div style={buyStyle}>
          <Buy truck={truck} cityId={nextCityId || currentCityId} />
          <Divider style={{ margin: "4px" }} />
        </div>
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

function distanceTime(currentCity, targetCity, speed) {
  const distance = calculateDistance(currentCity, targetCity);
  const travelTime = (distance / speed).toFixed(1);

  return `${distance}km - ${travelTime}h`;
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

  return (
    <div>
      <div>
        <div>Speed {speed} km/h</div>
        <span>Travel to </span>
      </div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <div>
          <Select
            onChange={value => setCityToTravelToId(value)}
            style={{ width: 250 }}
            value={cityToTravelToId}
          >
            {citiesToTravelTo.map(city => (
              //@ts-ignore
              <Select.Option
                key={city.id}
                onClick={() => setCityToTravelToId(city.id)}
                value={city.id}
              >
                {city.name} [
                {distanceTime(currentCity, allCities[city.id], speed)}]
              </Select.Option>
            ))}
          </Select>
        </div>
        <Button
          type={"primary"}
          onClick={() => {
            socket.send(createTruckTravelMessage(id, cityToTravelToId));
          }}
          disabled={cityToTravelToId === ""}
        >
          GO
        </Button>
      </div>
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
        <Progress
          percent={(travelTimePassed / totalTime) * 100}
          showInfo={false}
        />
        <div>
          <div>{getFormattedTime(new Date(arrivalTime))}</div>
          <CityInline cityId={nextCityId} />
        </div>
      </div>
    </div>
  );
}

interface ResourceFromFactory {
  factoryId: string;
  resource: string;
  count: number;
  price: number;
}

function getResourceList(factories: IFactory[]): ResourceFromFactory[] {
  const resources: ResourceFromFactory[] = [];
  factories.forEach(factory => {
    const { storage } = factory;
    Object.entries(storage).forEach(([resource, slot]) => {
      resources.push({ factoryId: factory.id, resource, count: slot.count, price: slot.price });
    });
  });
  return resources;
}

const BuyContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

const BuyableResourceContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

export function Buy({ truck, cityId }) {
  const allFactories: IFactory[] = Object.values(useGetFactories());
  const factoriesInCity = allFactories.filter(
    factory => factory.cityId === cityId
  );

  const resources = getResourceList(factoriesInCity);

  return (
    <BuyContainer>
      <span>
        Resources to purchase in <CityInline cityId={cityId} />
      </span>
      {resources.map(({ factoryId, resource, count, price }) => {
        return (
          <FactoryResource
            key={factoryId}
            factoryId={factoryId}
            resource={resource}
            truck={truck}
            count={count}
            price={price}
          />
        );
      })}
    </BuyContainer>
  );
}

export function FactoryResource({ truck, resource, count, price, factoryId }) {
  const { socket } = useTruckSocket();
  const [selectedCount, setSelectedCount] = useState(0);
  const { cash = 0 } = useGetPlayer();

  const capacity = truck.storage.capacity;
  const capacityTaken = calculateCapacityTaken(truck.storage);
  const canAffordAmount = Number((cash / price).toFixed(0));
  const max = Math.min(
    canAffordAmount,
    Math.min(capacity - capacityTaken, count)
  );
  const buyDisabled = selectedCount === 0 || max === 0;

  return (
    <BuyableResourceContainer>
      <div>
        <Tag color={'magenta'}>${price}</Tag>
        <ResourceIcon resource={resource} />
        <span>
          {count}
        </span>
      </div>
      <div style={{ display: "flex" }}>
        <InputNumber
          max={Math.min(
            canAffordAmount,
            Math.min(capacity - capacityTaken, count)
          )}
          min={0}
          value={selectedCount}
          //@ts-ignore
          onChange={setSelectedCount}
        />
        <div style={{ minWidth: "84px" }}>
          <Button
            icon={"dollar"}
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
            disabled={buyDisabled}
            block
            style={{ backgroundColor: buyDisabled ? "#DDDDDD" : "#78D89C" }}
          >
            {selectedCount * price}
          </Button>
        </div>
      </div>
    </BuyableResourceContainer>
  );
}
