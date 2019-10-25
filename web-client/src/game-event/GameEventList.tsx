import React from "react";
import Card from "antd/lib/card";
import { Divider } from "antd";
import Typography from "antd/lib/typography/Typography";
import { useGameEventSocket } from "./useGameEventSocket";
import { READY_STATE_TABLE } from "../websocket/hook";
import { GameEvent } from "./GameEvent";
import { GameEventListProps } from "./index";

export function GameEventList({ events }: GameEventListProps) {
  const { readyState } = useGameEventSocket();

  return (
    <div>
      <Typography>
        Game events - state [{READY_STATE_TABLE[readyState]}]
      </Typography>
      <Divider />
      <Card>
        {events.map(event => (
          <GameEvent key={event.id} event={event} />
        ))}
      </Card>
    </div>
  );
}
