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
  const resourceCounts = Object.entries(storage.resources).map(
    ([resource, count]) => {
      return { resource, count };
    }
  );

  const capacityTaken = calculateCapacityTaken(storage);

  return (
    <div>
      <div>
        <Icon type="database" /> Capacity {capacityTaken} / {storage.capacity}
      </div>
      <div>
        {resourceCounts.map(({ resource, count }) => (
          <Tag color={"purple"} key={resource}>
            <span>{count}</span>
            <ResourceIcon resource={resource} />
          </Tag>
        ))}
      </div>
    </div>
  );
}
