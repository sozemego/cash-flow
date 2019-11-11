import React, { useEffect } from "react";
import { usePlayerSocket } from "./usePlayerSocket";
import { PLAYER_SERVICE_PLAYER_BY_USER_ID_URL, PLAYER_SERVICE_PLAYER_URL } from "../config/urls";
import { playerAdded } from "./actions";
import { useGetPlayer } from "./selectors";
import { useDispatch } from "react-redux";
import { useGetUser } from "../auth/selectors";

export function Player() {
  usePlayerSocket();
  const player = useGetPlayer();
  const dispatch = useDispatch();

  const user = useGetUser()!;

  useEffect(
    () => {
      fetch(PLAYER_SERVICE_PLAYER_BY_USER_ID_URL + `?id=${user.id}`)
        .then(response => response.json())
        .then(payload => dispatch(playerAdded(payload)))
        .catch((err) => console.log(err));
    },
    /* eslint-disable-line */ [user.id]
  );

  return (
    <div>
      <div>
        {player.name} - ${player.cash}
      </div>
    </div>
  );
}
