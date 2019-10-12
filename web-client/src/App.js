import React from "react";
import { Game } from "./game/Game";
import 'antd/dist/antd.css';
import { Header } from "./game/Header";

function App() {
  return (
    <div>
      <div style={{backgroundColor: "#e7e7e7"}}>
        <h2 style={{ textAlign: "center" }}>Cash flow</h2>
        <Header />
      </div>
      <Game />
    </div>
  );
}

export default App;
