import React from "react";
import styled from "styled-components";
import { Truck } from "./Truck";
import { Typography, Divider } from "antd";
import { TruckListProps } from "./index";
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
  const selectedCityId = useGetSelectedCityId();

  return (
    <Container>
      <Typography>
        Trucks in <CityInline cityId={selectedCityId} />
      </Typography>
      <Divider />
      <ListContainer>
        {trucks.map(truck => (
          <Truck key={truck.id} truck={truck} />
        ))}
      </ListContainer>
    </Container>
  );
}
