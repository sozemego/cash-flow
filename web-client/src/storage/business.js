/**
 * Transfers all resources from oldStorage to newStorage.
 * Drops any resources that will not fit in the newStorage.
 * @param oldStorage
 * @param newStorage
 */
export function transfer(oldStorage, newStorage) {
	if (oldStorage.capacity <= newStorage.capacity) {
		newStorage.resources = {...oldStorage.resources};
		return;
	}
	const resources = oldStorage.resources;
	Object.entries(resources).forEach(([resource, count]) => {
		newStorage.resources[resource] = Math.min(count, getRemainingCapacity(newStorage));
	});
}

export function getRemainingCapacity(storage) {
	return storage.capacity - getCapacityTaken(storage);
}

export function getCapacityTaken(storage) {
	const resources = storage;
	let capacityTaken = 0;
	Object.values(resources).forEach(count => capacityTaken += count);
	return capacityTaken;
}

