import React, { useEffect } from "react";
import { FactoryList } from "../factory/FactoryList";
import { useGetFactories } from "../factory/selectors";
import { TruckList } from "../truck/TruckList";
import { useGetTrucks } from "../truck/selectors";
import { CityList } from "../city/CityList";
import { useGetCities } from "../city/selectors";
import {useDispatch} from "react-redux";
import {cityAdded} from "../city/actions";

export function Game() {
  const factories = useGetFactories();
  const trucks = useGetTrucks();
  const cities = useGetCities();
  const dispatch = useDispatch();

  useEffect(() => {
    fetch("http://localhost:9000/world/")
      .then(res => res.json())
      .then(cities => cities.forEach(city => dispatch(cityAdded(city))));
  }, [dispatch]);

  return (
    <div style={{ display: "flex", flexDirection: "row" }}>
      <div style={{ width: "25%" }}>
        <FactoryList factories={factories} />
      </div>
      <div style={{ width: "25%" }}>
        <TruckList trucks={trucks} />
      </div>
      <div style={{ width: "25%" }}>
        <CityList cities={cities} />
      </div>
    </div>
  );
}
