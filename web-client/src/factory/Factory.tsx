import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { FACTORY_SERVICE_URL_EVENTS } from "../config/urls";
import { getFormattedTime } from "../clock/business";
import { useGameClock } from "../clock/useGameClock";
import { useGetCities, useGetHighlightedCity } from "../world/selectors";
import { useDispatch } from "react-redux";
import { cityHighlighted } from "../world/actions";
import Divider from "antd/lib/divider";
import Progress from "antd/lib/progress";
import Card from "antd/lib/card";
import Tag from "antd/lib/tag";
import Icon from "antd/lib/icon";
import { Modal, Tooltip } from "antd";
import { Debug } from "../components/Debug";
import { Events } from "../components/Events";
import {
  FactoryEvent,
  FactoryEventsProps,
  FactoryProps,
  IFactory,
  ProducerInputOutputProps,
  ResourceCountProps
} from "./index";
import { FactoryStorage } from "./FactoryStorage";
import { ResourceName } from "../world";
import { ResourceIcon } from "../components/ResourceIcon";
import { IResourceCount } from "../storage";
import { getAsJson } from "../rest/client";
import { JsonResponse } from "../rest";

const Container = styled.div`
  max-height: 250px;
  width: 100%;
  max-width: 450px;
  margin: 6px;
`;

const Header = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Producer = styled.div`
  display: flex;
  flex-direction: column;
`;

const ProducerResources = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
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

export function Factory({ factory }: FactoryProps) {
  const [showEvents, setShowEvents] = useState(false);
  const dispatch = useDispatch();
  const highlightedCityId = useGetHighlightedCity();
  const cities = useGetCities();
  const { id, name, storage, cityId } = factory;
  const city = cities[cityId];
  const cityName = city ? city.name : '';

  const cardStyle: React.CSSProperties = Object.assign(
    {},
    highlightedCityId === cityId ? { background: "#e0e0e0" } : {}
  );

  return (
    <Container>
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
        <FactoryStorage storage={storage} />
        <Divider style={{ margin: "2px" }} />
        <div>
          <FactoryProducer factory={factory} />
        </div>
      </Card>
    </Container>
  );
}

export function FactoryProducer({ factory }: { factory: IFactory }) {
  const { producer } = factory;

  const { time, clock } = useGameClock({ interval: 500 });
  const minutes = producer.time;
  const ms = minutes * 1000;

  const productionEndTime = new Date(
    producer.productionStartTime + ms * clock.multiplier
  );

  let productionTimePassed =
    (time - producer.productionStartTime) / clock.multiplier;
  if (productionTimePassed >= ms) {
    productionTimePassed = ms;
  }

  return (
    <Producer>
      <ProducerResources>
        <ProducerInputOutput input={producer.input} output={producer.output} />
        <div>
          <Icon type="clock-circle" />
          <span style={{ marginLeft: "4px" }}>{formatDuration(minutes)}</span>
        </div>
      </ProducerResources>
      <ProducerProgress>
        <ProductionDate>
          {getFormattedTime(new Date(producer.productionStartTime))}
        </ProductionDate>
        <Progress
          percent={(productionTimePassed / ms) * 100}
          showInfo={false}
          strokeColor={productionTimePassed === ms ? "gray" : "green"}
        />
        <ProductionDate>{getFormattedTime(productionEndTime)}</ProductionDate>
      </ProducerProgress>
    </Producer>
  );
}

export function ProducerInputOutput({
  input,
  output
}: ProducerInputOutputProps) {
  function nonZeroCount(resourceCount: IResourceCount) {
    return resourceCount.count > 0;
  }

  const inputs = Object.keys(input)
    .map(resource => ({
      resource: resource as ResourceName,
      count: input[resource as ResourceName]
    }))
    .filter(nonZeroCount);
  const outputs = Object.keys(output)
    .map(resource => ({
      resource: resource as ResourceName,
      count: output[resource as ResourceName]
    }))
    .filter(nonZeroCount);

  return (
    <div
      style={{ display: "flex", flexDirection: "row", alignItems: "center" }}
    >
      {inputs.map(input => (
        <ResourceCount
          key={input.resource}
          resourceCount={input}
          input={"INPUT"}
        />
      ))}
      {inputs.length > 0 && (
        <Icon type="double-right" style={{ marginRight: "8px" }} />
      )}
      {outputs.map(output => (
        <ResourceCount
          key={output.resource}
          resourceCount={output}
          input={"OUTPUT"}
        />
      ))}
    </div>
  );
}

export function ResourceCount({ resourceCount, input }: ResourceCountProps) {
  const { resource, count } = resourceCount;
  return (
    <Tag color={input === "INPUT" ? "red" : "green"} key={resource}>
      <span>{count}</span>
      <ResourceIcon resource={resource} />
    </Tag>
  );
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
    getAsJson<FactoryEvent[]>(FACTORY_SERVICE_URL_EVENTS + "?id=" + factory.id)
      .then((response: JsonResponse<FactoryEvent[]>) => {
        return response.payload.map((event: FactoryEvent) => {
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
      .then(events => setEvents(events as any));
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
