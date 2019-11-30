import React from "react";
import styled from "styled-components";
import { Tag } from "antd";

const Container = styled.div`
  display: grid;
  grid-template-columns: 180px 125px 75px auto;
`;

const Timestamp = styled.div`
  margin: 2px;
  margin-right: 4px;
  flex-grow: 1;
  overflow: hidden;
`;

const Level = styled.div`
  overflow: hidden;
`;

const Application = styled.div`
  margin: 2px;
  margin-right: 4px;
  overflow: hidden;
`;

const Message = styled.div`
  overflow: hidden;
`;

export function Log({ log }) {
  const { timestamp, application, level, message } = log;
  return (
    <Container>
      <Timestamp>{getDate(timestamp)}</Timestamp>
      <Application>{application}</Application>
      <Level>
        <Tag color={getColor(level)} style={{ maxHeight: "26px" }}>
          {level}
        </Tag>
      </Level>
      <Message>{message}</Message>
    </Container>
  );
}

function getColor(level) {
  return {
    info: "blue",
    warn: "magenta",
    error: "red",
    trace: "gray"
  }[level.toLowerCase()];
}

function getDate(timestamp) {
  return new Date(timestamp).toISOString();
}
