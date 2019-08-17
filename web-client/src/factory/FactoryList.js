import React from "react";
import styled from "styled-components";
import { Factory } from "./Factory";
import { useFactorySocket } from "./useFactorySocket";
import { READY_STATE_TABLE } from "../websocket/hook";

const Container = styled.div`
  margin-left: 12px;
`;

export function FactoryList({ factories }) {
  const { readyState } = useFactorySocket();

  return (
    <Container>
      <div>Factories - state [{READY_STATE_TABLE[readyState]}]</div>
      <hr />
      {factories.map(factory => (
        <Factory key={factory.id} factory={factory} />
      ))}
    </Container>
  );
}
