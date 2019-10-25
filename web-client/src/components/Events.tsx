import React, { useState } from "react";
import { Divider } from "antd";
import Search from "antd/lib/input/Search";
import CheckableTag from "antd/lib/tag/CheckableTag";

interface Event {
  type: string;
}

export interface EventsProps {
  events: Event[];
}

interface CheckedTypes {
    [type: string]: boolean;
}

export function Events({ events }: EventsProps) {
  const [filterPhrase, setFilterPhrase] = useState("");
  const [checkedType, setCheckType] = useState<CheckedTypes>({});

  const uniqueTypes: string[] = [...new Set(events.map(event => event.type))];
  const phraseFilter = (event: string) =>
    event.toLowerCase().includes(filterPhrase.toLowerCase());
  const typeFilter =
    Object.values(checkedType).filter(Boolean).length === 0
      ? (_: Event) => true
      : (event: Event) => checkedType[event.type];

  // @ts-ignore
  return (
    <div
      style={{
        maxHeight: "560px",
        overflowY: "scroll",
        overflowX: "hidden",
        flexBasis: 0,
        flexGrow: 1
      }}
    >
      <Search
        value={filterPhrase}
        onChange={e => setFilterPhrase(e.target.value)}
      />
      <div style={{ display: "flex", flexDirection: "row", flexWrap: "wrap" }}>
        {uniqueTypes.map((type: string) => (
          //@ts-ignore
          <CheckableTag
            color={"green"}
            key={type}
            checked={checkedType[type]}
            onChange={v => {
              checkedType[type] = v;
              setCheckType(checkedType);
            }}
          >
            {type}
          </CheckableTag>
        ))}
      </div>
      {events
        .filter(typeFilter)
        .map(event => {
          return JSON.stringify(event, null, 2);
        })
        .filter(phraseFilter)
        .map((event, index) => {
          const withoutFirstBracket = event.substr(2);
          const withoutLastBracket = withoutFirstBracket.substr(
            0,
            withoutFirstBracket.length - 2
          );
          return (
            <span key={index}>
              <pre>{withoutLastBracket}</pre>
              <Divider style={{ margin: "2px" }} />
            </span>
          );
        })}
    </div>
  );
}
