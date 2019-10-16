import React from "react";
import { IFactoryStorage } from "./index.d";
import {ResourceIcon} from "../components/ResourceIcon";

export interface FactoryStorageProps {
  storage: IFactoryStorage;
}

export function FactoryStorage({ storage }: FactoryStorageProps) {
  const { capacities, resources } = storage;

  return (
    <>
      {Object.entries(capacities).map(([resource, capacity]) => {
          const capacityTaken = resources[resource] || 0;
          return (
              <div>
                  <ResourceIcon resource={resource}/>
                  {capacityTaken} / {capacity}
              </div>
          )
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
  const totalStorage = { capacities: {}, resources: {} };
  for (let storage of storages) {
    const totalCapacities = totalStorage.capacities;
    const capacities = storage.capacities;
    Object.entries(capacities).forEach(([resource, capacity]) => {
      const totalCapacity = totalCapacities[resource] || 0;
      totalCapacities[resource] = totalCapacity + (capacity as number);
    });
    transfer(storage, totalStorage);
  }
  return totalStorage;
}
