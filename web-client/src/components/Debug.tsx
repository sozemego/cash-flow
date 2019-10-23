import React from "react";

export function Debug({ obj, style = {} }) {
  const debug = JSON.stringify(obj, null, 2);
  return <pre style={style}>{debug}</pre>;
}
