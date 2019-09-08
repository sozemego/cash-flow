import React from "react";
import styled, { css } from "styled-components";

const Container = styled.div`
  border: 1px solid gray;
  width: 100%;
  ${props => css`
    height: ${props.height};
  `}
`;

const Bar = styled.div`
  ${props => css`
    width: ${props.width}%;
    border-top: ${props.height / 2}px solid red;
    border-bottom: ${props.height / 2}px solid red;
    opacity: ${props.disabled ? 0.25 : 1}
  `}
`;

export function ProgressBar({ current = 0, time = 0, height = 2, disabled }) {
  let percent = (current / time) * 100;
  if (isNaN(percent)) {
    percent = 0;
  }

  return (
    <Container height={height}>
      <Bar height={height} width={percent} disabled={disabled}/>
    </Container>
  );
}
