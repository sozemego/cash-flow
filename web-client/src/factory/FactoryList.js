import React from "react";
import "./factory.css";
import { Factory } from "./Factory";
import { useFactorySocket } from "./factoryService";

export function FactoryList({ factories }) {
  const { socket, state } = useFactorySocket();

  return (
    <div className={"factory-list-container"}>
      <div>Factories - state [{state}]</div>
      <hr />
      {factories.map(factory => (
        <Factory key={factory.id} factory={factory} />
      ))}
    </div>
  );
}
