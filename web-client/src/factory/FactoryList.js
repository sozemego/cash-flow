import React from "react";
import "./factory.css";
import { Factory } from "./Factory";
import { useFactorySocket } from "./factoryService";
import {READY_STATE_TABLE} from "../websocket/hook";

export function FactoryList({ factories }) {
  const { socket, readyState } = useFactorySocket();

  return (
    <div className={"factory-list-container"}>
      <div>Factories - state [{READY_STATE_TABLE[readyState]}]</div>
      <hr />
      {factories.map(factory => (
        <Factory key={factory.id} factory={factory} />
      ))}
    </div>
  );
}
