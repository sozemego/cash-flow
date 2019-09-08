import React, { useEffect, useState } from "react";
import { usePlayerSocket } from "./usePlayerSocket";
import { PLAYER_SERVICE_URL } from "../config/urls";

export function Player({}) {
  const { socket } = usePlayerSocket();

  const [player, setPlayer] = useState({});

  useEffect(() => {
    fetch(PLAYER_SERVICE_URL + "/player")
      .then(response => response.json(), console.log)
      .then(payload => setPlayer(payload));
  }, []);

  return (
    <div>
      <div>
        {player.name} - ${player.cash}
      </div>
    </div>
  );
}
