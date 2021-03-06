import React from "react";

export function Debug({ obj, style = {} }: DebugProps) {
  const debug = JSON.stringify(obj, null, 2);
  return <pre style={style}>{debug}</pre>;
}

export interface DebugProps {
  obj: object;
  style?: any;
}
