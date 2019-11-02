import { LatLngTuple } from "leaflet";
import { Marker, Polyline, Tooltip } from "react-leaflet";
import React from "react";
import { useGetTrucks } from "../truck/selectors";
import { useGameClock } from "../clock/useGameClock";
import { useGetCities } from "../world/selectors";
import { getTruckIcon } from "./business";

export function TravellingTrucks() {
  const trucks = useGetTrucks();
  const cities = useGetCities();

  const travellingTrucks = Object.values(trucks).filter(
    truck => truck.navigation.nextCityId !== null
  );
  const { time } = useGameClock({ interval: 2500 });

  function getCityPosition(cityId: string): LatLngTuple {
    const city = cities[cityId];
    if (!city) {
      return [0, 0];
    }
    return [city.latitude, city.longitude];
  }

  function getCityName(cityId: string): string {
    const city = cities[cityId];
    return city ? city.name : "";
  }

  return (
    <>
      {travellingTrucks.map(truck => {
        const { navigation } = truck;
        const {
          nextCityId,
          currentCityId,
          startTime,
          arrivalTime
        } = navigation;
        const startCity = getCityPosition(currentCityId);
        const endCity = getCityPosition(nextCityId!);
        let travelTimePassed = time - startTime;
        const totalTime = arrivalTime - startTime;
        const percentTravelled = travelTimePassed / totalTime;

        const latDifference = endCity[0] - startCity[0];
        const longDifference = endCity[1] - startCity[1];

        const start: LatLngTuple = [
          startCity[0] + percentTravelled * latDifference,
          startCity[1] + percentTravelled * longDifference
        ];

        const positions = [start, endCity];

        return (
          <div key={truck.id}>
            <Polyline positions={positions} />
            <Marker key={truck.id} position={start} icon={getTruckIcon(truck)}>
              <Tooltip>
                {truck.name} travelling to {getCityName(nextCityId!)}
              </Tooltip>
            </Marker>
          </div>
        );
      })}
    </>
  );
}
