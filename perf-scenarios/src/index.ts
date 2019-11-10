const axios = require("axios");
const uuid = require("uuid/v4");

const users = 500;
const batchSize = 25;
let usersCreated = 0;

async function createUser(username: string, index: number) {
  const response = await axios.post(`http://localhost:9007/auth/create`, {
    username,
    password: "does not matter"
  });
  console.log(`User = ${response.data.name} created.`);
  console.log(`Users ${++usersCreated}/${users} created.`);
  return response.data;
}

async function testsUsersParallel() {
  const startTime = Date.now();

  const promises = [];
  for (let i = 0; i < users; i++) {
    const id = uuid();
    const username = `user_${id}`;
    promises.push(createUser(username, i));
  }

  return axios.all(promises).then(responses => {
    console.log(responses);
    const endTime = Date.now();
    const ms = endTime - startTime;
    console.log(`Took ${ms}ms to create ${users}`);
  });
}

async function testUsersSerial() {
  const startTime = Date.now();

  const promiseFactories = [];
  for (let i = 0; i < users;) {
      const promises = [];
      for (let j = 0; j < batchSize; j++, i++) {
          const id = uuid();
          const username = `user_${id}`;
          promises.push(() => createUser(username, i));
      }
      promiseFactories.push(() => Promise.all(promises.map(p => p())));
  }
  return promiseFactories
    .reduce((acc, current) => {
      return acc.then(current);
    }, Promise.resolve())
    .then(() => {
      const endTime = Date.now();
      const ms = endTime - startTime;
      console.log(`Took ${ms}ms to create ${users} users serially`);
    });
}

// testsUsersParallel();
testUsersSerial();
