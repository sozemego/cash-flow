import { ResourceName } from "../world/index.d";

export interface IStorage {
    capacity: number;
    resources: ResourceCounts
}

export type ResourceCounts = {
    [resource in ResourceName]: number;
};