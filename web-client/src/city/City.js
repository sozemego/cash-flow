import React, { useState } from "react";
import styled from "styled-components";
import { useDispatch } from "react-redux";
import { cityHighlighted } from "./actions";

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
  border: 1px solid black;
`;

export function City({ city }) {
  const [debug, setDebug] = useState(false);
  const dispatch = useDispatch();

  const { id, name, factorySlots } = city;

  return (
    <Container
      onMouseEnter={() => dispatch(cityHighlighted(id))}
      onMouseLeave={() => dispatch(cityHighlighted(null))}
    >
      <Header>
        <Id>{id}</Id>
        <Debug onClick={() => setDebug(!debug)}>{debug ? "-" : "+"}</Debug>
      </Header>
      <div>{name}</div>
      <Divider />
      <div>Factory slots - {factorySlots}</div>
      {debug && <div>{JSON.stringify(city, null, 2)}</div>}
    </Container>
  );
}
