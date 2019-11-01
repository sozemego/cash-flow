import React from "react";

export interface FactoryIconProps {
  texture: string;
}

export function FactoryIcon({ texture }: FactoryIconProps) {
  return (
    <img
      src={`/img/factory/${texture}`}
      alt={"Factory icon"}
      style={{ width: "32px", height: "32px" }}
    />
  );
}
