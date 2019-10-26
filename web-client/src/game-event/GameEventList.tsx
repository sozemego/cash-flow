import React from "react";
import Card from "antd/lib/card";
import { Divider } from "antd";
import Typography from "antd/lib/typography/Typography";
import { useGameEventSocket } from "./useGameEventSocket";
import { READY_STATE_TABLE } from "../websocket/hook";
import { GameEvent } from "./GameEvent";
import { GameEventListProps } from "./index";
import { getGameEventText } from "./business";
import { AppState } from "../store";
import { useStore } from "react-redux";

export function GameEventList({ events }: GameEventListProps) {
  const { readyState } = useGameEventSocket();
  const store = useStore<AppState>();

  events = events
    .map(event => {
      return {
        ...event,
        text: getGameEventText(event, store.getState())
      };
    })
    .sort((a, b) => {
      const timeA = a.timestamp.getTime();
      const timeB = b.timestamp.getTime();
      return timeA > timeB ? -1 : timeA === timeB ? 0 : 1;
    })
    .filter((_, index) => index < 15);

  return (
    <div style={{ marginLeft: "12px" }}>
      <Typography>
        Game events - state [{READY_STATE_TABLE[readyState]}]
      </Typography>
      <Divider />
      <div style={{ height: "22px", opacity: 0 }}>A</div>
      <Card>
        {events.map(event => (
          <GameEvent key={event.id} event={event} />
        ))}
      </Card>
    </div>
  );
}
