const axios = require("axios");
const uuid = require("uuid/v4");

//1. create many users
const users = 50000;

const startTime = Date.now();

//2. send requests in parallel
const promises = [];
for (let i = 0; i < users; i++) {
  const id = uuid();
  const promise = axios
    .post(`http://localhost:9007/auth/create`, {
      username: `user_${id}`,
      password: "does not matter"
    })
    .then(response => {
      console.log(`OK response for ${id}`);
      return response.data;
    });
  promises.push(promise);
}

axios.all(promises).then(responses => {
  console.log(responses);
  const endTime = Date.now();
  const ms = endTime - startTime;
  console.log(`Took ${ms}ms to create ${users}`);
});

//3. send requests serially
