import React, { useState } from "react";
import styled from "styled-components";
import { ProgressBar } from "../components/progressBar/ProgressBar";
import { CityInline } from "../city/CityInline";
import { getFormattedTime } from "../clock/business";
import { useGameClock } from "../clock/gameClock";

function capacityTaken(storage) {
  const { resources } = storage;
  let capacityTaken = 0;
  Object.values(resources).forEach(taken => {
    capacityTaken += taken;
  });
  return capacityTaken;
}

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
  margin-left: -12px;
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

  return `${hours} hour${pluralHours ? "s" : ""} and ${minutes} minute${
    pluralMinutes ? "s" : ""
  }`;
}

export function Factory({ factory }) {
  const [debug, setDebug] = useState(false);

  const { id, name, storage, producer, cityId } = factory;

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

  return (
    <Container>
      <Header>
        <Id>{id}</Id>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <div>
        {name} at <CityInline cityId={cityId} />
      </div>
      <Divider />
      <div>
        Storage - [{capacityTaken(storage)} / {storage.capacity}]
      </div>
      <div>
        <Divider />
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
            <ProgressBar current={productionTimePassed} time={ms} height={6} />
            <ProductionDate>
              {getFormattedTime(productionEndTime)}
            </ProductionDate>
          </ProducerProgress>
        </Producer>
      </div>
      {debug && <div>{JSON.stringify(factory, null, 2)}</div>}
    </Container>
  );
}
