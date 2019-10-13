import React from "react";
import { ResourceIcon } from "./ResourceIcon";
import Icon from "antd/lib/icon";
import Tag from "antd/lib/tag";

export function calculateCapacityTaken(storage) {
  const { resources } = storage;
  let capacityTaken = 0;
  Object.values(resources).forEach(taken => {
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

export function ResourceList({ resources }) {
  const resourceCounts = Object.entries(resources).map(([resource, count]) => {
    return { resource, count };
  });

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
