import React, { useState } from "react";
import styled from "styled-components";
import { ProgressBar } from "../components/progressBar/ProgressBar";
import { useClock } from "../hooks/clock";
import { CityInline } from "../city/CityInline";
import { getFormattedDate, getFormattedTime } from "../clock/business";

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

const Debug = styled.div`
  cursor: pointer;
  border: 1px solid black;
`;

export function Factory({ factory }) {
  const [debug, setDebug] = useState(false);

  const { id, name, storage, producer, cityId } = factory;

  const { time } = useClock({ interval: 500 });
  const minutes = producer.time;
  const ms = minutes * 1000;

  let productionTimePassed = time - producer.productionStartTime;
  if (productionTimePassed >= ms) {
    productionTimePassed = ms;
  }
  if (producer.productionStartTime === -1) {
    productionTimePassed = 0;
  }

  const productionEndTime = new Date(
    producer.productionStartTime + productionTimePassed
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
            <span> Producer [{producer.resource}] </span>
            <span>
              {(productionTimePassed / 1000).toFixed(0)} / {minutes}
            </span>
          </div>
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center"
            }}
          >
            <div style={{ margin: 4 }}>
              {getFormattedTime(new Date(producer.productionStartTime))}
            </div>
            <ProgressBar current={productionTimePassed} time={ms} />
            <div style={{ margin: 4 }}>
              {getFormattedTime(productionEndTime)}
            </div>
          </div>
          {/*<div style={{width: "100%", margin: "auto"}}>*/}
          {/*  <ProgressBar current={productionTimePassed} time={ms} />*/}
          {/*</div>*/}
        </Producer>
      </div>
      {debug && <div>{JSON.stringify(factory, null, 2)}</div>}
    </Container>
  );
}
