import React from "react";
import { Game } from "./game/Game";
import { Clock } from "./clock/Clock";
import { Player } from "./player/Player";

function App() {
  return (
    <div>
      <h2 style={{ textAlign: "center" }}>Cash flow</h2>
      <div style={{ textAlign: "center" }}>
        <Clock />
        <Player />
        <hr />
      </div>
      <Game />
    </div>
  );
}

export default App;
