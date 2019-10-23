/**
 * Transfers all resources from oldStorage to newStorage.
 * Drops any resources that will not fit in the newStorage.
 */
export function transfer(oldStorage, newStorage) {
  const resources = oldStorage.resources;
  Object.entries(resources).forEach(([resource, count]) => {
    addResource(newStorage, resource, count as number);
  });
}

export function getRemainingCapacity(storage) {
  const remainingCapacity = storage.capacity - getCapacityTaken(storage);
  if (isNaN(remainingCapacity)) {
    throw new Error("Remaining capacity cannot be NaN");
  }
  return remainingCapacity;
}

export function getCapacityTaken(storage) {
  const { resources } = storage;
  let capacityTaken = 0;
  Object.values(resources).forEach((count: any) => (capacityTaken += count));
  return capacityTaken;
}

export function addResource(storage, resource, count = 1) {
  const remainingCapacity = getRemainingCapacity(storage);
  const countToAdd = Math.min(remainingCapacity, count);
  const previousCount = storage.resources[resource] || 0;
  storage.resources[resource] = previousCount + countToAdd;
}
