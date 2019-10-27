import React from "react";
import { Icon } from "antd";
import styled from "styled-components";
import { Transition } from "react-spring/renderprops-universal";

import { GameEventLevelProps, GameEventProps, Level } from "./index.d";
import { getFormattedDateTime } from "../clock/business";

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
    <Transition
      items={event}
      config={{ duration: 500 }}
      from={{ opacity: 0, transform: "translate(50px, 0)" }}
      enter={{ opacity: 1, transform: "translate(0, 0)" }}
      leave={{ opacity: 0, transform: "translate(-50px, 0)" }}
    >
      {item => props => (
        <div style={props}>
          <Container>
            <Info>
              <GameEventLevel level={item.level} />
              <Timestamp>
                {getFormattedDateTime(new Date(item.timestamp))}
              </Timestamp>
            </Info>
            <div>{item.text}</div>
          </Container>
        </div>
      )}
    </Transition>
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
