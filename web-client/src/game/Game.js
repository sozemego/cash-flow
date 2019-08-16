import React from "react";
import { FactoryList } from "../factory/FactoryList";
import { useGetFactories} from "../factory/selectors";

export function Game() {

  const factories = useGetFactories();

  return (
    <div>
      <div style={{width: "25%"}}>
        <FactoryList factories={factories} />
      </div>
    </div>
  );
}
