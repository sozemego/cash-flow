import React, { useState } from "react";
import styled, { css } from "styled-components";
import { Factory } from "./Factory";
import { useFactorySocket } from "./useFactorySocket";
import { READY_STATE_TABLE } from "../websocket/hook";
import { useGetCities } from "../world/selectors";
import { Divider } from "antd";
import Tag from "antd/lib/tag";
import { FactoryByCityProps, GroupButtonProps, IFactory } from "./index";
import { FLAGS } from "../featureFlags";
import { useGetSelectedCityId } from "../game/selectors";
import { CityInline } from "../world/CityInline";

const Container = styled.div`
  margin-left: 12px;
`;

const Header = styled.div`
  min-height: 50px;
`;

const FactoryListContainer = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
`;

const GroupButton = styled.button<GroupButtonProps>`
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

export interface FactoryGroupProps {
  factories: IFactory[];
}

export function FactoryGroup({ factories }: FactoryGroupProps) {
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

export interface FactoryListProps {
  factories: IFactory[];
}

export function FactoryList({ factories }: FactoryListProps) {
  const gameOnMap = FLAGS.GAME_ON_MAP;
  const selectedCityId = useGetSelectedCityId();

  return (
    <FactoryListContainer>
      {gameOnMap && <div style={{marginLeft: "12px"}}>
        Factories in <CityInline cityId={selectedCityId}/>
      </div>}
      {factories.map(factory => (
        <Factory key={factory.id} factory={factory} />
      ))}
    </FactoryListContainer>
  );
}

export interface FactoryByTypeProps {
  factories: IFactory[];
}

interface FactoryByProp {
  [prop: string]: IFactory[];
}

export function FactoryByType({ factories }: FactoryByTypeProps) {
  const factoryByName: FactoryByProp = {};
  factories.forEach(factory => {
    const factories = factoryByName[factory.name] || [];
    factories.push(factory);
    factoryByName[factory.name] = factories;
  });

  const names = [...new Set(factories.map(factory => factory.name))];

  return (
    <>
      {names.map(name => (
        <FactoryList key={name} factories={factoryByName[name]} />
      ))}
    </>
  );
}

const FactoryByCityContainer = styled.div`
  padding-left: 2px;
`;

export function FactoryByCity({ factories, cities }: FactoryByCityProps) {
  const factoryByCity: FactoryByProp = {};
  factories.forEach((factory: IFactory) => {
    const factories = factoryByCity[factory.cityId] || [];
    factories.push(factory);
    factoryByCity[factory.cityId] = factories;
  });

  return (
    <>
      {cities.map(city => {
        const factories = factoryByCity[city.id] || [];
        if (factories.length === 0) {
          return null;
        }
        return (
          <FactoryByCityContainer key={city.id}>
            <Tag color={"orange"}>
              {city.name} [{factories.length}]
            </Tag>
            <FactoryList factories={factories} />
          </FactoryByCityContainer>
        );
      })}
    </>
  );
}
