import React, { useEffect } from "react";
import { FactoryList } from "../factory/FactoryList";
import { useGetFactories} from "../factory/selectors";
import { useDispatch } from "react-redux";

export function Game() {
  const dispatch = useDispatch();

  const factories = useGetFactories();

  return (
    <div>
      <div style={{width: "25%"}}>
        <FactoryList factories={factories} />
      </div>
    </div>
  );
}
