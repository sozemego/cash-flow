import React, { useState } from "react";
import styled, { css } from "styled-components";
import { Factory } from "./Factory";
import { useFactorySocket } from "./useFactorySocket";
import { READY_STATE_TABLE } from "../websocket/hook";
import { useGetCities } from "../city/selectors";
import { Divider } from "antd";
import Tag from "antd/lib/tag";

const Container = styled.div`
  margin-left: 12px;
`;

const Header = styled.div`
  min-height: 50px;
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
      <Header>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignContent: "center"
          }}
        >
          <div style={{ alignSelf: "center" }}>
            Factories - state [{READY_STATE_TABLE[readyState]}]
          </div>
          <span>
            Group by:
            {Object.values(GROUP_BY).map(val => (
              <GroupButton
                onClick={() => setGroupBy(val)}
                selected={val === groupBy}
                key={val}
              >
                {val.toUpperCase()}
              </GroupButton>
            ))}
          </span>
        </div>
        <Divider style={{ margin: "10px" }} />
      </Header>
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
  const factoryByName = {};
  factories.forEach(factory => {
    const factories = factoryByName[factory.name] || [];
    factories.push(factory);
    factoryByName[factory.name] = factories;
  });

  const names = [...new Set(factories.map(factory => factory.name))];

  return names.map(name => (
    <FactoryList key={name} factories={factoryByName[name]} />
  ));
}

const FactoryByCityContainer = styled.div`
  padding-left: 2px;
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
        <Tag color={"orange"}>
          {city.name} [{factories.length}]
        </Tag>
        <FactoryList factories={factories} />
      </FactoryByCityContainer>
    );
  });
}
