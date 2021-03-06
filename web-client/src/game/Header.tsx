import { Clock } from "../clock/Clock";
import { Player } from "../player/Player";
import React, { useState } from "react";
import { useGetResources } from "../world/selectors";
import Icon from "antd/lib/icon";
import Modal from "antd/lib/modal";
import Table from "antd/lib/table";
import { ResourceIcon } from "../components/ResourceIcon";
import { useGetFactories } from "../factory/selectors";
import { IFactory, IStorageSlot } from "../factory";
import { ResourceMap } from "../world";
import { ResourceData, ResourceDatas } from "./index";
import { GameHeader } from "./GameHeader";
import { useGetUser } from "../auth/selectors";
import { useDispatch } from "react-redux";
import { userLoggedOut } from "../auth/actions";

export function Header() {
  const user = useGetUser();
  return (
    <>
      <div
        style={{
          textAlign: "center",
          display: "flex",
          justifyContent: "space-around"
        }}
      >
        <div></div>
        <div>
          {user && <Clock />}
          {user && <Player />}
        </div>
        <div>
          <Resources />
          {user && <LogoutIcon />}
        </div>
      </div>
      <hr />
      <GameHeader />
    </>
  );
}

function calcTotalResources(
  factories: IFactory[],
  resources: ResourceMap
): ResourceDatas {
  const datas: ResourceDatas = {};
  factories.forEach(factory => {
    const { storage } = factory;
    Object.entries(storage).forEach(([resource, slot]) => {
      slot = slot as IStorageSlot;
      const defaultData: ResourceData = {
        count: 0,
        averagePrice: 0,
        saturation: 0,
        factories: 0
      };
      const data = datas[resource] || defaultData;
      data.count += slot.count;
      data.averagePrice += slot.price;
      data.saturation += slot.count / slot.capacity;
      data.factories += 1;
      datas[resource] = data;
    });
  });
  Object.keys(resources).forEach(resource => {
    const data = datas[resource];
    if (!data) {
      return;
    }
    data.averagePrice = data.averagePrice / data.factories;
    data.saturation = data.saturation / data.factories;
  });
  return datas;
}

function Resources() {
  const [show, setShow] = useState(false);
  const resources = useGetResources();
  const factories = useGetFactories();
  const totalResources = calcTotalResources(factories, resources);

  function close() {
    setShow(false);
  }

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: (name: string) => {
        name = name[0].toUpperCase() + name.substr(1).toLowerCase();
        return (
          <div>
            <ResourceIcon resource={name.toLowerCase()} />
            {name}
          </div>
        );
      }
    },
    { title: "Price", dataIndex: "price", key: "price" },
    {
      title: "In factories",
      dataIndex: "countInFactories",
      key: "countInFactories"
    },
    {
      title: "Average price / saturation",
      dataIndex: "priceSaturation",
      key: "priceSaturation"
    }
  ];
  const data = Object.values(resources).map(resource => {
    const resourceName = resource.name.toUpperCase();
    const data = totalResources[resourceName] || {
      count: 0,
      saturation: 0,
      averagePrice: 0
    };
    return {
      key: resource.name,
      name: resource.name,
      price: `${resource.minPrice} - ${resource.maxPrice}`,
      countInFactories: data.count,
      priceSaturation: `${data.averagePrice.toFixed(1)} (${(
        data.saturation * 100
      ).toFixed(0)}%)`
    };
  });

  return (
    <>
      <Icon type="appstore" onClick={() => setShow(true)} />
      <Modal visible={show} onCancel={close} onOk={close} title={"Resources"}>
        <Table dataSource={data} columns={columns} pagination={false} />
      </Modal>
    </>
  );
}

export function LogoutIcon() {
  const dispatch = useDispatch();
  return <Icon type="logout" onClick={() => dispatch(userLoggedOut())} />;
}
