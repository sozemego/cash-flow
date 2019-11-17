import React, { useEffect } from "react";
import { usePlayerSocket } from "./usePlayerSocket";
import { PLAYER_SERVICE_PLAYER_BY_USER_ID_URL } from "../config/urls";
import { playerAdded } from "./actions";
import { useGetPlayer } from "./selectors";
import { useDispatch } from "react-redux";
import { useGetUser } from "../auth/selectors";
import { getAsJson } from "../rest/client";

export function Player() {
  usePlayerSocket();
  const player = useGetPlayer();
  const dispatch = useDispatch();

  const user = useGetUser()!;

  async function getPlayer() {
      const response = await getAsJson(PLAYER_SERVICE_PLAYER_BY_USER_ID_URL + `?id=${user.id}`);
      if (response.status === 404) {
          getPlayer();
          return;
      }
      dispatch(playerAdded(response.payload));
  }

  useEffect(
    () => {
        getPlayer();
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
