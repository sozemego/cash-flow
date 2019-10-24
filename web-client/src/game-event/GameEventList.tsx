import React from "react";
import { useGameEventSocket } from "./useGameEventSocket";
import { READY_STATE_TABLE } from "../websocket/hook";
import Typography from "antd/lib/typography/Typography";
import { Divider } from "antd";
import { GameEvent } from "./GameEvent";

export function GameEventList({ events }) {
  const { readyState, socket } = useGameEventSocket();

  return (
    <div>
      <Typography>
        Game events - state [{READY_STATE_TABLE[readyState]}]
      </Typography>
      <Divider />
      {events.map(event => (
        <GameEvent event={event} />
      ))}
    </div>
  );
}
