import { Marker } from "react-leaflet";
import { getTruckIcon } from "./business";
import React from "react";
import { ITruck } from "../truck";
import { LatLngTuple } from "leaflet";
import { useGetCities } from "../world/selectors";
import { CityTrucksProps } from "./index";
import { useGetTrucks } from "../truck/selectors";

export function CityTrucks({ zoom }: CityTrucksProps) {
  const cities = useGetCities();
  const trucks = useGetTrucks();

  function getPosition(truck: ITruck, index: number): LatLngTuple {
    const { currentCityId } = truck.navigation;
    const cityPosition = getCityPosition(currentCityId);
    if (cityPosition[0] === 0 && cityPosition[1] === 0) {
      return cityPosition;
    }
    const zoomMultiplier: Record<number, number> = {
      3: 4,
      4: 1.25,
      5: 0.75,
      6: 0.35,
      7: 0.2
    };
    return [
      cityPosition[0],
      cityPosition[1] + 0.1 + index * zoomMultiplier[zoom]
    ];
  }

  function getCityPosition(cityId: string): LatLngTuple {
    const city = cities[cityId];
    if (!city) {
      return [0, 0];
    }
    return [city.latitude, city.longitude];
  }

  const trucksAtCity = Object.values(trucks).filter(
    truck => truck.navigation.nextCityId === null
  );
  const trucksPerCity: Record<string, ITruck[]> = {};
  trucksAtCity.forEach(truck => {
    const trucks = trucksPerCity[truck.navigation.currentCityId] || [];
    trucks.push(truck);
    trucksPerCity[truck.navigation.currentCityId] = trucks;
  });

  return (
    <>
      {Object.entries(trucksPerCity).map(entry => {
        const trucks = entry[1];
        return trucks.map((truck, index) => (
          <Marker
            key={truck.id}
            position={getPosition(truck, index + 1)}
            icon={getTruckIcon(truck)}
          />
        ));
      })}
    </>
  );
}
