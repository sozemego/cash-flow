import React from "react";

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
      <div style={{ color: "gray", fontSize: "0.85rem" }}>Storage</div>
      <div>
        Capacity {capacityTaken(storage)} / {storage.capacity}
      </div>
      <div style={{ display: "flex", flexDirection: "row", minHeight: "64px" }}>
        {resourceCounts.length === 0 && (
          <div style={{ color: "gray", fontSize: "0.75rem" }}>Empty</div>
        )}
        {resourceCounts.map(({ resource, count }) => (
          <div
            style={{
              border: "1px dotted gray",
              display: "flex",
              alignItems: "center"
            }}
          >
            <img
              src={`/img/resources/${resource}.png`} alt={resource}
              style={{ width: "48px" }}
            />
            <span>{count}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
