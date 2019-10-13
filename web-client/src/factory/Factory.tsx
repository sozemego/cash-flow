import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { FACTORY_SERVICE_URL_EVENTS } from "../config/urls";
import { getFormattedTime } from "../clock/business";
import { useGameClock } from "../clock/gameClock";
import { useGetCities, useGetHighlightedCity } from "../world/selectors";
import { useDispatch } from "react-redux";
import { cityHighlighted } from "../world/actions";
import { Storage } from "../components/Storage";
import Divider from "antd/lib/divider";
import Progress from "antd/lib/progress";
import Card from "antd/lib/card";
import Tag from "antd/lib/tag";
import Icon from "antd/lib/icon";
import { Modal, Tooltip } from "antd";
import { Debug } from "../components/Debug";
import { Events } from "../components/Events";
import {FactoryEvent, IFactory} from "./index.d";

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Producer = styled.div`
  display: flex;
  flex-direction: column;
`;

const ProducerProgress = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const ProductionDate = styled.div`
  margin: 4px;
`;

function formatDuration(minutes: number): string {
  let hours = 0;
  while (minutes >= 60) {
    hours += 1;
    minutes -= 60;
  }

  const pluralHours = hours === 0 || hours > 1;
  const pluralMinutes = minutes === 0 || minutes > 1;

  if (minutes === 0) {
    return `${hours} hour${pluralHours ? "s" : ""}`;
  }

  if (hours === 0) {
    return `${minutes} minute${pluralMinutes ? "s" : ""}`;
  }

  return `${hours} hour${pluralHours ? "s" : ""} and ${minutes} minute${
    pluralMinutes ? "s" : ""
  }`;
}

export interface FactoryProps {
  factory: IFactory;
}

export function Factory({ factory }: FactoryProps) {
  const [showEvents, setShowEvents] = useState(false);
  const dispatch = useDispatch();
  const highlightedCityId = useGetHighlightedCity();
  const cities = useGetCities();
  const { id, name, storage, producer, cityId } = factory;
  const cityName = cities[cityId].name;

  const { time, clock } = useGameClock({ interval: 500 });
  const minutes = producer.time;
  const ms = minutes * 1000;

  let productionTimePassed =
    (time - producer.productionStartTime) / clock.multiplier;
  if (productionTimePassed >= ms) {
    productionTimePassed = ms;
  }

  const productionEndTime = new Date(
    producer.productionStartTime + ms * clock.multiplier
  );

  const cardStyle: React.CSSProperties = Object.assign(
    {},
    highlightedCityId === cityId ? { background: "#e0e0e0" } : {}
  );

  return (
    <>
      <Header>
        <div>
          <Tag color={"blue"}>{name}</Tag>
          <Tag color={"gold"}>{cityName}</Tag>
          <Tag color={"gray"}>{id}</Tag>
        </div>
        <div>
          <Icon type="ordered-list" onClick={() => setShowEvents(true)} />
          <FactoryEvents
            factory={factory}
            showEvents={showEvents}
            onClose={() => setShowEvents(false)}
          />
          <Tooltip title={<Debug obj={factory} />}>
            <Icon type={"question-circle"} />
          </Tooltip>
        </div>
      </Header>
      <Card
        onMouseEnter={() => dispatch(cityHighlighted(cityId))}
        onMouseLeave={() => dispatch(cityHighlighted(null))}
        bodyStyle={cardStyle}
      >
        <Storage storage={storage} />
        <Divider style={{ margin: "2px" }} />
        <div>
          <Producer>
            <div>
              <span>
                Production of 1 {producer.resource} takes{" "}
                {formatDuration(minutes)}
              </span>
            </div>
            <ProducerProgress>
              <ProductionDate>
                {getFormattedTime(new Date(producer.productionStartTime))}
              </ProductionDate>
              <Progress
                percent={(productionTimePassed / ms) * 100}
                showInfo={false}
                strokeColor={productionTimePassed === ms ? "gray" : "green"}
              />
              <ProductionDate>
                {getFormattedTime(productionEndTime)}
              </ProductionDate>
            </ProducerProgress>
          </Producer>
        </div>
      </Card>
    </>
  );
}

export interface FactoryEventsProps {
  factory: IFactory;
  showEvents: boolean;
  onClose: (e: React.MouseEvent<HTMLElement>) => void;
}

export function FactoryEvents({
  factory,
  showEvents,
  onClose
}: FactoryEventsProps) {
  const [events, setEvents] = useState([]);
  useEffect(() => {
    if (!showEvents) {
      return;
    }
    fetch(FACTORY_SERVICE_URL_EVENTS + "?id=" + factory.id)
      .then(result => result.json())
      .then(events => {
        return events.map((event: FactoryEvent) => {
          const parsedEvent: any = { ...event };
          const { timestamp: ts } = event;
          parsedEvent.timestamp = new Date(
            ts[0],
            ts[1],
            ts[2],
            ts[3],
            ts[4],
            ts[5]
          );
          return parsedEvent;
        });
      })
      .then(setEvents);
  }, [showEvents, factory.id]);

  return (
    <Modal visible={showEvents} onOk={onClose} onCancel={onClose} width={1400}>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-around"
        }}
      >
        <Events events={events} />
        <Debug obj={factory} style={{ flexBasis: 0, flexGrow: 1 }} />
      </div>
    </Modal>
  );
}
