import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { ProgressBar } from "../components/progressBar/ProgressBar";
import { useClock } from "../hooks/clock";

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

const Divider = styled.div`
  width: 25%;
  opacity: 0.25;
  margin-left: 0;
`;

const Producer = styled.div`
  display: flex;
  flex-direction: row;
`;

const Debug = styled.div`
  cursor: pointer;
  border: 1px solid black;
`;

export function Factory({ factory }) {
  const [debug, setDebug] = useState(false);

  const { id, name, storage, producer } = factory;

  const { time } = useClock({ interval: 500 });

  let productionTimePassed = time - producer.productionStartTime;
  if (productionTimePassed >= producer.time) {
    productionTimePassed = producer.time;
  }

  return (
    <Container>
      <Header>
        <Id>{id}</Id>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <div>{name}</div>
      <Divider />
      <div>
        Storage - [{capacityTaken(storage)} / {storage.capacity}]
      </div>
      <div>
        <Producer>
          <div>
            Producer [{producer.resource}] - {productionTimePassed} /{" "}
            {producer.time}
          </div>
          <ProgressBar current={productionTimePassed} time={producer.time} />
        </Producer>
      </div>
      {debug && <div>{JSON.stringify(factory, null, 2)}</div>}
    </Container>
  );
}
