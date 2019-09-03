import React from "react";
import styled from 'styled-components';

const Container = styled.div`
  color: gray;
  font-size: 0.85rem;
`;

const ResourcesContainer = styled.div`
  display: flex;
  flex-direction: row;
  min-height: 60px;
`;

const Empty = styled.div`
  color: gray;
  font-size: 0.75rem;
`;

const Resource = styled.div`
border: 1px dotted gray;
              display: flex;
              align-items: center;
`;

function capacityTaken(storage) {
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

  return (
    <div>
      <Container>Storage</Container>
      <div>
        Capacity {capacityTaken(storage)} / {storage.capacity}
      </div>
      <ResourcesContainer>
        {resourceCounts.length === 0 && (
          <Empty>Empty</Empty>
        )}
        {resourceCounts.map(({ resource, count }) => (
          <Resource key={resource}>
            <img
              src={`/img/resources/${resource}.png`} alt={resource}
              style={{ width: "48px" }}
            />
            <span>{count}</span>
          </Resource>
        ))}
      </ResourcesContainer>
    </div>
  );
}
