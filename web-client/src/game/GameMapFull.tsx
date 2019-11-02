import { Map, Marker, Polyline, TileLayer, Tooltip } from "react-leaflet";
import { CityMapTooltip } from "../world/CityMapTooltip";
import React from "react";
import Leaflet, { Icon, LatLngTuple } from "leaflet";
import { useGetCities } from "../world/selectors";
import { useDispatch } from "react-redux";
import { citySelected, truckSelected } from "./actions";
import { useGetTrucks } from "../truck/selectors";
import { ITruck } from "../truck";
import { useGameClock } from "../clock/useGameClock";
import { useTruckSocket } from "../truck/useTruckSocket";
import { createTruckTravelMessage } from "../truck/message";
import { useGetSelectedTruckId } from "./selectors";

export interface GameMapFullProps {
  height: number;
}

export function GameMapFull({ height }: GameMapFullProps) {
  const dispatch = useDispatch();
  const trucks = useGetTrucks();
  const { socket } = useTruckSocket();
  const selectedTruckId = useGetSelectedTruckId();

  let [zoom, setZoom] = React.useState(5);
  zoom = zoom > 7 ? 7 : zoom;
  zoom = zoom < 3 ? 3 : zoom;
  const position: LatLngTuple = [50.6625, 17.9262];
  const cities = useGetCities();

  const travellingTrucks = Object.values(trucks).filter(
    truck => truck.navigation.nextCityId !== null
  );
  const trucksAtCity = Object.values(trucks).filter(
    truck => truck.navigation.nextCityId === null
  );
  const trucksPerCity: Record<string, ITruck[]> = {};
  trucksAtCity.forEach(truck => {
    const trucks = trucksPerCity[truck.navigation.currentCityId] || [];
    trucks.push(truck);
    trucksPerCity[truck.navigation.currentCityId] = trucks;
  });

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

  function getPosition(truck: ITruck, index: number): LatLngTuple {
    const { currentCityId } = truck.navigation;
    const cityPosition = getCityPosition(currentCityId);
    if (cityPosition[0] === 0 && cityPosition[1] === 0) {
      return cityPosition;
    }
    return [cityPosition[0], cityPosition[1] + 0.1 + index * 0.02 * zoom];
  }

  const { time } = useGameClock({ interval: 2500 });

  const mapRef = React.useRef<Map>();

  React.useLayoutEffect(() => {
    mapRef.current!.leafletElement.setMinZoom(3);
    mapRef.current!.leafletElement.setMaxZoom(7);
  });

  return (
    <Map
      center={position}
      zoom={zoom}
      style={{ height, width: "auto" }}
      onViewportChanged={viewport => setZoom(viewport.zoom as number)}
      zoomControl={false}
      //@ts-ignore
      ref={mapRef}
    >
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {Object.values(cities).map(city => (
        <Marker
          key={city.id}
          position={[city.latitude, city.longitude]}
          icon={cityIcon}
          onClick={() => {
            if (selectedTruckId) {
              socket.send(createTruckTravelMessage(selectedTruckId, city.id));
              dispatch(truckSelected(""));
            } else {
              dispatch(citySelected(city.id));
              dispatch(truckSelected(""));
            }
          }}
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
  iconSize: [32, 32]
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
