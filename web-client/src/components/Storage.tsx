import React from "react";
import { ResourceIcon } from "./ResourceIcon";
import Icon from "antd/lib/icon";
import Tag from "antd/lib/tag";
import { ResourceMap } from "../world/reducer";
import { Resources } from "../factory/index.d";

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
  return (
    <div>
      <div>
        <Icon type="database" /> Capacity {capacityTaken} / {storage.capacity}
      </div>
      <div>
        <ResourceList resources={storage.resources} />
      </div>
    </div>
  );
}

interface ResourceCount {
  resource: string;
  count: number;
}

export function ResourceList(props) {
  const resources: Resources = props.resources;
  const resourceCounts: ResourceCount[] = Object.entries(resources).map(
    ([resource, count]) => {
      return { resource, count };
    }
  );

  return (
    <>
      {resourceCounts.map(({ resource, count }) => (
        <Tag color={"purple"} key={resource}>
          <span>{count}</span>
          <ResourceIcon resource={resource} />
        </Tag>
      ))}
    </>
  );
}
