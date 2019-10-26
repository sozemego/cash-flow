import React from "react";
import { Icon } from "antd";
import styled from "styled-components";
import Divider from "antd/lib/divider";
import { GameEventLevelProps, GameEventProps, Level } from "./index.d";
import { getFormattedTime } from "../clock/business";

const Container = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-start;
  margin: 4px;
  padding: 4px;
`;

const Info = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
`;

const Timestamp = styled.div`
  margin-right: 4px;
`;

export function GameEvent({ event }: GameEventProps) {
  return (
    <>
      <Container>
        <Info>
          <GameEventLevel level={event.level} />
          <Timestamp>{getFormattedTime(new Date(event.timestamp))}</Timestamp>
        </Info>
        <div>{event.text}</div>
      </Container>
    </>
  );
}

export function GameEventLevel({ level }: GameEventLevelProps) {
  const levels = {
    [Level.INFO]: "info-circle",
    [Level.WARNING]: "exclamation-circle",
    [Level.CRITICAL]: "stop"
  };
  return <Icon type={levels[level]} style={{ marginRight: "6px" }} />;
}
