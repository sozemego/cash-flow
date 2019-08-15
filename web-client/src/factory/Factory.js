import React, { useState, useEffect } from "react";
import "./factory.css";
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

export function Factory({ factory }) {
  const [debug, setDebug] = useState(false);

  const { id, name, storage, producer } = factory;

  const { time } = useClock({ interval: 500 });

  let productionTimePassed = time - producer.productionStartTime;
  if (productionTimePassed >= producer.time) {
    productionTimePassed = producer.time;
  }

  return (
    <div
      className={"factory-container"}
      onMouseEnter={() => setDebug(true)}
      onMouseLeave={() => setDebug(false)}
    >
      <div className={"factory-id-container"}>{id}</div>
      <div>{name}</div>
      <hr className={"factory-name-divider"} />
      <div>
        Storage - [{capacityTaken(storage)} / {storage.capacity}]
      </div>
      <div>
        <div className={"factory-producer-container"}>
          <div>
            Producer [{producer.resource}] - {productionTimePassed} /{" "}
            {producer.time}
          </div>
          <ProgressBar current={productionTimePassed} time={producer.time} />
        </div>
      </div>
      {debug && <div>{JSON.stringify(factory, null, 2)}</div>}
    </div>
  );
}
