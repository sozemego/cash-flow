import React from "react";
import { Header } from "./Header";
import { Logs } from "./Logs";

function App() {
  return (
    <div style={{ overflow: "always" }}>
      <Header />
      <Logs />
    </div>
  );
}

export default App;
