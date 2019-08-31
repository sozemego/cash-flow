import React from "react";
import { Game } from "./game/Game";
import { Clock } from "./clock/Clock";

function App() {
  return (
    <div>
      <h2 style={{ textAlign: "center" }}>Cash flow</h2>
      <div style={{ textAlign: "center" }}>
        <Clock />
        <hr />
      </div>
      <Game />
    </div>
  );
}

export default App;
