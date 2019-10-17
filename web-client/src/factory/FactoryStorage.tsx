import React from "react";
import { IFactoryStorage, IStorageSlotEntry } from "./index.d";
import { ResourceIcon } from "../components/ResourceIcon";

export interface FactoryStorageProps {
  storage: IFactoryStorage;
}

export function FactoryStorage({ storage }: FactoryStorageProps) {
  return (
    <>
      {Object.entries(storage).map(([resource, slot]: IStorageSlotEntry) => {
        const { count, capacity } = slot;
        return (
          <div key={resource}>
            <ResourceIcon resource={resource} />
            {count} / {capacity}
          </div>
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
  const resources = oldStorage.resources;
  Object.entries(resources).forEach(([resource, count]) => {
    addResource(newStorage, resource, count);
  });
}

export function getRemainingCapacity(
  storage: IFactoryStorage,
  resource: string
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
  resource: string
): number {
  const { capacities } = storage;
  return capacities[resource] || 0;
}

export function getCapacityTaken(
  storage: IFactoryStorage,
  resource: string
): number {
  const { resources } = storage;
  return resources[resource] || 0;
}

export function addResource(storage, resource, count = 1) {
  const remainingCapacity = getRemainingCapacity(storage, resource);
  const countToAdd = Math.min(remainingCapacity, count);
  const previousCount = storage.resources[resource] || 0;
  storage.resources[resource] = previousCount + countToAdd;
}

/**
 * Combines all storages into one with summed capacities and resources.
 * @param storages
 */
export function combine(storages: IFactoryStorage[]): IFactoryStorage {
  const totalStorage: IFactoryStorage = {};
  for (let storage of storages) {
    Object.entries(storage).forEach(([resource, slot]: IStorageSlotEntry) => {
      const totalSlot = totalStorage[resource] || {
        count: 0,
        capacity: 0,
        price: 0
      };
      totalSlot.count += slot.count;
      totalSlot.capacity += slot.capacity;
      totalStorage[resource] = totalSlot;
    });
  }
  return totalStorage;
}
