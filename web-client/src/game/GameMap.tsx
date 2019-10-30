import React from "react";
import { Icon } from "antd";
import Modal from "antd/lib/modal";
import { Marker, TileLayer, Map, Tooltip, Polyline } from "react-leaflet";
import Leaflet from "leaflet";
import { LatLngTuple } from "leaflet";
import { useGetCities } from "../world/selectors";
import { ICity } from "../world";
import { useGetTrucks } from "../truck/selectors";
import { ITruck } from "../truck";
import { useGameClock } from "../clock/useGameClock";

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

export function GameMap() {
  const position: LatLngTuple = [50.6625, 17.9262];
  const zoom = 5;

  const cities: ICity[] = Object.values(useGetCities());
  const trucks: ITruck[] = Object.values(useGetTrucks());
  const trucksAtCity = trucks.filter(
    truck => truck.navigation.nextCityId === null
  );
  const travellingTrucks = trucks.filter(
    truck => truck.navigation.nextCityId !== null
  );

  function getPosition(truck: ITruck): LatLngTuple {
    return randomDeviation(getCityPosition(truck.navigation.currentCityId));
  }

  function getCityPosition(cityId: string): LatLngTuple {
    const city = cities.find(city => city.id === cityId);
    if (!city) {
      return [0, 0];
    }
    return [city.latitude, city.longitude];
  }

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

  function getCityName(cityId: string): string {
    const city = cities.find(city => city.id === cityId);
    return city ? city.name : '';
  }

  const { time } = useGameClock({ interval: 1000 });

  return (
    <Map
      center={position}
      zoom={zoom}
      style={{ height: "620px", width: "auto" }}
    >
      <TileLayer
        attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {cities.map(city => (
        <Marker key={city.id} position={[city.latitude, city.longitude]}>
          <Tooltip>{city.name}</Tooltip>
        </Marker>
      ))}
      {trucksAtCity.map(truck => (
        <Marker
          key={truck.id}
          position={getPosition(truck)}
          icon={getTruckIcon(truck)}
        />
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
          <>
            <Polyline positions={positions} />
            <Marker key={truck.id} position={start} icon={getTruckIcon(truck)}>
              <Tooltip>{truck.name} travelling to {getCityName(nextCityId!)}</Tooltip>
            </Marker>
          </>
        );
      })}
    </Map>
  );
}

export function GameMapIcon() {
  const [showMap, setShowMap] = React.useState<boolean>(false);

  return (
    <>
      <Icon type="global" onClick={() => setShowMap(!showMap)} />
      <Modal
        title="World map"
        visible={showMap}
        width={`${window.innerWidth - 50}px`}
        onOk={() => setShowMap(false)}
        onCancel={() => setShowMap(false)}
      >
        <GameMap />
      </Modal>
    </>
  );
}
