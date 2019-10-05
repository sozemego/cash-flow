import React, { useState } from "react";
import { Divider } from "antd";
import Search from "antd/lib/input/Search";
import CheckableTag from "antd/lib/tag/CheckableTag";

export function Events({ events }) {
  const [filterPhrase, setFilterPhrase] = useState("");
  const [checkedType, setCheckType] = useState({});

  const uniqueTypes = [...new Set(events.map(event => event.type))];
  const phraseFilter = event =>
    event.toLowerCase().includes(filterPhrase.toLowerCase());
  const typeFilter =
    Object.values(checkedType).filter(Boolean).length === 0
      ? e => true
      : event => checkedType[event.type] === true;

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
        {uniqueTypes.map(type => (
          <CheckableTag
            color={"green"}
            key={type}
            checked={checkedType[type] === true}
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
