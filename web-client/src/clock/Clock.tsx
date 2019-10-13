import React from "react";

import { useGameClock } from "./gameClock";
import Text from "antd/lib/typography/Text";
import { getFormattedDateTime } from "./business";


export function Clock() {
  const { date } = useGameClock({ interval: 1000 });

  return <Text code>{getFormattedDateTime(date)}</Text>;
}
