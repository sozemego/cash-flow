import React from "react";
import "antd/dist/antd.css";
import { Header } from "./game/Header";
import { Game } from "./game/Game";

function App() {
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
      {mapHeight > 0 && <Game height={mapHeight}/>}
    </div>
  );
}

export default App;
