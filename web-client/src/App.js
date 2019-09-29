import React from "react";
import { Game } from "./game/Game";
import { Clock } from "./clock/Clock";
import { Player } from "./player/Player";
import Container from "@material-ui/core/Container";

function App() {
  return (
    <Container maxWidth={"xl"}>
      <h2 style={{ textAlign: "center" }}>Cash flow</h2>
      <div style={{ textAlign: "center" }}>
        <Clock />
        <Player />
        <hr />
      </div>
      <Game />
    </Container>
  );
}

export default App;
