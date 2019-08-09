World service
---

This services stores data about the world tiles.

Endpoints
---

There are currently 3 endpoints.

```
GET /world
```
Returns all tiles

```
GET /world/mark?x={x}&y={y}
```

Returns true or false based on whether a tile is occupied

```
POST /world/mark?x={x}&y={y}&mark={boolean}
```

Marks a tile as occupied or not based on 'mark' parameter.