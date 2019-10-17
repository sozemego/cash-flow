import React from "react";
import { ResourceIcon } from "./ResourceIcon";
import Icon from "antd/lib/icon";
import Tag from "antd/lib/tag";
import { ResourceMap } from "../world/reducer";

export function calculateCapacityTaken(storage) {
  const { resources }: ResourceMap = storage;
  let capacityTaken = 0;
  Object.values(resources).forEach((taken: number) => {
    capacityTaken += taken;
  });
  return capacityTaken;
}

export function Storage({ storage }) {
  const capacityTaken = calculateCapacityTaken(storage);
  const resourceCounts = Object.entries(storage.resources).map(
    ([resource, count]) => {
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

export interface ResourceCount {
  resource: string;
  count: number;
}

export function ResourceList(props) {
  const resources: ResourceCount[] = props.resources;

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
