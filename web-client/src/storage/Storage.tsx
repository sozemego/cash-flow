import React from "react";
import { ResourceIcon } from "../components/ResourceIcon";
import Icon from "antd/lib/icon";
import Tag from "antd/lib/tag";
import {
  IStorage,
  ResourceCount,
  ResourceListProps,
  StorageProps
} from "./index";
import { ResourceName } from "../world";

export function Storage({ storage }: StorageProps) {
  const capacityTaken = calculateCapacityTaken(storage);
  const resourceCounts: ResourceCount[] = Object.entries(storage.resources).map(
    entry => {
      const resource = entry[0] as ResourceName;
      const count = entry[1] as number;
      return { resource, count };
    }
  );
  return (
    <div>
      <div>
        <Icon type="database" /> Capacity {capacityTaken} / {storage.capacity}
      </div>
      <div>
        <ResourceList resources={resourceCounts} />
      </div>
    </div>
  );
}

export function ResourceList({ resources }: ResourceListProps) {
  return (
    <>
      {resources.map(({ resource, count }) => (
        <Tag color={"purple"} key={resource}>
          <span>{count}</span>
          <ResourceIcon resource={resource} />
        </Tag>
      ))}
    </>
  );
}

export function calculateCapacityTaken(storage: IStorage) {
  const { resources } = storage;
  let capacityTaken = 0;
  Object.values(resources).forEach((taken: number) => {
    capacityTaken += taken;
  });
  return capacityTaken;
}
