import React from "react";
import { useTruckSocket } from "./useTruckSocket";
import styled from "styled-components";
import { READY_STATE_TABLE } from "../websocket/hook";
import { Truck } from "./Truck";

const Container = styled.div`
  margin-left: 12px;
`;

export function TruckList({ trucks }) {
  const { readyState } = useTruckSocket();

  return (
    <Container>
      <div>Trucks - state [{READY_STATE_TABLE[readyState]}]</div>
      <hr />
      {trucks.map(truck => (
        <Truck key={truck.id} truck={truck} />
      ))}
    </Container>
  );
}
