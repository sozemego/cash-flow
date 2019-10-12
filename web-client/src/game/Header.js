import { Clock } from "../clock/Clock";
import { Player } from "../player/Player";
import React, { useState } from "react";
import { useGetResources } from "../world/selectors";
import Icon from "antd/lib/icon";
import Modal from "antd/lib/modal";
import Table from "antd/lib/table";
import { ResourceIcon } from "../components/ResourceIcon";

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

function Resources() {
  const [show, setShow] = useState(false);
  const resources = useGetResources();

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
        return <div><ResourceIcon resource={name.toLowerCase()}/>{name}</div>;
      }
    },
    { title: "Min price", dataIndex: "minPrice", key: "minPrice" },
    { title: "Max price", dataIndex: "maxPrice", key: "maxPrice" }
  ];
  const data = Object.values(resources).map(resource => {
    return {
      key: resource.name,
      name: resource.name,
      minPrice: resource.minPrice,
      maxPrice: resource.maxPrice
    };
  });

  console.log(data);

  return (
    <>
      <Icon type="appstore" onClick={() => setShow(true)} />
      <Modal visible={show} onCancel={close} onOk={close} title={"Resources"}>
        <Table dataSource={data} columns={columns} pagination={false} />
      </Modal>
    </>
  );
}
