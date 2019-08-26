import React, { useState } from "react";
import styled, { css } from "styled-components";
import { Factory } from "./Factory";
import { useFactorySocket } from "./useFactorySocket";
import { READY_STATE_TABLE } from "../websocket/hook";
import { useGetCities } from "../city/selectors";

const Container = styled.div`
  margin-left: 12px;
`;

const GroupButton = styled.button`
  display: inline-block;
  margin-left: 4px;
  margin-right: 4px;
  ${props =>
    props.selected &&
    css`
      color: green;
      font-size: 1.2rem;
    `}
`;

const GROUP_BY = {
  CITY: "CITY",
  DEFAULT: "DEFAULT",
  TYPE: "TYPE"
};

export function FactoryGroup({ factories }) {
  const [groupBy, setGroupBy] = useState(GROUP_BY.CITY);
  const { readyState } = useFactorySocket();
  const cities = useGetCities();

  return (
    <Container>
      <div>Factories - state [{READY_STATE_TABLE[readyState]}]</div>
      <div>
        Group by:
        {Object.values(GROUP_BY).map(val => (
          <GroupButton
            onClick={() => setGroupBy(val)}
            selected={val === groupBy}
          >
            {val.toUpperCase()}
          </GroupButton>
        ))}
      </div>
      <hr />
      {groupBy === GROUP_BY.DEFAULT && <FactoryList factories={factories} />}
      {groupBy === GROUP_BY.TYPE && <FactoryByType factories={factories} />}
      {groupBy === GROUP_BY.CITY && (
        <FactoryByCity factories={factories} cities={Object.values(cities)} />
      )}
    </Container>
  );
}

export function FactoryList({ factories }) {
  return factories.map(factory => (
    <Factory key={factory.id} factory={factory} />
  ));
}

export function FactoryByType({ factories }) {
  const factoryByType = {};
  factories.forEach(factory => {
    const factories = factoryByType[factory.templateId] || [];
    factories.push(factory);
    factoryByType[factory.templateId] = factories;
  });

  const types = [...new Set(factories.map(factory => factory.templateId))];

  return types.map(type => (
    <div key={type}>
      <div>{type}</div>
      <hr style={{ width: "25%", margin: "auto" }} />
      <FactoryList factories={factoryByType[type]} />
    </div>
  ));
}

const FactoryByCityContainer = styled.div`
  padding-left: 2px;
  ${props =>
    css`
      border-left: 2px solid ${props.even ? "#D3D3D3" : "#9e9e9e"};
    `}}
`;

export function FactoryByCity({ factories, cities }) {
  const factoryByCity = {};
  factories.forEach(factory => {
    const factories = factoryByCity[factory.cityId] || [];
    factories.push(factory);
    factoryByCity[factory.cityId] = factories;
  });

  return cities.map((city, index) => {
    const factories = factoryByCity[city.id] || [];
    if (factories.length === 0) {
      return null;
    }
    const even = index % 2 === 0;
    return (
      <FactoryByCityContainer key={city.id} even={even}>
        <div>
          {city.name} [{factories.length}]
        </div>
        <FactoryList factories={factories} />
      </FactoryByCityContainer>
    );
  });
}
