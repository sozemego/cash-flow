import React from "react";
import styled from "styled-components";
import { useDispatch } from "react-redux";
import { cityHighlighted } from "./actions";
import Card from "antd/lib/card";
import Tag from "antd/lib/tag";
import Icon from "antd/lib/icon";
import { Tooltip } from "antd";
import { Debug } from "../components/Debug";
import { useGetFactories } from "../factory/selectors";
import { combine } from "../storage/business";
import { ResourceList } from "../components/Storage";

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

export function City({ city }) {
  const dispatch = useDispatch();

  const { id, name, factorySlots } = city;
  const factories = useGetFactories();
  const factoriesInThisCity = factories.filter(factory => factory.cityId === id);
  const totalCityStorage = combine(factoriesInThisCity.map(factory => factory.storage));

  return (
    <>
      <Header>
        <Tag color={"gold"}>{name}</Tag>
        <Tooltip
          title={<Debug obj={city} />}
        >
          <Icon type={"question-circle"} />
        </Tooltip>
      </Header>
      <Card
        onMouseEnter={() => dispatch(cityHighlighted(id))}
        onMouseLeave={() => dispatch(cityHighlighted(null))}
      >
        <div>Factory slots - {factorySlots}</div>
				<ResourceList resources={totalCityStorage.resources}/>
      </Card>
    </>
  );
}
