import React, { useState } from "react";
import { Divider } from "antd";
import Search from "antd/lib/input/Search";

export function Events({ events }) {
  const [filterPhrase, setFilterPhrase] = useState("");

  return (
    <div
      style={{ maxHeight: "560px", overflowY: "scroll", overflowX: "hidden", flexBasis: 0, flexGrow: 1}}
    >
      <Search
        value={filterPhrase}
        onChange={e => setFilterPhrase(e.target.value)}
      />
      {events
        .map(event => {
          return JSON.stringify(event, null, 2);
        })
        .filter(event => event.toLowerCase().includes(filterPhrase.toLowerCase()))
        .map((event, index) => {
          const withoutFirstBracket = event.substr(2);
          const withoutLastBracket = withoutFirstBracket.substr(0, withoutFirstBracket.length - 2);
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
