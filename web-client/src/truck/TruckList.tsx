import React from "react";
import { useTruckSocket } from "./useTruckSocket";
import styled from "styled-components";
import { READY_STATE_TABLE } from "../websocket/hook";
import { Truck } from "./Truck";
import { Typography, Divider } from "antd";

const Container = styled.div`
  margin-left: 12px;
`;

export function TruckList({ trucks }) {
  const { readyState } = useTruckSocket();

  return (
    <Container>
      <Typography>Trucks - state [{READY_STATE_TABLE[readyState]}]</Typography>
      <Divider />
      {trucks.map(truck => (
        <Truck key={truck.id} truck={truck} />
      ))}
    </Container>
  );
}
