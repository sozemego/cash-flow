import React from "react";

export function ProgressBar({current = 0, time = 0}) {

	let percent = (current / time) * 100;
	if (isNaN(percent)) {
		percent = 0;
	}

	const style =
		percent == 0
			? {display: "none"}
			: {
				width: `${percent}%`,
				border: "1px solid red"
			};

	return (
		<div style={{border: "1px solid gray", height: "2px", width: "100%"}}>
			<div style={style}/>
		</div>
	);
}
