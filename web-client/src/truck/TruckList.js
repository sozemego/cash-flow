import React from "react";
import { useTruckSocket } from "./useTruckSocket";
import styled from "styled-components";
import { READY_STATE_TABLE } from "../websocket/hook";
import { Truck } from "./Truck";

const Container = styled.div`
  margin-left: 12px;
`;

const Header = styled.div`
  min-height: 50px;
`;

export function TruckList({ trucks }) {
  const { readyState } = useTruckSocket();

  return (
    <Container>
      <Header>Trucks - state [{READY_STATE_TABLE[readyState]}]</Header>
      <hr />
      {trucks.map(truck => (
        <Truck key={truck.id} truck={truck} />
      ))}
    </Container>
  );
}
