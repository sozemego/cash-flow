const socket = new WebSocket("ws://localhost:9001/factory/websocket");

socket.onopen = function onOpen(arg) {
    start();
};

socket.onerror = function onError(error) {
    console.log(error);
};

function start() {
    socket.onmessage = function onMessage(msg) {
        if (msg.data) {
            const data = JSON.parse(msg.data);
            if (data.type == 'RESOURCE_PRODUCED') {
                handleResourceProduced(data);
            }
            if (data.type == 'FACTORY_ADDED') {
                handleFactoryAdded(data);
            }
            if (data.type == 'RESOURCE_PRODUCTION_STARTED') {
                handleResourceProductionStarted(data);
            }
            console.log(data.type);
        }
    };
}

const factories = {};

function addFactory(factory) {
    if (factories[factory.id]) {
        removeFactory(factory);
    }
    const root = document.getElementById("root");
    const factoryDiv = createDom(factory);
    root.appendChild(factoryDiv);
    factories[factory.id] = {
        factory,
        dom: factoryDiv
    };
}

function updateFactory(factory) {
    const data = factories[factory.id];
    if (!data) {
        return;
    }
    /**
     * @type HTMLElement
     */
    const dom = factories[factory.id].dom;
    const newDom = createDom(factory);
    dom.parentElement.replaceChild(newDom, dom);
    factories[factory.id] = {
        factory,
        dom: newDom
    };
}

function removeFactory(factory) {
    /**
     * @type HTMLElement
     */
    const dom = factories[factory.id].dom;
    dom.parentElement.removeChild(dom);

    delete factories[factory.id];
}

function createDom(factory) {
    const factoryDiv = document.createElement("div");
    factoryDiv.innerHTML = factoryTemplate(factory);
    return factoryDiv;
}

function factoryTemplate(factory) {
    const {producer} = factory;
    const {time, progress, resource} = producer;
    console.log(producer);
    const timeLeft = (time - progress) / 1000;
    const resourceCount = getAllResourceCount(factory);
    const {producing} = producer;
    return `
    <div class="factory-container">
      <div>
        <span class="smaller">
            ${factory.id}
        </span>
      </div>
      <div class="factory-data-container">
        <div>
          <span>
              ${factory.name}
          </span>   
        </div>
        <div>
          <span>Storage - [${resourceCount} / ${factory.storage.capacity}]</span>
        </div>
        <div>
          Production - [${resource} ${producing ? '🚴🏿‍♂️'
        : '😴'}] Time left: ${timeLeft}s
        </div>
      </div>
   </div>
  `
}

function getAllResourceCount(factory) {
    const {storage} = factory;
    return Object.values(storage.resources).reduce((total, next) => {
        return total + next;
    }, 0);
}

function handleResourceProduced(message) {
    const {factoryId, resource} = message;
    const {factory} = factories[factoryId];
    const {storage, producer} = factory;

    const count = storage.resources[resource] || 0;
    storage.resources[resource] = count + 1;
    producer.progress = 0;
    producer.producing = false;
    updateFactory(factory);
}

function handleFactoryAdded(message) {
    const {factoryDTO} = message;
    addFactory(factoryDTO);
}

function handleResourceProductionStarted(message) {
    const {factory} = factories[message.factoryId];
    if (!factory) {
        return;
    }
    factory.producer.producing = true;
    updateFactory(factory);
}