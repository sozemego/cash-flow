import React, { useEffect } from "react";
import {
  WORLD_SERVICE_CITIES_URL,
  WORLD_SERVICE_RESOURCES_URL
} from "../config/urls";
import { citiesAdded, resourcesAdded } from "../world/actions";
import { useDispatch } from "react-redux";
import { GameMapFull } from "./GameMapFull";
import { useTruckSocket } from "../truck/useTruckSocket";
import { useFactorySocket } from "../factory/useFactorySocket";
import { FactoryList } from "../factory/FactoryGroup";
import { useGetFactories } from "../factory/selectors";
import { TruckList } from "../truck/TruckList";
import { useGetTrucks } from "../truck/selectors";
import { GameProps } from "./index";
import styled, { css } from "styled-components";
import { useGetSelectedCityId } from "./selectors";
import { useGetCities } from "../world/selectors";
import { citySelected, truckSelected } from "./actions";
import { getAsJson } from "../rest/client";

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
  width: 100%;
  overflow: scroll;
  background-color: transparent;
  pointer-events: none;
  display: flex;
  justify-content: space-between;
  ${props => css`
    // @ts-ignore
    width: ${props.width};
    // @ts-ignore
    max-height: ${props.height};
  `}
`;

const LeftSideContainer = styled.div`
  width: 15%;
  background: #e7e7e7;
  pointer-events: all;
`;

const RightSideContainer = styled.div`
  width: 25%;
  background: #e7e7e7;
  pointer-events: all;
`;

export function Game({ height }: GameProps) {
  const dispatch = useDispatch();

  useTruckSocket();
  useFactorySocket();
  let factories = useGetFactories();
  let trucks = Object.values(useGetTrucks());
  const cities = useGetCities();

  useEffect(() => {
    getAsJson(WORLD_SERVICE_CITIES_URL)
      .then(cities => dispatch(citiesAdded(cities)));
  }, [dispatch]);

  useEffect(() => {
    getAsJson(WORLD_SERVICE_RESOURCES_URL)
      .then(resources => dispatch(resourcesAdded(resources)));
  }, [dispatch]);

  useEffect(() => {
    function listener(event: KeyboardEvent) {
      if (event.code === "Escape") {
        dispatch(citySelected(""));
        dispatch(truckSelected(""));
      }
    }
    window.addEventListener("keyup", listener);
    return () => window.removeEventListener("keyup", listener);
  }, [dispatch]);

  let city = null;
  const selectedCityId = useGetSelectedCityId();
  if (selectedCityId) {
    city = cities[selectedCityId];
    factories = factories.filter(factory => factory.cityId === selectedCityId);
    trucks = trucks.filter(
      truck => truck.navigation.currentCityId === selectedCityId
    );
  }

  return (
    <Container>
      <GameMapContainer>
        <GameMapFull height={height} />
      </GameMapContainer>
      <OverlayContainer>
        {city && (
          <LeftSideContainer>
            <FactoryList factories={factories} />
          </LeftSideContainer>
        )}
        <div></div>
        {city && (
          <RightSideContainer>
            <TruckList trucks={trucks} />
          </RightSideContainer>
        )}
      </OverlayContainer>
    </Container>
  );
}
