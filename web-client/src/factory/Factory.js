import React, { useState } from "react";
import "./factory.css";

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
        Producer [{producer.resource}] - {producer.progress} / {producer.time}
      </div>
      {debug && <div>{JSON.stringify(factory, null, 2)}</div>}
    </div>
  );
}
