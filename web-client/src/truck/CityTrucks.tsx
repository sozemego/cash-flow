import React from "react";
import { CircleMarker, Marker, Polyline, Tooltip } from "react-leaflet";
import { Tag } from "antd";
import { LatLngTuple } from "leaflet";
import { getTruckIcon } from "../game/business";
import { ITruck, TruckMapTooltipProps } from "./index";
import { useGetCities } from "../world/selectors";
import { CityTrucksProps } from "../game";
import { useGetTrucks } from "./selectors";
import { Storage } from "../storage/Storage";
import { truckSelected } from "../game/actions";
import { useDispatch } from "react-redux";
import { useGetSelectedTruckId } from "../game/selectors";
import { useGameClock } from "../clock/useGameClock";

export function CityTrucks({ zoom }: CityTrucksProps) {
  const dispatch = useDispatch();
  const cities = useGetCities();
  const trucks = useGetTrucks();
  const selectedTruckId = useGetSelectedTruckId();

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

  function isSelected(truckId: string): boolean {
    return selectedTruckId === truckId;
  }

  useGameClock({ interval: 2500 });

  return (
    <>
      {Object.entries(trucksPerCity).map(entry => {
        const trucks = entry[1];
        return trucks.map((truck, index) => (
          <Marker
            key={truck.id}
            position={getPosition(truck, index + 1)}
            icon={getTruckIcon(truck)}
            onClick={() => dispatch(truckSelected(truck.id))}
          >
            {isSelected(truck.id) && (
              <CircleMarker
                center={getPosition(truck, index + 1)}
                radius={15}
              />
            )}
            <Tooltip>
              <div>{selectedTruckId === truck.id ? "SELECTED" : ""}</div>
              <TruckMapTooltip truck={truck} />
            </Tooltip>
          </Marker>
        ));
      })}
    </>
  );
}

export function TruckMapTooltip({ truck }: TruckMapTooltipProps) {
  return (
    <div>
      <Tag color={"red"}>{truck.name}</Tag>
      <span>Speed: {truck.speed}km/h</span>
      <Storage storage={truck.storage} />
    </div>
  );
}

export interface TooltipTravelLineProps {
  cityId: string | null;
}

export function TooltipTravelLine({ cityId }: TooltipTravelLineProps) {
  const cities = useGetCities();
  const city = cities[cityId as string];
  const selectedTruckId = useGetSelectedTruckId();
  const trucks = useGetTrucks();
  const truck = trucks[selectedTruckId];

  function getPositionsToCity(): LatLngTuple[] {
    const currentCity = cities[truck.navigation.currentCityId];
    return [
      [currentCity!.latitude, currentCity!.longitude],
      [city.latitude, city.longitude]
    ];
  }

  return (
    <div>{city && truck && <Polyline positions={getPositionsToCity()} />}</div>
  );
}
