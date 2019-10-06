import React from "react";
import { Game } from "./game/Game";
import { Clock } from "./clock/Clock";
import { Player } from "./player/Player";
import 'antd/dist/antd.css';

function App() {
  return (
    <div>
      <div style={{backgroundColor: "#e7e7e7"}}>
        <h2 style={{ textAlign: "center" }}>Cash flow</h2>
        <div style={{ textAlign: "center" }}>
          <Clock />
          <Player />
          <hr />
        </div>
      </div>

      <Game />
    </div>
  );
}

export default App;
