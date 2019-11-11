import React, { useEffect } from "react";
import { usePlayerSocket } from "./usePlayerSocket";
import { PLAYER_SERVICE_PLAYER_URL } from "../config/urls";
import { playerAdded } from "./actions";
import { useGetPlayer } from "./selectors";
import { useDispatch } from "react-redux";

export function Player() {
  usePlayerSocket();
  const player = useGetPlayer();
  const dispatch = useDispatch();

  useEffect(
    () => {
      fetch(PLAYER_SERVICE_PLAYER_URL)
        .then(response => response.json())
        .then(payload => dispatch(playerAdded(payload)))
        .catch((err) => console.log(err));
    },
    /* eslint-disable-line */ []
  );

  return (
    <div>
      <div>
        {player.name} - ${player.cash}
      </div>
    </div>
  );
}
