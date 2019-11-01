import React from "react";
import { useTruckSocket } from "./useTruckSocket";
import styled from "styled-components";
import { READY_STATE_TABLE } from "../websocket/hook";
import { Truck } from "./Truck";
import { Typography, Divider } from "antd";
import { TruckListProps } from "./index";
import { FLAGS } from "../featureFlags";
import { useGetSelectedCityId } from "../game/selectors";
import { CityInline } from "../world/CityInline";

const Container = styled.div`
  margin-left: 12px;
`;

const ListContainer = styled.div`
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  max-height: 1000px;
`;

export function TruckList({ trucks }: TruckListProps) {
  const { readyState } = useTruckSocket();
  const gameOnMap = FLAGS.GAME_ON_MAP;
  const selectedCityId = useGetSelectedCityId();

  return (
    <Container>
      {!gameOnMap && (
        <Typography>
          Trucks - state [{READY_STATE_TABLE[readyState]}]
        </Typography>
      )}
      {gameOnMap && (
        <Typography>
          Trucks in <CityInline cityId={selectedCityId} />
        </Typography>
      )}
      <Divider />
      <ListContainer>
        {trucks.map(truck => (
          <Truck key={truck.id} truck={truck} />
        ))}
      </ListContainer>
    </Container>
  );
}
