import React from 'react';

export function Debug({ obj }) {
  const debug = JSON.stringify(obj, null, 2);
  return <pre>{debug}</pre>;
}
