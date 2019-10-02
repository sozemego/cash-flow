import React, { useState } from "react";
import styled from "styled-components";
import { getFormattedTime } from "../clock/business";
import { useGameClock } from "../clock/gameClock";
import { useGetCities, useGetHighlightedCity } from "../city/selectors";
import { useDispatch } from "react-redux";
import { cityHighlighted } from "../city/actions";
import { Storage } from "../components/Storage";
import Divider from "antd/lib/divider";
import Progress from "antd/lib/progress";
import Card from "antd/lib/card";
import Tag from "antd/lib/tag";

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Producer = styled.div`
  display: flex;
  flex-direction: column;
`;

const ProducerProgress = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const ProductionDate = styled.div`
  margin: 4px;
`;

const Debug = styled.div`
  cursor: pointer;
  border: 1px solid black;
`;

function formatDuration(minutes) {
  let hours = 0;
  while (minutes >= 60) {
    hours += 1;
    minutes -= 60;
  }

  const pluralHours = hours === 0 || hours > 1;
  const pluralMinutes = minutes === 0 || minutes > 1;

  if (minutes === 0) {
    return `${hours} hour${pluralHours ? "s" : ""}`;
  }

  if (hours === 0) {
    return `${minutes} minute${pluralMinutes ? "s" : ""}`;
  }

  return `${hours} hour${pluralHours ? "s" : ""} and ${minutes} minute${
    pluralMinutes ? "s" : ""
  }`;
}

export function Factory({ factory }) {
  const [debug, setDebug] = useState(false);
  const dispatch = useDispatch();
  const highlightedCityId = useGetHighlightedCity();
  const cities = useGetCities();
  const { id, name, storage, producer, cityId } = factory;
  const cityName = cities[cityId].name;

  const { time, clock } = useGameClock({ interval: 500 });
  const minutes = producer.time;
  const ms = minutes * 1000;

  let productionTimePassed =
    (time - producer.productionStartTime) / clock.multiplier;
  if (productionTimePassed >= ms) {
    productionTimePassed = ms;
  }

  const productionEndTime = new Date(
    producer.productionStartTime + ms * clock.multiplier
  );

  const cardStyle = Object.assign(
    {},
    highlightedCityId === cityId && { background: "#e0e0e0" }
  );

  return (
    <>
      <Header>
        <div>
          <Tag color={"blue"}>{name}</Tag>
          <Tag color={"gold"}>{cityName}</Tag>
          <Tag color={"gray"}>{id}</Tag>
        </div>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <Card
        onMouseEnter={() => dispatch(cityHighlighted(cityId))}
        onMouseLeave={() => dispatch(cityHighlighted(null))}
        bodyStyle={cardStyle}
      >
        <Storage storage={storage} />
        <Divider style={{ margin: "2px" }} />
        <div>
          <Producer>
            <div>
              <span>
                Production of 1 {producer.resource} takes{" "}
                {formatDuration(minutes)}
              </span>
            </div>
            <ProducerProgress>
              <ProductionDate>
                {getFormattedTime(new Date(producer.productionStartTime))}
              </ProductionDate>
              <Progress
                percent={(productionTimePassed / ms) * 100}
                showInfo={false}
                strokeColor={
                  productionTimePassed === ms ? "gray" : "green"
                }
              />
              <ProductionDate>
                {getFormattedTime(productionEndTime)}
              </ProductionDate>
            </ProducerProgress>
          </Producer>
        </div>
        {debug && <div>{JSON.stringify(factory, null, 2)}</div>}
      </Card>
    </>
  );
}
