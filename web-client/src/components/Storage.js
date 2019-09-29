import React from "react";
import styled from "styled-components";
import { ResourceIcon } from "./ResourceIcon";
import Icon from "antd/lib/icon";

const ResourcesContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const Resource = styled.div`
  border: 1px dotted gray;
  display: flex;
  align-items: center;
`;

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
      <ResourcesContainer>
        {resourceCounts.map(({ resource, count }) => (
          <Resource key={resource}>
            <ResourceIcon resource={resource} />
            <span>{count}</span>
          </Resource>
        ))}
      </ResourcesContainer>
    </div>
  );
}
