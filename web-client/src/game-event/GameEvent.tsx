import React from "react";
import { GameEventProps } from "./index";

export function GameEvent({ event }: GameEventProps) {
  return <div>{event.text}</div>;
}
