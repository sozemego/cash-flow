import React, { useEffect } from "react";
import {
  WORLD_SERVICE_CITIES_URL,
  WORLD_SERVICE_RESOURCES_URL
} from "../config/urls";
import { ICity } from "../world";
import { cityAdded, resourcesAdded } from "../world/actions";
import { useDispatch } from "react-redux";
import { GameMapFull } from "./GameMapFull";
import { useTruckSocket } from "../truck/useTruckSocket";
import { useFactorySocket } from "../factory/useFactorySocket";
import { FactoryList } from "../factory/FactoryGroup";
import { useGetFactories } from "../factory/selectors";
import { TruckList } from "../truck/TruckList";
import { useGetTrucks } from "../truck/selectors";
import { GameOnMapProps } from "./index";
import styled, { css } from "styled-components";

const Container = styled.div`
  display: grid;
`;

const GameMapContainer = styled.div`
  grid-column: 1;
  grid-row: 1;
  width: 100%;
`;

const OverlayContainer = styled.div`
  grid-column: 1;
  grid-row: 1;
  z-index: 1059;
  maxheight: height;
  width: 100%;
  overflow: scroll;
  background-color: transparent;
  pointer-events: none;
  display: flex;
  justify-content: space-between;
  ${props => css`
    // @ts-ignore
    width: ${props.width};
  `}
`;

const LeftSideContainer = styled.div`
  width: 15%;
  background: white;
  pointer-events: all;
`;

const RightSideContainer = styled.div`
  width: 25%;
  background: white;
  pointer-events: all;
`;

export function GameOnMap({ height }: GameOnMapProps) {
  const dispatch = useDispatch();

  useTruckSocket();
  useFactorySocket();
  const factories = useGetFactories();
  const trucks = Object.values(useGetTrucks());

  useEffect(() => {
    fetch(WORLD_SERVICE_CITIES_URL)
      .then<ICity[]>(res => res.json())
      .then(cities => cities.forEach(city => dispatch(cityAdded(city))));
  }, [dispatch]);

  useEffect(() => {
    fetch(WORLD_SERVICE_RESOURCES_URL)
      .then(res => res.json())
      .then(resources => dispatch(resourcesAdded(resources)));
  }, [dispatch]);

  return (
    <Container>
      <GameMapContainer>
        <GameMapFull height={height} />
      </GameMapContainer>
      <OverlayContainer>
        <LeftSideContainer>
          <FactoryList factories={factories} />
        </LeftSideContainer>
        <div></div>
        <RightSideContainer>
          <TruckList trucks={trucks} />
        </RightSideContainer>
      </OverlayContainer>
    </Container>
  );
}
