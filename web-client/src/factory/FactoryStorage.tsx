import React from "react";
import { IFactoryStorage, IStorageSlot } from "./index";
import { ResourceIcon } from "../components/ResourceIcon";
import Tag from "antd/lib/tag";
import styled from "styled-components";

export interface FactoryStorageProps {
  storage: IFactoryStorage;
}

const ResourceContainer = styled.div`
  display: flex;
  align-items: center;
`;

export function FactoryStorage({ storage }: FactoryStorageProps) {
  return (
    <>
      {Object.entries(storage).map(([resource, slot]) => {
        const { count, capacity, price } = slot as IStorageSlot;
        return (
          <ResourceContainer key={resource}>
            <Tag color={"magenta"}>${price}</Tag>
            <ResourceIcon resource={resource} />
            {count} / {capacity}
          </ResourceContainer>
        );
      })}
    </>
  );
}

/**
 * Combines all storages into one with summed capacities and resources.
 * @param storages
 */
export function combine(storages: IFactoryStorage[]): IFactoryStorage {
  const totalStorage: IFactoryStorage = {};
  for (let storage of storages) {
    Object.values(storage).forEach(slot => {
      slot = slot as IStorageSlot; //TODO come back here for this garbage
      const { resource, capacity, count } = slot;
      const totalSlot: IStorageSlot = totalStorage[resource] || {
        resource,
        count: 0,
        capacity: 0,
        price: 0
      };
      totalSlot.count += count;
      totalSlot.capacity += capacity;
      totalStorage[resource] = totalSlot;
    });
  }
  return totalStorage;
}
