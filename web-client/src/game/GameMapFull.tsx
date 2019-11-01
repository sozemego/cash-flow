import { Map, Marker, Polyline, TileLayer, Tooltip } from "react-leaflet";
import { CityMapTooltip } from "../world/CityMapTooltip";
import React from "react";
import Leaflet, { Icon, LatLngTuple } from "leaflet";
import { useGetCities } from "../world/selectors";
import { useDispatch } from "react-redux";
import { citySelected } from "./actions";
import { useGetTrucks } from "../truck/selectors";
import { ITruck } from "../truck";
import { useGameClock } from "../clock/useGameClock";

export interface GameMapFullProps {
  height: number;
}

const deviationCache: Record<string, LatLngTuple> = {};

function randomDeviation(position: LatLngTuple): LatLngTuple {
  const deviation = 0.005;
  return [
    position[0] + getRandomArbitrary(-deviation, deviation),
    position[1] + getRandomArbitrary(-deviation, deviation)
  ];
}

function getRandomArbitrary(min: number, max: number) {
  return Math.random() * (max - min) + min;
}

export function GameMapFull({ height }: GameMapFullProps) {
  const dispatch = useDispatch();
  const trucks = useGetTrucks();
  const [zoom, setZoom] = React.useState(5);
  const position: LatLngTuple = [50.6625, 17.9262];
  const cities = useGetCities();

  const travellingTrucks = Object.values(trucks).filter(
    truck => truck.navigation.nextCityId !== null
  );
  const trucksAtCity = Object.values(trucks).filter(
    truck => truck.navigation.nextCityId === null
  );

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

  function getPosition(truck: ITruck): LatLngTuple {
    const { currentCityId } = truck.navigation;
    let cacheHit: LatLngTuple = deviationCache[truck.id + currentCityId];
    if (!cacheHit) {
      cacheHit = deviationCache[truck.id + currentCityId] = randomDeviation(
        getCityPosition(truck.navigation.currentCityId)
      );
    }
    return cacheHit;
  }

  const { time } = useGameClock({ interval: 2500 });

  return (
    <Map
      center={position}
      zoom={zoom}
      style={{ height, width: "auto" }}
      onViewportChanged={viewport => setZoom(viewport.zoom as number)}
      zoomControl={false}
    >
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {trucksAtCity.map(truck => (
        <Marker
          key={truck.id}
          position={getPosition(truck)}
          icon={getTruckIcon(truck)}
        />
      ))}
      {Object.values(cities).map(city => (
        <Marker
          key={city.id}
          position={[city.latitude, city.longitude]}
          icon={cityIcon}
          onClick={() => dispatch(citySelected(city.id))}
        >
          <Tooltip>
            <CityMapTooltip city={city} />
          </Tooltip>
        </Marker>
      ))}
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
    </Map>
  );
}

const cityIcon = new Icon({
  iconUrl: `img/city_icon.png`,
  iconRetinaUrl: `img/city_icon.png`,
  iconAnchor: undefined,
  popupAnchor: undefined,
  shadowUrl: undefined,
  shadowSize: undefined,
  shadowAnchor: undefined,
  iconSize: [24, 24]
});

function getTruckIcon(truck: ITruck): Leaflet.Icon {
  return new Leaflet.Icon({
    iconUrl: `img/truck/${truck.texture}`,
    iconRetinaUrl: `img/truck/${truck.texture}`,
    iconAnchor: undefined,
    popupAnchor: undefined,
    shadowUrl: undefined,
    shadowSize: undefined,
    shadowAnchor: undefined,
    iconSize: [24, 24]
  });
}
