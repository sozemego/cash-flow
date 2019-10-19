import React from "react";
import { IFactoryStorage, IStorageSlot, IStorageSlotEntry } from "./index.d";
import { ResourceIcon } from "../components/ResourceIcon";
import Tag from "antd/lib/tag";
import styled from "styled-components";
import { ResourceName } from "../world/index.d";

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
 * Transfers all resources from oldStorage to newStorage.
 * Drops any resources that will not fit in the newStorage.
 */
export function transfer(
  oldStorage: IFactoryStorage,
  newStorage: IFactoryStorage
) {
  Object.entries(oldStorage).forEach(([resource, slot]) => {
    addSlot(newStorage, slot as IStorageSlot);
  });
}

export function getRemainingCapacity(
  storage: IFactoryStorage,
  resource: ResourceName
): number {
  const remainingCapacity =
    getCapacity(storage, resource) - getCapacityTaken(storage, resource);
  if (isNaN(remainingCapacity)) {
    throw new Error("Remaining capacity cannot be NaN");
  }
  return remainingCapacity;
}

export function getCapacity(
  storage: IFactoryStorage,
  resource: ResourceName
): number {
  const slot = storage[resource];
  if (!slot) {
    return 0;
  }
  return slot.capacity;
}

export function getCapacityTaken(
  storage: IFactoryStorage,
  resource: string
): number {
  const slot = storage[resource];
  if (!slot) {
    return 0;
  }
  return slot.count;
}

export function addSlot(storage: IFactoryStorage, slot: IStorageSlot) {
  const currentSlot = storage[slot.resource];
  if (!currentSlot) {
    storage[slot.resource] = slot;
  } else {
    currentSlot.capacity += slot.capacity;
    currentSlot.count += slot.count;
  }
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
