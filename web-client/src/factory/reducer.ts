import { produce } from "immer";
import {
  FACTORY_ADDED,
  FACTORY_INIT,
  PRODUCTION_FINISHED,
  PRODUCTION_LINE_ADDED,
  PRODUCTION_STARTED,
  RESOURCE_BOUGHT,
  RESOURCE_PRICE_CHANGED,
  RESOURCE_SOLD,
  RESOURCE_STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED
} from "./actions";
import {
  FactoryAction,
  FactoryAddedAction,
  FactoryState,
  IFactory,
  ProductionFinishedAction,
  ResourceSoldAction,
  ResourcePriceChangedAction,
  ProductionLineAddedAction,
  IFactoryStorage,
  ProducerInput,
  ProducerOutput,
  ResourceBoughtAction
} from "./index";
import { StorageContentChangedAction } from "../storage";
import { ResourceName } from "../world";

const initialState: FactoryState = {
  factories: []
};

export function reducer(
  state: FactoryState = initialState,
  action: FactoryAction
): FactoryState {
  switch (action.type) {
    case FACTORY_INIT:
      return initialState;
    case FACTORY_ADDED:
      return factoryAdded(state, action);
    case STORAGE_CONTENT_CHANGED:
      return storageContentChanged(state, action);
    case RESOURCE_STORAGE_CAPACITY_CHANGED:
      return resourceStorageCapacityChanged(state, action);
    case PRODUCTION_FINISHED:
      return productionFinished(state, action);
    case PRODUCTION_STARTED:
      return productionStarted(state, action);
    case RESOURCE_SOLD:
      return resourceSold(state, action);
    case RESOURCE_BOUGHT:
      return resourceBought(state, action);
    case RESOURCE_PRICE_CHANGED:
      return resourcePriceChanged(state, action);
    case PRODUCTION_LINE_ADDED:
      return productionLineAdded(state, action);
    default:
      return state;
  }
}

const factoryAdded = produce(
  (state: FactoryState, action: FactoryAddedAction) => {
    const { factoryDTO } = action;
    state.factories.push(factoryDTO);
  }
);

const productionFinished = produce(
  (state: FactoryState, action: ProductionFinishedAction) => {
    const { entityId } = action;
    const factory = findFactory(state, entityId);
    if (!factory) {
      return state;
    }
    const { producer, storage } = factory;
    const { output } = producer;
    addOutput(storage, output);
    factory.producer.productionStartTime = -1;
  }
);

const productionStarted = produce((state, action) => {
  const { entityId, productionStartTime } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return state;
  }
  const { producer, storage } = factory;
  const { input } = producer;
  removeInput(storage, input);
  factory.producer.productionStartTime = productionStartTime;
});

const storageContentChanged = produce(
  (state: FactoryState, action: StorageContentChangedAction) => {
    const { entityId, resource, change } = action;
    const factory = findFactory(state, entityId);
    if (!factory) {
      return;
    }
    const { storage } = factory;
    const slot = storage[resource];
    slot!.count += change;
  }
);

const resourceStorageCapacityChanged = produce((state, action) => {
  const { entityId, capacityChanges } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const { storage } = factory;
  Object.entries(capacityChanges).forEach(entry => {
    const resource = entry[0] as ResourceName;
    const change = entry[1] as number;
    const slot = storage[resource] || {
      resource,
      capacity: 0,
      count: 0,
      price: 0
    };
    slot.capacity += change as number;
    if (slot.capacity < slot.count) {
      slot.count = slot.capacity;
    }
    storage[resource] = slot;
  });
});

const resourceSold = produce(
  (state: FactoryState, action: ResourceSoldAction) => {
    const { entityId, resource, count } = action;
    const factory = findFactory(state, entityId);
    if (!factory) {
      return;
    }
    const { storage } = factory;
    changeResourceCount(resource, -count, storage);
  }
);

const resourceBought = produce(
  (state: FactoryState, action: ResourceBoughtAction) => {
    const { entityId, resource, count } = action;
    const factory = findFactory(state, entityId);
    if (!factory) {
      return;
    }
    const { storage } = factory;
    changeResourceCount(resource, count, storage);
  }
);

const resourcePriceChanged = produce(
  (state: FactoryState, action: ResourcePriceChangedAction) => {
    const { prices, entityId } = action;
    const factory = findFactory(state, entityId);
    if (!factory) {
      return;
    }
    const { storage } = factory;
    Object.entries(prices).forEach(([resource, price]) => {
      storage[resource as ResourceName]!.price = price as number;
    });
  }
);

const productionLineAdded = produce(
  (state: FactoryState, action: ProductionLineAddedAction) => {
    const { entityId, input, output, time } = action;
    const factory = findFactory(state, entityId);
    if (!factory) {
      return;
    }
    const { producer } = factory;
    producer.input = input;
    producer.output = output;
    producer.time = time;
  }
);

function findFactory(
  state: FactoryState,
  factoryId: string
): IFactory | undefined {
  return state.factories.find(factory => factory.id === factoryId);
}

function removeInput(storage: IFactoryStorage, input: ProducerInput) {
  Object.keys(input).forEach(resource => {
    const count = input[resource as ResourceName] || 0;
    changeResourceCount(resource as ResourceName, -count, storage);
  });
}

function addOutput(storage: IFactoryStorage, output: ProducerOutput) {
  Object.keys(output).forEach(resource => {
    const count = output[resource as ResourceName] || 0;
    changeResourceCount(resource as ResourceName, count, storage);
  });
}

function changeResourceCount(
  resource: ResourceName,
  count: number,
  storage: IFactoryStorage
) {
  const slot = storage[resource];
  slot!.count += count;
}
