import React, { useState } from "react";
import styled from "styled-components";
import { useDispatch } from "react-redux";
import { cityHighlighted } from "./actions";
import Card from "antd/lib/card";
import { Avatar } from "antd";
import Tag from "antd/lib/tag";
import Icon from "antd/lib/icon";

const Container = styled.div`
  margin: 2px;
  padding: 12px;
  border: dotted gray 1px;
`;

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Id = styled.div`
  color: gray;
  font-size: 0.75rem;
`;

const Divider = styled.div`
  width: 25%;
  opacity: 0.25;
  margin-left: 0;
`;

const Debug = styled.div`
  cursor: pointer;
  max-height: 24px;
`;

export function City({ city }) {
  const [debug, setDebug] = useState(false);
  const dispatch = useDispatch();

  const { id, name, factorySlots } = city;

  return (
    <>
      <Header>
        <Tag color={"gold"}>{name}</Tag>
        <Debug onClick={() => setDebug(!debug)}>
          {debug ? <Icon type="question" /> : <Icon type="question-circle" />}
        </Debug>
      </Header>
      <Card
        onMouseEnter={() => dispatch(cityHighlighted(id))}
        onMouseLeave={() => dispatch(cityHighlighted(null))}
      >
        <div>Factory slots - {factorySlots}</div>
        {debug && <div>{JSON.stringify(city, null, 2)}</div>}
      </Card>
    </>
  );
}
