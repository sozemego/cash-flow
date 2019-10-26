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
    <div style={{ marginLeft: "12px" }}>
      <Typography>
        Game events - state [{READY_STATE_TABLE[readyState]}]
      </Typography>
      <Divider />
      <div style={{ height: "22px", opacity: 0 }}>A</div>
      <Card>
        {events.map((event, index) => (
          <>
            <GameEvent key={event.id} event={event} />
            {/*{index < events.length - 1 && <Divider />}*/}
          </>
        ))}
      </Card>
    </div>
  );
}
