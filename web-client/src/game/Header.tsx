import { Clock } from "../clock/Clock";
import { Player } from "../player/Player";
import React, { useState } from "react";
import { useGetResources } from "../world/selectors";
import Icon from "antd/lib/icon";
import Modal from "antd/lib/modal";
import Table from "antd/lib/table";
import { ResourceIcon } from "../components/ResourceIcon";
import { useGetFactories } from "../factory/selectors";
import {IStorageSlot} from "../factory/index.d";

export function Header(props) {
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
          <Clock />
          <Player />
        </div>
        <div>
          <Resources />
        </div>
      </div>
      <hr />
    </>
  );
}

function calcTotalResourceCounts(factories) {
  const counts = {};
  factories.forEach(factory => {
    const { storage } = factory;
    Object.entries(storage).forEach(([resource, slot]) => {
      const actualCount = counts[resource] || 0;
      counts[resource] = actualCount + (slot as IStorageSlot).count;
    })
  });
  return counts;
}

function Resources() {
  const [show, setShow] = useState(false);
  const resources = useGetResources();
  const factories = useGetFactories();
  const resourceCounts = calcTotalResourceCounts(factories);

  function close() {
    setShow(false);
  }

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: name => {
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
    { title: "In factories", dataIndex: "countInFactories", key: "countInFactories" }
  ];
  const data = Object.values(resources).map(resource => {
    return {
      key: resource.name,
      name: resource.name,
      price: `${resource.minPrice} - ${resource.maxPrice}`,
      countInFactories: resourceCounts[resource.name.toUpperCase()]
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
