import React from "react";
import { DatePicker, TimePicker } from "antd";
import moment from "moment";

import { useGameClock } from "./gameClock";

const dateFormat = `DD-MM-YYYY`;
const timeFormat = "HH:mm";

export function Clock() {
  const { date } = useGameClock({ interval: 1000 });

  return (
    <>
      <DatePicker
        value={moment(
          `${date.getDate() + 1}/${date.getMonth()}/${date.getFullYear()}`,
          dateFormat
        )}
        format={dateFormat}
				allowClear={false}
      />
      <TimePicker
        value={moment(`${date.getHours()}:${date.getMinutes()}`, timeFormat)}
        format={timeFormat}
				allowClear={false}
      />
    </>
  );
}
