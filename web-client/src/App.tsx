import React from "react";
import { Game } from "./game/Game";
import "antd/dist/antd.css";
import { Header } from "./game/Header";
import { GameOnMap } from "./game/GameOnMap";
import { FLAGS } from "./featureFlags";

function App() {
  const gameOnMap = FLAGS.GAME_ON_MAP;
  const [mapHeight, setMapHeight] = React.useState(0);

  const headerRef = React.useRef<HTMLDivElement>(null);

  function calcMapHeight() {
    const rect = headerRef.current!.getBoundingClientRect();
    const headerHeight = rect.height;
    const { innerHeight } = window;
    setMapHeight(innerHeight - headerHeight - 25);
  }

  React.useLayoutEffect(() => {
    calcMapHeight();
  }, [headerRef]);

  React.useEffect(() => {
    function listener() {
      calcMapHeight();
    }
    window.addEventListener("resize", listener);
    return () => window.removeEventListener("resize", listener);
  }, []);

  return (
    <div>
      <div style={{ backgroundColor: "#e7e7e7" }} ref={headerRef}>
        <h2 style={{ textAlign: "center" }}>Cash flow</h2>
        <Header />
      </div>
      {gameOnMap ? mapHeight > 0 && <GameOnMap height={mapHeight} /> : <Game />}
    </div>
  );
}

export default App;
