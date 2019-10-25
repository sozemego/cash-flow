import { IStorage } from "./index.d";
import { ResourceName } from "../world";
import { STORAGE_CONTENT_CHANGED } from "../truck/actions";

export interface ResourceCount {
    resource: ResourceName;
    count: number;
}

export interface StorageProps {
    storage: IStorage;
}

export interface ResourceListProps {
    resources: ResourceCount[]
}

export interface IStorage {
    capacity: number;
    resources: ResourceCounts
}

export type ResourceCounts = {
    [resource in ResourceName]: number;
};

export interface StorageContentChangedAction {
    type: typeof STORAGE_CONTENT_CHANGED;
    entityId: string;
    resource: ResourceName;
    change: number;
}
