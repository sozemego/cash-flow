import React from "react";

export interface ResourceIconProps {
  resource: string;
}

export function ResourceIcon({ resource }: ResourceIconProps) {
  return (
    <img
      src={`/img/resources/${resource}.png`}
      alt={resource}
      style={{ width: "48px" }}
    />
  );
}
