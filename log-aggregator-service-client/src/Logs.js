import React, { useEffect, useLayoutEffect } from "react";
import CheckableTag from "antd/lib/tag/CheckableTag";
import styled from "styled-components";
import { Log } from "./Log";

const Container = styled.div``;

const LogsContainer = styled.div`
  max-height: ${props => props.height || 0}px;
  overflow: scroll;
  background: #300A24;
  color: green;
`;

const Filters = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  margin-bottom: 8px;
`;

export function Logs() {
  const scrollRef = React.useRef();
  const [logs, setLogs] = React.useState([]);
  const [appFilter, setAppFilter] = React.useState({});
  const [levelFilter, setLevelFilter] = React.useState({});

  useEffect(() => {
    fetch("http://localhost:8999/log-aggregator-service/log")
      .then(response => response.json())
      .then(setLogs);
  }, []);

  const applications = new Set();
  logs.forEach(log => applications.add(log.application));

  const levels = new Set();
  logs.forEach(log => levels.add(log.level));

  function areAllValuesFalse(obj) {
    return Object.values(obj).filter(Boolean).length === 0;
  }

  function levelFilterFn(log) {
    if (areAllValuesFalse(levelFilter)) {
      return true;
    }
    return levelFilter[log.level] === true;
  }

  function appFilterFn(log) {
    if (areAllValuesFalse(appFilter)) {
      return true;
    }
    return appFilter[log.application] === true;
  }

  function lengthFilter(log, index, array) {
    const length = array.length;
    const max = 200;
    if (length < max) {
      return true;
    }
    const minIndex = length - max;
    return index > minIndex;
  }

  useLayoutEffect(() => {
    scrollRef.current.scrollIntoView({ behavior: "smooth" });
  });

  return (
    <Container>
      <Filters>
        <ApplicationFilter
          applications={Array.from(applications)}
          appFilter={appFilter}
          setAppFilter={setAppFilter}
        />
        <LevelFilter
          levels={Array.from(levels)}
          levelFilter={levelFilter}
          setLevelFilter={setLevelFilter}
        />
      </Filters>
      <LogsContainer height={window.innerHeight - 125}>
        {logs
          .filter(levelFilterFn)
          .filter(appFilterFn)
          .filter(lengthFilter)
          .map(log => (
            <Log log={log} key={log.id} />
          ))}
        <div ref={scrollRef} />
      </LogsContainer>
    </Container>
  );
}

export function ApplicationFilter({ applications, appFilter, setAppFilter }) {
  return (
    <div>
      <div>App filter</div>
      {applications.map(application => (
        <CheckableTag
          checked={appFilter[application]}
          key={application}
          onChange={checked => {
            appFilter = { ...appFilter };
            appFilter[application] = checked;
            setAppFilter(appFilter);
          }}
        >
          {application}
        </CheckableTag>
      ))}
    </div>
  );
}

export function LevelFilter({ levels, levelFilter, setLevelFilter }) {
  return (
    <div>
      <div>Level filter</div>
      {levels.map(level => (
        <CheckableTag
          checked={levelFilter[level]}
          key={level}
          onChange={checked => {
            levelFilter = { ...levelFilter };
            levelFilter[level] = checked;
            setLevelFilter(levelFilter);
          }}
        >
          {level}
        </CheckableTag>
      ))}
    </div>
  );
}
