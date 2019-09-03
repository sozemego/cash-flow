import React from 'react';

export function ResourceIcon({resource}) {

	return (
		<img
			src={`/img/resources/${resource}.png`} alt={resource}
			style={{ width: "48px" }}
		/>
	)
}