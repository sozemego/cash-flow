import React from "react";
import styled from "styled-components";
import { City } from "./City";
import Tag from "antd/lib/tag";
import { Typography } from "antd";
import { Divider } from "antd/es";

const Container = styled.div`
  margin-left: 12px;
`;

const Header = styled.div`
  min-height: 50px;
`;

export function CityList({ cities }) {
  const cityList = Object.values(cities);

  return (
    <Container>
      <Typography level={3}>CITIES</Typography>
      <Divider/>
      {cityList.map(city => (
        <City key={city.id} city={city} />
      ))}
    </Container>
  );
}
