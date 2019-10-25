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
import { ResourceList } from "../storage/Storage";
import { IFactoryStorage, IStorageSlot } from "../factory";
import { combine } from "../factory/FactoryStorage";
import { CityProps, ResourceName } from "./index";
import { ResourceCount } from "../storage";

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

export function City({ city }: CityProps) {
  const dispatch = useDispatch();

  const { id, name, factorySlots } = city;
  const factories = useGetFactories();
  const factoriesInThisCity = factories.filter(
    factory => factory.cityId === id
  );
  const totalCityStorage: IFactoryStorage = combine(
    factoriesInThisCity.map(factory => factory.storage)
  );

  const resourceCounts: ResourceCount[] = Object.entries(totalCityStorage).map(
    entry => {
      const resource = entry[0] as ResourceName;
      const slot = entry[1] as IStorageSlot;
      return { resource, count: slot!.count };
    }
  );

  return (
    <>
      <Header>
        <Tag color={"gold"}>{name}</Tag>
        <Tooltip title={<Debug obj={city} />}>
          <Icon type={"question-circle"} />
        </Tooltip>
      </Header>
      <Card
        onMouseEnter={() => dispatch(cityHighlighted(id))}
        onMouseLeave={() => dispatch(cityHighlighted(null))}
      >
        <div>Factory slots - {factorySlots}</div>
        <ResourceList resources={resourceCounts} />
      </Card>
    </>
  );
}
